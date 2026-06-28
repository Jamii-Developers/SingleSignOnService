package com.jamii.jUser.services;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jUser.controller.UserData;
import com.jamii.jUser.controller.UserDataHistory;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jUser.requests.EditUserDataServicesREQ;
import com.jamii.jUser.responses.EditUserDataRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service for handling user profile data editing operations.
 * 
 * <p>This service allows authenticated users to update their profile information,
 * including personal details (name, address, contact info) and privacy settings.
 * The operation requires valid session authentication and maintains a history
 * of all changes for audit purposes.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Validate session cookie (device key, user key, session key)</li>
 *   <li>Verify user exists and is active</li>
 *   <li>Update user profile data in the database</li>
 *   <li>Create a history record of the changes</li>
 *   <li>Update privacy settings if provided</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found or inactive</li>
 *   <li>Database operation failures</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 */
@Service
public class EditUserDataOPS
        extends AbstractUserServicesOPS
{

    /** Repository for user login operations */
    @Autowired private UserLogin userLogin;
    
    /** Repository for user profile data operations */
    @Autowired private UserData userData;
    
    /** Repository for user data history tracking */
    @Autowired private UserDataHistory userDataHistory;

    /**
     * Validates the session cookie and extracts authentication keys from the request.
     * 
     * <p>This method extracts the device key, user key, and session key from the
     * request payload and delegates to the parent class for session validation.
     * 
     * @throws Exception if cookie validation fails or session is invalid
     */
    @Override
    public void validateCookie()
            throws Exception
    {
        EditUserDataServicesREQ req = (EditUserDataServicesREQ) JamiiMapperUtils.mapObject(getRequest(), EditUserDataServicesREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
        super.validateCookie();
    }

    /**
     * Processes the user data edit request.
     * 
     * <p>This method performs the following operations:
     * <ul>
     *   <li>Validates user exists and is active (reuses cached user from cookie validation)</li>
     *   <li>Updates user profile data in the database</li>
     *   <li>Creates a history record of the changes for audit trail</li>
     *   <li>Updates privacy settings if provided</li>
     * </ul>
     * 
     * @throws Exception if an error occurs during processing or user is not found
     */
    @Override
    public void processRequest()
            throws Exception
    {

        if (!getIsSuccessful()) {
            return;
        }

        EditUserDataServicesREQ req = (EditUserDataServicesREQ) JamiiMapperUtils.mapObject(getRequest(), EditUserDataServicesREQ.class);
        
        // Reuse validated user from cookie validation to avoid redundant database call
        UserLoginTBL userRecord = this.cookie.getValidatedUser();
        if (userRecord == null) {
            jamiiDebug.warning("User not found or inactive from cookie validation");
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_UserNotFound();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        try {
            // Adds the latest userData to the database
            this.userData.add(userRecord, req);
            jamiiDebug.info("User profile data updated for user: " + userRecord.getUsername());

            // Create a record of the new update for audit trail
            this.userDataHistory.copyUserData(this.userData.data);
            jamiiDebug.info("User data history record created");

            // Updates Privacy Settings if provided
            if (req.getPrivacy() != null) {
                userRecord.setPrivacy(req.getPrivacy());
                this.userLogin.add(userRecord);
                jamiiDebug.info("Privacy settings updated for user: " + userRecord.getUsername());
            }

            setIsSuccessful(true);
        } catch (Exception e) {
            jamiiDebug.error("Error updating user data: " + e.getMessage());
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_UserNotFound();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
        }
    }

    /**
     * Generates the HTTP response for the edit user data operation.
     * 
     * <p>Returns a success response with a confirmation message if the profile
     * was updated successfully, or an error response if validation failed.
     * 
     * @return ResponseEntity containing the response data with appropriate status code
     */
    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {
            EditUserDataRESP editUserDataRESP = new EditUserDataRESP();
            return new ResponseEntity<>(editUserDataRESP.getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
