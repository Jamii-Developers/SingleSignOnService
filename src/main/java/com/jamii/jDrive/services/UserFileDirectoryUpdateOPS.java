package com.jamii.jDrive.services;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.utils.JamiiStringUtils;
import com.jamii.jDrive.controllers.FileDirectory;
import com.jamii.jDrive.controllers.FileTableOwner;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jDrive.requests.UserFileDirectoryUpdateREQ;
import com.jamii.jDrive.responses.UserFileDirectoryUpdateRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Service for handling user file directory update operations in the Jamii Drive system.
 * 
 * <p>This service allows authenticated users to move their files between different
 * directories within their personal storage space. The service validates user permissions,
 * verifies file accessibility, and updates directory information in the database.</p>
 * 
 * <p>Operation flow:</p>
 * <ol>
 *   <li>Extract authentication keys from request in {@link #validateCookie()}</li>
 *   <li>Validate user session and ensure user is active</li>
 *   <li>Locate file record in database using filename</li>
 *   <li>Verify file ownership and current status</li>
 *   <li>Check if target directory is different from current</li>
 *   <li>Update file directory record with new location</li>
 *   <li>Return update confirmation response</li>
 * </ol>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Directory-based file organization</li>
 *   <li>Ownership verification for security</li>
 *   <li>Duplicate move prevention</li>
 *   <li>Automatic timestamp updates</li>
 * </ul>
 * 
 * <p>Error conditions:</p>
 * <ul>
 *   <li>Invalid or expired user session</li>
 *   <li>User not found or inactive</li>
 *   <li>File not found or not accessible</li>
 *   <li>File already in trash or deleted</li>
 *   <li>Target directory same as current location</li>
 * </ul>
 * 
 * <p>Security considerations:</p>
 * <ul>
 *   <li>Users can only move their own files</li>
 *   <li>Directory path validation prevents unauthorized access</li>
 *   <li>Status validation prevents operations on deleted files</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see UserFileDirectoryUpdateREQ
 * @see FileDirectory
 * @see FileTableOwner
 */
@Service
public class UserFileDirectoryUpdateOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private FileTableOwner fileTableOwner;
    @Autowired private FileDirectory fileDirectory;
    
    protected UserFileDirectoryUpdateREQ req = null;

    /**
     * Maps the incoming request to a {@link UserFileDirectoryUpdateREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new UserFileDirectoryUpdateREQ();
        req = (UserFileDirectoryUpdateREQ) JamiiMapperUtils.mapObject(getRequest(), UserFileDirectoryUpdateREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
    }

    /**
     * Processes the file directory update request.
     * 
     * <p>Validates user permissions, verifies file accessibility, and updates
     * the file's directory location. Prevents duplicate moves and operations
     * on files that are in trash or deleted.</p>
     * 
     * <p>Process steps:</p>
     * <ol>
     *   <li>Validate user exists and is active</li>
     *   <li>Locate file record using filename</li>
     *   <li>Verify file is not in trash or deleted</li>
     *   <li>Check if target directory differs from current</li>
     *   <li>Update directory record with new location</li>
     *   <li>Update timestamp for audit trail</li>
     * </ol>
     * 
     * @throws IOException if file processing fails
     */
    @Override
    public void processRequest()
            throws IOException
    {

        if (!getIsSuccessful()) {
            return;
        }

        UserFileDirectoryUpdateREQ req = (UserFileDirectoryUpdateREQ) JamiiMapperUtils.mapObject(getRequest(), UserFileDirectoryUpdateREQ.class);

        this.userLogin.data = this.userLogin.fetchByUserKey(req.getUserKey(), UserLogin.ACTIVE_ON).orElse(null);
        if (this.userLogin.data == null) {
            jamiiDebug.warning("This user key does not exists : " + req.getUserKey());
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryOPS_NoMatchingUserKey();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        this.fileTableOwner.data = this.fileTableOwner.fetch(this.userLogin.data, req.getFileName()).orElse(null);
        if (this.fileTableOwner.data == null) {
            jamiiDebug.warning("This the file is in trash or has been deleted from the system: " + req.getFileName());
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryOPS_FileIsInTrash();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        ArrayList<Integer> checkAvailability = new ArrayList<>(Arrays.asList(FileTableOwner.ACTIVE_STATUS_DELETED, FileTableOwner.ACTIVE_STATUS_IN_TRASH));
        if (this.fileTableOwner.checkStatus(checkAvailability)) {
            jamiiDebug.warning("This the file is in trash or has been deleted from the system: " + req.getFileName());
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryOPS_FileIsInTrash();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        this.fileDirectory.data = this.fileDirectory.fetch(this.userLogin.data, this.fileTableOwner.data).orElse(null);
        if (this.fileDirectory.data != null && JamiiStringUtils.equals(req.getDirectoryUpdate(), this.fileDirectory.data.getUidirectory())) {
            jamiiDebug.warning("File is already in said location: " + req.getFileName());
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryUpdateOPS_FileIsAlreadyInThisDirectory();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        assert this.fileDirectory.data != null;
        this.fileDirectory.data.setUidirectory(req.getDirectoryUpdate());
        this.fileDirectory.data.setLastupdated(LocalDateTime.now());
        this.fileDirectory.save();
    }

    /**
     * Generates the HTTP response for the directory update operation.
     * 
     * <p>Returns a success response with update confirmation if the operation
     * completed successfully, otherwise delegates error handling to the parent class.</p>
     * 
     * @return ResponseEntity containing the update response or error details
     */
    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {
            UserFileDirectoryUpdateRESP response = new UserFileDirectoryUpdateRESP();
            return new ResponseEntity<>(response.getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
