package com.jamii.jUser.services;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.utils.JamiiStringUtils;
import com.jamii.utils.JamiiUserPasswordEncryptTool;
import com.jamii.jUser.controller.PasswordHashRecords;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jUser.requests.ChangePasswordServicesREQ;
import com.jamii.jUser.responses.ChangePasswordRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service for handling password change operations for authenticated users.
 * 
 * <p>This service validates the user's session, verifies their current password,
 * ensures the new password hasn't been used recently (last 10 passwords), and updates
 * the password in the system.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Verify user exists by email and username</li>
 *   <li>Validate old password matches current password</li>
 *   <li>Check new password is not in last 10 password history</li>
 *   <li>Update password and record in password history</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found</li>
 *   <li>Old password doesn't match</li>
 *   <li>New password matches one of last 10 passwords</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see PasswordHashRecords
 */
@Service
public class ChangePasswordOPS
        extends AbstractUserServicesOPS
{

    /** Repository for managing password hash history records */
    @Autowired private PasswordHashRecords passwordHashRecords;
    
    /** Repository for user login operations */
    @Autowired private UserLogin userLogin;
    
    /** Request object containing password change data */
    protected ChangePasswordServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link ChangePasswordServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new ChangePasswordServicesREQ();
        req = (ChangePasswordServicesREQ) JamiiMapperUtils.mapObject(getRequest(), ChangePasswordServicesREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
    }

    /**
     * Processes the password change request.
     * 
     * <p>This method performs the following validations:
     * <ul>
     *   <li>User exists and is active (reuses cached user from cookie validation)</li>
     *   <li>Email and username match the authenticated user</li>
     *   <li>Old password matches current password</li>
     *   <li>New password is not in the last 10 password history</li>
     * </ul>
     * 
     * <p>If all validations pass, the password is updated and recorded in history.
     * 
     * @throws Exception if an error occurs during processing
     */
    @Override
    public void processRequest()
            throws Exception
    {

        if (!getIsSuccessful()) {
            return;
        }

        // Request parameters are already mapped in setUserRequestData()

        // Validate request parameters
        if (req.getOld_password() == null || req.getOld_password().trim().isEmpty()) {
            jamiiDebug.warning("Old password is required");
            this.jamiiErrorsMessagesRESP.setPasswordChange_PasswordsNotMatching();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        if (req.getNew_password() == null || req.getNew_password().trim().isEmpty()) {
            jamiiDebug.warning("New password is required");
            this.jamiiErrorsMessagesRESP.setPasswordChange_PasswordsNotMatching();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Reuse validated user from cookie validation to avoid redundant database call
        UserLoginTBL user = this.cookie.getValidatedUser();
        if (user == null) {
            jamiiDebug.warning("No validated user from cookie validation");
            this.jamiiErrorsMessagesRESP.setPasswordChange_UsernameOrEmailAddressDoesNotExist();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Verify email and username match the authenticated user
        if (!user.getEmailaddress().equals(req.getEmailaddress()) || !user.getUsername().equals(req.getUsername())) {
            jamiiDebug.warning("Email or username does not match authenticated user: " + req.getUsername());
            this.jamiiErrorsMessagesRESP.setPasswordChange_UsernameOrEmailAddressDoesNotExist();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Check if the old password matches the current saved password
        String encryptedOldPassword = JamiiUserPasswordEncryptTool.encryptPassword(req.getOld_password());
        if (!JamiiStringUtils.equals(encryptedOldPassword, user.getPasswordsalt())) {
            jamiiDebug.warning("Old password doesn't match current password for user: " + req.getUsername());
            this.jamiiErrorsMessagesRESP.setPasswordChange_PasswordsNotMatching();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Encrypt the new password
        String encryptedNewPassword = JamiiUserPasswordEncryptTool.encryptPassword(req.getNew_password());

        // Temporarily set the new password on the user object to check against history
        String originalPasswordSalt = user.getPasswordsalt();
        user.setPasswordsalt(encryptedNewPassword);
        
        // Check if the new password matches the last 10 passwords the user used
        if (passwordHashRecords.isPasswordInLastTenRecords(user)) {
            jamiiDebug.warning("New password matches one of the last 10 passwords used by user: " + req.getUsername());
            this.jamiiErrorsMessagesRESP.setPasswordChange_PasswordMatchesLastTen();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            // Restore original password before returning
            user.setPasswordsalt(originalPasswordSalt);
            return;
        }

        // Update the password in the database (password is already set)
        userLogin.update(user);
        
        // Record the new password in history
        passwordHashRecords.addUserNewPasswordRecord(user);
        
        jamiiDebug.info("Password changed successfully for user: " + req.getUsername());
    }

    /**
     * Generates the HTTP response for the password change operation.
     * 
     * <p>Returns a success response with a confirmation message if the password
     * was changed successfully, or an error response if validation failed.
     * 
     * @return ResponseEntity containing the response data with appropriate status code
     */
    @Override
    public ResponseEntity<?> getResponse()
    {

        if (this.JamiiError == null || this.JamiiError.isEmpty()) {
            ChangePasswordRESP changePasswordRESP = new ChangePasswordRESP();
            return new ResponseEntity<>(changePasswordRESP.getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
