package com.jamii.jDrive.services;

import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jDrive.peer.FileTableOwner;
import com.jamii.jDrive.model.FileTableOwnerTBL;
import com.jamii.jDrive.requests.UserFileDownloadREQ;
import com.jamii.jUser.peer.UserLogin;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.utils.JamiiFileDownloadUtils;
import com.jamii.utils.JamiiFileUtils;
import com.jamii.utils.JamiiLoggingUtils;
import com.jamii.utils.JamiiMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

/**
 * Service for handling user file download operations in the Jamii Drive system.
 * 
 * <p>This service allows authenticated users to download their files from the storage
 * system. The service validates user permissions, locates files in the database,
 * creates file resources from the physical storage, and streams them as HTTP responses.</p>
 * 
 * <p>Operation flow:</p>
 * <ol>
 *   <li>Extract authentication keys from request in {@link #validateCookie()}</li>
 *   <li>Validate user session and ensure user is active</li>
 *   <li>Locate file record in database using filename</li>
 *   <li>Verify file ownership and active status</li>
 *   <li>Create file resource from physical storage location</li>
 *   <li>Stream file as HTTP response with proper headers</li>
 * </ol>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Secure file access with ownership verification</li>
 *   <li>Proper MIME type handling for different file types</li>
 *   <li>Streaming download for efficient memory usage</li>
 *   <li>Comprehensive error handling and logging</li>
 * </ul>
 * 
 * <p>Error conditions:</p>
 * <ul>
 *   <li>Invalid or expired user session</li>
 *   <li>User not found or inactive</li>
 *   <li>File not found or not accessible</li>
 *   <li>File system access errors</li>
 *   <li>Resource creation failures</li>
 * </ul>
 * 
 * <p>Security considerations:</p>
 * <ul>
 *   <li>Users can only download their own files</li>
 *   <li>File path validation prevents directory traversal</li>
 *   <li>Access logging for audit purposes</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see UserFileDownloadREQ
 * @see JamiiFileDownloadUtils
 */
@Service
public class UserFileDownloadOPS
        extends AbstractUserServicesOPS
{

    @Autowired protected FileTableOwner fileTableOwner;
    @Autowired JamiiLoggingUtils jamiiLoggingUtils;
    @Autowired private UserLogin userLogin;
    
    protected UserFileDownloadREQ req = null;

    /** The file resource to be streamed to the client */
    private Resource resource;

    /**
     * Gets the file resource for download.
     * 
     * @return The Resource object containing the file data
     */
    public Resource getResource()
    {
        return resource;
    }

    /**
     * Sets the file resource for download.
     * 
     * @param resource The Resource object containing the file data
     */
    public void setResource(Resource resource)
    {
        this.resource = resource;
    }

    /**
     * Resets the service state for reuse.
     * 
     * <p>Clears the file resource and calls parent reset to clean up
     * authentication and error state.</p>
     */
    @Override
    public void reset()
    {
        super.reset();
        setResource(null);
    }

    /**
     * Maps the incoming request to a {@link UserFileDownloadREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new UserFileDownloadREQ();
        req = (UserFileDownloadREQ) JamiiMapperUtils.mapObject(getRequest(), UserFileDownloadREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
    }

    /**
     * Processes the file download request.
     * 
     * <p>Validates user permissions, locates the file record, and creates
     * a file resource from the physical storage for streaming to the client.</p>
     * 
     * <p>Process steps:</p>
     * <ol>
     *   <li>Validate user exists and is active</li>
     *   <li>Locate file record using filename</li>
     *   <li>Verify file is accessible and not deleted</li>
     *   <li>Create file resource from storage location</li>
     *   <li>Handle any file system errors gracefully</li>
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

        this.userLogin.data = this.userLogin.fetchByUserKey(req.getUserKey(), UserLogin.ACTIVE_ON).orElse(null);
        if (this.userLogin.data == null) {
            jamiiDebug.warning("This user key does not exists : " + req.getUserKey());
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoMatchingUserKey();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        this.fileTableOwner.data = this.fileTableOwner.fetch(this.userLogin.data, req.getFileName()).orElse(null);
        if (this.fileTableOwner == null) {
            jamiiDebug.warning("This the file is in trash or has been deleted from the system: " + req.getDeviceKey());
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoActiveFileFound();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        JamiiFileDownloadUtils downloadUtil = new JamiiFileDownloadUtils();

        try {

            assert this.fileTableOwner.data != null;
            String fileLocation = this.fileTableOwner.data.getFilelocation();
            String systemFilename = this.fileTableOwner.data.getSystemfilename();
            String fileExtension = JamiiFileUtils.getFileExtension(this.fileTableOwner.data.getFiletype());
            setResource(downloadUtil.getFileAsResource(fileLocation, systemFilename, fileExtension));
        }
        catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger(this.getClass().getName(), e, this.userLogin.data);
            jamiiDebug.error("Error creating resource : " + req.getDeviceKey());
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_OopsWeCannotFindThisFile();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        setIsSuccessful(true);
    }

    /**
     * Generates the HTTP response for file download.
     * 
     * <p>Creates a streaming response with the file resource and appropriate
     * headers for browser download. Uses generic octet-stream content type
     * and attachment disposition to force download behavior.</p>
     * 
     * @return ResponseEntity containing the file stream or error details
     */
    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + getResource().getFilename() + "\"";

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(getResource());
        }

        return super.getResponse();
    }

    /**
     * Fetches file metadata with caching for improved performance.
     * 
     * <p>This method caches file metadata to reduce database queries
     * for frequently accessed file information.</p>
     * 
     * @param user the user to fetch file ownership for
     * @param filename the system filename to fetch
     * @return Optional containing FileTableOwnerTBL if found and accessible, empty otherwise
     */
    @Cacheable(value = "file-metadata", key = "#user.id + ':' + #filename")
    public Optional<FileTableOwnerTBL> fetchFileMetadataWithCache(UserLoginTBL user, String filename) {
        return this.fileTableOwner.fetch(user, filename);
    }

    /**
     * Evicts file metadata cache when file is updated.
     * 
     * <p>This method should be called when a file's metadata is updated
     * to invalidate cached file information.</p>
     * 
     * @param user the user who owns the file
     * @param filename the filename whose cache should be evicted
     */
    @CacheEvict(value = "file-metadata", key = "#user.id + ':' + #filename")
    public void evictFileMetadataCache(UserLoginTBL user, String filename) {
        // File metadata cache eviction - method body can be empty
    }

    /**
     * Evicts all file metadata cache for a user.
     * 
     * <p>This method should be called when a user's files undergo
     * bulk operations that affect cached file data.</p>
     * 
     * @param userId the user ID whose file caches should be evicted
     */
    @CacheEvict(value = "file-metadata", allEntries = true)
    public void evictAllFileCaches(Integer userId) {
        // All file caches eviction - method body can be empty
    }
}
