package com.jamii.jDrive.services;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jDrive.controllers.FileTableOwner;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jDrive.requests.UserFileDeleteREQ;
import com.jamii.jDrive.responses.UserFileDeleteRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Service for handling user file deletion operations in the Jamii Drive system.
 * 
 * <p>This service allows authenticated users to delete their files using a soft delete
 * approach. Files are moved to trash rather than being permanently deleted, allowing
 * for potential recovery and maintaining data integrity.</p>
 * 
 * <p>Operation flow:</p>
 * <ol>
 *   <li>Extract authentication keys from request in {@link #validateCookie()}</li>
 *   <li>Validate user session and ensure user is active</li>
 *   <li>Locate file record in database using filename</li>
 *   <li>Verify file ownership and current status</li>
 *   <li>Update file status to "in trash" (soft delete)</li>
 *   <li>Return deletion confirmation response</li>
 * </ol>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Soft delete implementation (files moved to trash)</li>
 *   <li>Ownership verification for security</li>
 *   <li>Status validation to prevent duplicate operations</li>
 *   <li>Database consistency maintenance</li>
 * </ul>
 * 
 * <p>Error conditions:</p>
 * <ul>
 *   <li>Invalid or expired user session</li>
 *   <li>User not found or inactive</li>
 *   <li>File not found or already deleted</li>
 *   <li>File already in trash</li>
 *   <li>User does not own the file</li>
 * </ul>
 * 
 * <p>Security considerations:</p>
 * <ul>
 *   <li>Users can only delete their own files</li>
 *   <li>Soft delete prevents accidental permanent loss</li>
 *   <li>Status validation prevents unauthorized state changes</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see UserFileDeleteREQ
 * @see FileTableOwner
 */
@Service
public class UserFileDeleteOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private FileTableOwner fileTableOwner;
    
    protected UserFileDeleteREQ req = null;

    /**
     * Maps the incoming request to a {@link UserFileDeleteREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new UserFileDeleteREQ();
        req = (UserFileDeleteREQ) JamiiMapperUtils.mapObject(getRequest(), UserFileDeleteREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
    }

    /**
     * Processes the file deletion request using soft delete approach.
     * 
     * <p>Validates user ownership and file status before performing soft delete.
     * Files are moved to trash rather than permanently deleted to allow recovery.</p>
     * 
     * <p>Process steps:</p>
     * <ol>
     *   <li>Validate user exists and is active</li>
     *   <li>Locate file record using filename</li>
     *   <li>Verify file is not already in trash or deleted</li>
     *   <li>Update file status to "in trash"</li>
     *   <li>Save changes to database</li>
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

        UserFileDeleteREQ req = (UserFileDeleteREQ) JamiiMapperUtils.mapObject(getRequest(), UserFileDeleteREQ.class);

        this.userLogin.data = this.userLogin.fetchByUserKey(req.getUserKey(), UserLogin.ACTIVE_ON).orElse(null);
        if (this.userLogin.data == null) {
            jamiiDebug.warning("This user key does not exists : " + req.getUserKey());
            this.jamiiErrorsMessagesRESP.setUserFileDeleteOPS_NoMatchingUserKey();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        this.fileTableOwner.data = this.fileTableOwner.fetch(this.userLogin.data, req.getFileName()).orElse(null);
        if (this.fileTableOwner.data == null) {
            jamiiDebug.warning("This the file is in trash or has been deleted from the system: " + req.getFileName());
            this.jamiiErrorsMessagesRESP.setUserFileDeleteOPS_FileIsInTrash();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        ArrayList<Integer> checkAvailability = new ArrayList<>(Arrays.asList(FileTableOwner.ACTIVE_STATUS_IN_TRASH, FileTableOwner.ACTIVE_STATUS_DELETED));
        if (this.fileTableOwner.checkStatus(checkAvailability)) {
            jamiiDebug.warning("This the file is in trash or has been deleted from the system: " + req.getFileName());
            this.jamiiErrorsMessagesRESP.setUserFileDeleteOPS_FileIsInTrash();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        this.fileTableOwner.data.setStatus(FileTableOwner.ACTIVE_STATUS_IN_TRASH);
        this.fileTableOwner.save();

        setIsSuccessful(true);
    }

    /**
     * Generates the HTTP response for the file deletion operation.
     * 
     * <p>Returns a success response with deletion confirmation if the operation
     * completed successfully, otherwise delegates error handling to the parent class.</p>
     * 
     * @return ResponseEntity containing the deletion response or error details
     */
    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {
            UserFileDeleteRESP userFileDeleteRESP = new UserFileDeleteRESP();
            return new ResponseEntity<>(userFileDeleteRESP.getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
