package com.jamii.jUser.services;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jUser.requests.DeactivateUserREQ;
import com.jamii.jUser.responses.DeactivateUserRESP;
import com.jamii.jPublic.services.ReactivateUserOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service for handling user account deactivation operations.
 * 
 * <p>This service allows authenticated users to deactivate their own accounts.
 * The operation requires valid session authentication and password verification
 * to prevent unauthorized deactivation.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Validate session cookie (device key, user key, session key)</li>
 *   <li>Verify user exists and is active</li>
 *   <li>Validate password matches current password</li>
 *   <li>Deactivate the user account</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found or already inactive</li>
 *   <li>Password doesn't match</li>
 * </ul>
 * 
 * <p>Note: Deactivation is a reversible action. Users can reactivate their accounts
 * through the reactivation service.
 * 
 * @see AbstractUserServicesOPS
 * @see ReactivateUserOPS
 */
@Service
public class DeactivateUserOPS
        extends AbstractUserServicesOPS
{

    /** Repository for user login operations */
    @Autowired private UserLogin userLogin;

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
        DeactivateUserREQ req = (DeactivateUserREQ) JamiiMapperUtils.mapObject(getRequest(), DeactivateUserREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
        super.validateCookie();
    }

    /**
     * Processes the user deactivation request.
     * 
     * <p>This method performs the following validations:
     * <ul>
     *   <li>User exists and is currently active</li>
 *   <li>Password matches current password</li>
 * </ul>
 * 
     * <p>If all validations pass, the user account is deactivated.
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

        DeactivateUserREQ req = (DeactivateUserREQ) JamiiMapperUtils.mapObject(getRequest(), DeactivateUserREQ.class);

        // Validate request parameters
        if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
            jamiiDebug.warning("Password is required for deactivation");
            this.jamiiErrorsMessagesRESP.setDeactivateUser_PasswordsNotMatching();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Reuse validated user from cookie validation to avoid redundant database call
        UserLoginTBL user = this.cookie.getValidatedUser();
        if (user == null) {
            jamiiDebug.warning("No validated user from cookie validation");
            this.jamiiErrorsMessagesRESP.setDeactivateUser_UsernameOrEmailAddressDoesNotExist();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Verify email and username match the authenticated user
        if (!user.getEmailaddress().equals(req.getEmailaddress()) || !user.getUsername().equals(req.getUsername())) {
            jamiiDebug.warning("Email or username does not match authenticated user: " + req.getUsername());
            this.jamiiErrorsMessagesRESP.setDeactivateUser_UsernameOrEmailAddressDoesNotExist();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Check if the password is valid
        if (!userLogin.isPasswordValid(req.getPassword(), user)) {
            jamiiDebug.warning("Password is incorrect for user: " + req.getUsername());
            this.jamiiErrorsMessagesRESP.setDeactivateUser_PasswordsNotMatching();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Deactivate user
        userLogin.deactivateUser(user);
        jamiiDebug.info("User account deactivated successfully: " + req.getUsername());
    }

    /**
     * Generates the HTTP response for the deactivation operation.
     * 
     * <p>Returns a success response with a confirmation message if the account
     * was deactivated successfully, or an error response if validation failed.
     * 
     * @return ResponseEntity containing the response data with appropriate status code
     */
    @Override
    public ResponseEntity<?> getResponse()
    {
        if (getIsSuccessful()) {
            DeactivateUserRESP deactivateUserRESP = new DeactivateUserRESP();
            return new ResponseEntity<>(deactivateUserRESP.getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
