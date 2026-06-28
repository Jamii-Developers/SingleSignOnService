package com.jamii.jUser.services;

import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.ViewUserProfileServicesREQ;
import com.jamii.jSocial.responses.ViewUserProfileRESP;
import com.jamii.jUser.controller.UserData;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jUser.model.UserDataTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.utils.JamiiMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for viewing user profile information.
 * 
 * <p>This service allows authenticated users to view the profile information of another user.
 * The requesting user must have a valid session, and both the requesting user and the target
 * user must exist and be active in the system.</p>
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Validate session cookie (device key, user key, session key)</li>
 *   <li>Fetch and validate the requesting user exists and is active</li>
 *   <li>Fetch and validate the target user exists and is active</li>
 *   <li>Fetch the target user's profile data</li>
 *   <li>Return the profile information in the response</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>Requesting user not found or inactive</li>
 *   <li>Target user not found or inactive</li>
 *   <li>Target user profile data not found</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see ViewUserProfileServicesREQ
 * @see ViewUserProfileRESP
 */
@Service
public class ViewUserProfileOPS
        extends AbstractUserServicesOPS
{

    /** Repository for user login operations */
    @Autowired private UserLogin userLogin;
    
    /** Repository for user data operations */
    @Autowired private UserData userData;

    /**
     * Validates the session cookie and extracts authentication keys from the request.
     * 
     * <p>This method extracts the device key, user key, and session key from the
     * profile view request and delegates to the parent class for session validation.</p>
     * 
     * @throws Exception if cookie validation fails or session is invalid
     */
    @Override
    public void validateCookie()
            throws Exception
    {
        ViewUserProfileServicesREQ req = (ViewUserProfileServicesREQ) JamiiMapperUtils.mapObject(getRequest(), ViewUserProfileServicesREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
        super.validateCookie();
    }

    /**
     * Processes the user profile view request.
     * 
     * <p>This method performs the following operations:
     * <ul>
     *   <li>Validates the requesting user exists and is active</li>
     *   <li>Validates the target user exists and is active</li>
     *   <li>Fetches the target user's profile data</li>
     * </ul>
     * 
     * <p>If any validation fails, an error response is set and the operation is marked as failed.</p>
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

        ViewUserProfileServicesREQ req = (ViewUserProfileServicesREQ) JamiiMapperUtils.mapObject(getRequest(), ViewUserProfileServicesREQ.class);

        // Fetch and validate the requesting user
        Optional<UserLoginTBL> requestingUser = this.userLogin.fetchByUserKey(getUserKey(), UserLogin.ACTIVE_ON);
        
        // Fetch and validate the target user
        Optional<UserLoginTBL> targetUser = this.userLogin.fetchByUserKey(req.getTargetUserKey(), UserLogin.ACTIVE_ON);
        
        if (requestingUser.isEmpty() || targetUser.isEmpty()) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Store users in the controller fields for response generation
        this.userLogin.data = requestingUser.get();
        this.userLogin.otherUser = targetUser.get();

        // Fetch the target user's profile data
        Optional<UserDataTBL> userProfile = this.userData.fetch(this.userLogin.otherUser, UserData.CURRENT_STATUS_ON);

        this.userData.data = userProfile.get();
    }

    /**
     * Generates the HTTP response for the profile view operation.
     * 
     * <p>If the profile view was successful, returns a success response with the target
     * user's profile information. If validation failed, returns an error response.</p>
     * 
     * @return ResponseEntity containing the response data with appropriate status code
     */
    @Override
    public ResponseEntity<?> getResponse()
    {
        if (getIsSuccessful()) {
            return new ResponseEntity<>(new ViewUserProfileRESP(this.userLogin.otherUser, this.userData.data).getJSONRESP(), HttpStatus.OK);
        }
        return super.getResponse();
    }
}
