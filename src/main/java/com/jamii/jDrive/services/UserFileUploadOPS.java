package com.jamii.jDrive.services;

import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jDrive.controllers.FileDirectory;
import com.jamii.jDrive.controllers.FileTableOwner;
import com.jamii.jDrive.model.FileTableOwnerTBL;
import com.jamii.jDrive.requests.UserFileUploadServicesREQ;
import com.jamii.jDrive.responses.UserFileUploadRESP;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.sysconfigs.FileServerConfigs;
import com.jamii.utils.JamiiMapperUtils;
import com.jamii.utils.JamiiRandomKeyToolGen;
import com.jamii.utils.JamiiUploadFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Service for handling user file upload operations in the Jamii Drive system.
 * 
 * <p>This service allows authenticated users to upload files to their personal storage space.
 * The service handles file validation, generates unique system filenames, creates database
 * records, and stores files in the configured file system location.</p>
 * 
 * <p>Operation flow:</p>
 * <ol>
 *   <li>Extract authentication keys from request in {@link #validateCookie()}</li>
 *   <li>Validate user session and ensure user is active</li>
 *   <li>Generate unique system filename using random key generator</li>
 *   <li>Create database record in file_table_owner table</li>
 *   <li>Create file directory record for organization</li>
 *   <li>Save physical file to storage location</li>
 *   <li>Return upload success response</li>
 * </ol>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Automatic system filename generation (25-character alphanumeric)</li>
 *   <li>File metadata tracking (type, size, original name)</li>
 *   <li>User-specific storage organization</li>
 *   <li>Database and file system consistency</li>
 * </ul>
 * 
 * <p>Error conditions:</p>
 * <ul>
 *   <li>Invalid or expired user session</li>
 *   <li>User not found or inactive</li>
 *   <li>File storage failures</li>
 *   <li>Database operation failures</li>
 * </ul>
 * 
 * <p>Performance considerations:</p>
 * <ul>
 *   <li>Files stored in user-specific directories for better organization</li>
 *   <li>System filenames prevent conflicts and enhance security</li>
 *   <li>Database operations optimized for concurrent uploads</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see UserFileUploadServicesREQ
 * @see FileTableOwnerTBL
 * @see JamiiUploadFileUtils
 */
@Service
public class UserFileUploadOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private FileTableOwner fileTableOwner;
    @Autowired private FileDirectory fileDirectory;
    @Autowired private JamiiUploadFileUtils jamiiUploadFileUtils;
    
    protected UserFileUploadServicesREQ req = null;

    /**
     * Generates a unique system filename for uploaded files.
     * 
     * <p>Creates a 25-character alphanumeric string that serves as a unique
     * identifier for files in the storage system. This prevents filename conflicts
     * and enhances security by obscuring the original file names.</p>
     * 
     * @return A 25-character random alphanumeric string
     */
    protected static String generateFileKey()
    {
        JamiiRandomKeyToolGen keyToolGen = new JamiiRandomKeyToolGen();
        keyToolGen.setLen(25);
        keyToolGen.setInclude_letters(true);
        keyToolGen.setInclude_numbers(true);
        return keyToolGen.generate();
    }

    /**
     * Maps the incoming request to a {@link UserFileUploadServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        // Extract authentication keys from the incoming request
        UserFileUploadServicesREQ tempReq = (UserFileUploadServicesREQ) JamiiMapperUtils.mapObject(getRequest(), UserFileUploadServicesREQ.class);
        
        // Use the specialized mapUploadFileObject method to create the proper request object
        req = JamiiMapperUtils.mapUploadFileObject(
            tempReq.getUserKey(),
            tempReq.getDeviceKey(),
            tempReq.getSessionKey(),
            tempReq.getUploadfile()
        );
        
        // Set authentication keys for session validation
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
    }

    /**
     * Processes the file upload request.
     * 
     * <p>Handles the complete file upload workflow including user validation,
     * file metadata creation, directory management, and physical file storage.</p>
     * 
     * <p>Process steps:</p>
     * <ol>
     *   <li>Validate user exists and is active</li>
     *   <li>Generate unique system filename</li>
     *   <li>Create file metadata record in database</li>
     *   <li>Create directory entry for file organization</li>
     *   <li>Save physical file to storage location</li>
     * </ol>
     * 
     * @throws Exception if file upload processing fails
     */
    @Override
    public void processRequest()
            throws Exception
    {

        if (!getIsSuccessful()) {
            return;
        }

        UserFileUploadServicesREQ req = (UserFileUploadServicesREQ) getRequest();
        this.userLogin.data = this.userLogin.fetchByUserKey(req.getUserKey(), UserLogin.ACTIVE_ON).orElse(null);
        if (this.userLogin.data == null) {
            jamiiDebug.warning("This user key does not exists : " + req.getUserKey());
            this.jamiiErrorsMessagesRESP.setUploadFileOPS_NoMatchingUserKey();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            return;
        }

        String sysFileName = generateFileKey();
        FileTableOwnerTBL fileTableOwnerTBL = this.fileTableOwner.add(getFileOwnerRecord(this.userLogin.data, sysFileName));
        this.fileDirectory.createFileDirectory(this.userLogin.data, fileTableOwnerTBL, "./");
        saveUploadedFile(this.userLogin.data.getIdAsString(), sysFileName);

        setIsSuccessful(true);
    }

    /**
     * Generates the HTTP response for the file upload operation.
     * 
     * <p>Returns a success response with upload confirmation if the operation
     * completed successfully, otherwise delegates error handling to the parent class.</p>
     * 
     * @return ResponseEntity containing the upload response or error details
     */
    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {
            UserFileUploadRESP userFileUploadRESP = new UserFileUploadRESP();
            return new ResponseEntity<>(userFileUploadRESP.getJSONRESP(), HttpStatus.OK);
        }
        return super.getResponse();
    }

    /**
     * Creates a file ownership record for database storage.
     * 
     * <p>Constructs a FileTableOwnerTBL entity with all necessary metadata
     * including file location, type, size, original and system filenames,
     * and timestamps.</p>
     * 
     * @param user The user who owns the file
     * @param sysFileName The generated system filename
     * @return A populated FileTableOwnerTBL entity ready for database insertion
     */
    protected FileTableOwnerTBL getFileOwnerRecord(UserLoginTBL user, String sysFileName)
    {
        UserFileUploadServicesREQ req = (UserFileUploadServicesREQ) getRequest();
        FileTableOwnerTBL fileTableOwnerTBL = new FileTableOwnerTBL();
        fileTableOwnerTBL.setFilelocation(FileServerConfigs.USER_IMAGE_STORE + File.separator + user.getId());
        fileTableOwnerTBL.setFiletype(req.getUploadfile().getContentType());
        fileTableOwnerTBL.setUserloginid(user);
        fileTableOwnerTBL.setFilesize(req.getUploadfile().getSize());
        fileTableOwnerTBL.setOriginalfilename(req.uploadFile.getOriginalFilename());
        fileTableOwnerTBL.setSystemfilename(sysFileName);
        fileTableOwnerTBL.setStatus(FileTableOwner.ACTIVE_STATUS_STORE);
        fileTableOwnerTBL.setDatecreated(LocalDateTime.now());
        fileTableOwnerTBL.setLastupdated(LocalDateTime.now());
        return fileTableOwnerTBL;
    }

    /**
     * Saves the uploaded file to the physical storage location.
     * 
     * <p>Configures the upload utility with the destination directory,
     * multipart file data, and system filename, then performs the actual
     * file save operation.</p>
     * 
     * @param userId The user ID for directory organization
     * @param sysFileName The system filename for the saved file
     * @throws Exception if file saving fails
     */
    protected void saveUploadedFile(String userId, String sysFileName)
            throws Exception
    {
        UserFileUploadServicesREQ req = (UserFileUploadServicesREQ) getRequest();
        this.jamiiUploadFileUtils.setDestDirectory(FileServerConfigs.USER_IMAGE_STORE + File.separator + userId);
        this.jamiiUploadFileUtils.setMultipartFile1(req.getUploadfile());
        this.jamiiUploadFileUtils.setSystemFilename(sysFileName);
        this.jamiiUploadFileUtils.save();
    }
}
