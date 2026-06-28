package com.jamii.jUser.services;

import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jUser.controller.DeviceInformation;
import com.jamii.jUser.controller.UserCookies;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jUser.model.DeviceInformationTBL;
import com.jamii.jUser.model.UserCookiesTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jUser.requests.UserLogoffREQ;
import com.jamii.jUser.responses.UserLogoffRESP;
import com.jamii.utils.JamiiMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service for handling user logoff operations.
 * 
 * <p>This service allows authenticated users to log off from a specific device
 * by deactivating their session cookie and device information. This ensures that
 * the session cannot be used for subsequent requests.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Validate session cookie (device key, user key, session key)</li>
 *   <li>Fetch and validate the user account</li>
 *   <li>Fetch and validate the device information</li>
 *   <li>Deactivate the device (set to DISABLED status)</li>
 *   <li>Fetch and validate the session cookie</li>
 *   <li>Deactivate the session cookie (set to DISABLED status)</li>
 *   <li>Return success response</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found or inactive</li>
 *   <li>Device not found or already deactivated</li>
 *   <li>Session cookie not found or already deactivated</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see UserLogoffREQ
 * @see UserLogoffRESP
 */
@Service
public class UserLogoffOPS
        extends AbstractUserServicesOPS
{

    /** Repository for user login operations */
    @Autowired private UserLogin userLogin;
    
    /** Repository for device information operations */
    @Autowired private DeviceInformation deviceInformation;
    
    /** Repository for user cookie operations */
    @Autowired private UserCookies userCookies;

    /**
     * Validates the session cookie and extracts authentication keys from the request.
     * 
     * <p>This method extracts the device key, user key, and session key from the
     * logoff request and delegates to the parent class for session validation.
     * 
     * @throws Exception if cookie validation fails or session is invalid
     */
    @Override
    public void validateCookie()
            throws Exception
    {
        UserLogoffREQ req = (UserLogoffREQ) JamiiMapperUtils.mapObject(getRequest(), UserLogoffREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
        super.validateCookie();
    }

    /**
     * Processes the user logoff request.
     * 
     * <p>This method performs the following operations:
     * <ul>
     *   <li>Reuses validated user, device, and cookie entities from cookie validation</li>
     *   <li>Deactivates the device (set to DISABLED status)</li>
     *   <li>Deactivates the session cookie (set to DISABLED status)</li>
     * </ul>
     * 
     * <p>Both the device and session cookie are deactivated to ensure complete
     * session termination. If either operation fails, the logoff is considered failed.</p>
     * 
     * <p>This method reuses entities fetched during cookie validation to avoid redundant database queries.</p>
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

        // Reuse validated entities from cookie validation to avoid redundant database calls
        UserLoginTBL user = this.cookie.getValidatedUser();
        DeviceInformationTBL device = this.cookie.getValidatedDevice();
        UserCookiesTBL cookie = this.cookie.getValidatedCookie();

        // Validate that all required entities were cached from cookie validation
        if (user == null || device == null || cookie == null) {
            this.jamiiErrorsMessagesRESP.setLoginError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Deactivate the device
        device.setActive(DeviceInformation.ACTIVE_STATUS_DISABLED);
        this.deviceInformation.save(device);

        // Deactivate the session cookie
        cookie.setActive(UserCookies.ACTIVE_STATUS_DISABLED);
        this.userCookies.save(cookie);
    }

    /**
     * Generates the HTTP response for the logoff operation.
     * 
     * <p>If the logoff was successful, returns a success response with a confirmation message.
     * If validation failed, returns an error response.
     * 
     * @return ResponseEntity containing the response data with appropriate status code
     */
    @Override
    public ResponseEntity<?> getResponse()
    {
        if (getIsSuccessful()) {
            UserLogoffRESP userLogoffRESP = new UserLogoffRESP();
            return new ResponseEntity<>(userLogoffRESP.getJSONRESP(), HttpStatus.OK);
        }
        return super.getResponse();
    }
}
