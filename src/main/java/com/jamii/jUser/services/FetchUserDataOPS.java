package com.jamii.jUser.services;

import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.FetchUserDataREQ;
import com.jamii.jUser.controller.UserData;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jUser.model.UserDataTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jUser.responses.FetchUserProfileRESP;
import com.jamii.utils.JamiiMapperUtils;
import com.jamii.utils.JamiiStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service for fetching user profile data.
 * 
 * <p>This service allows authenticated users to retrieve their current profile
 * information, including personal details (name, address, contact info) and privacy settings.
 * The operation requires valid session authentication.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Verify user exists and is active</li>
 *   <li>Fetch current user profile data</li>
 *   <li>Return profile information with safe string handling</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found or inactive</li>
 *   <li>User profile data not found</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 */
@Service
public class FetchUserDataOPS
        extends AbstractUserServicesOPS
{

    /** Repository for user login operations */
    @Autowired private UserLogin userLogin;
    
    /** Repository for user profile data operations */
    @Autowired private UserData userData;

    /** Response object containing fetched user profile data */
    private FetchUserProfileRESP fetchUserProfileRESP;
    
    /** Request object containing user data fetch information */
    protected FetchUserDataREQ req = null;

    /**
     * Maps the incoming request to a {@link FetchUserDataREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new FetchUserDataREQ();
        req = (FetchUserDataREQ) JamiiMapperUtils.mapObject(getRequest(), FetchUserDataREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
    }

    /**
     * Processes the user data fetch request.
     * 
     * <p>This method performs the following operations:
     * <ul>
     *   <li>Validates user exists and is active (reuses cached user from cookie validation)</li>
     *   <li>Fetches current user profile data</li>
     *   <li>Populates response with profile information using safe string handling</li>
     *   <li>Includes privacy settings from user login record</li>
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

        // Request parameters are already mapped in setUserRequestData()

        // Reuse validated user from cookie validation to avoid redundant database call
        UserLoginTBL userRecord = this.cookie.getValidatedUser();
        if (userRecord == null) {
            jamiiDebug.warning("User not found or inactive from cookie validation");
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_UserNotFound();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        UserDataTBL userdata = userRecord.getUserData();
        
        fetchUserProfileRESP = new FetchUserProfileRESP();
        
        if ( userdata != null) {
            fetchUserProfileRESP.setFirstname(JamiiStringUtils.getSafeString(userdata.getFirstname()));
            fetchUserProfileRESP.setLastname(JamiiStringUtils.getSafeString(userdata.getLastname()));
            fetchUserProfileRESP.setMiddlename(JamiiStringUtils.getSafeString(userdata.getMiddlename()));
            fetchUserProfileRESP.setAddress1(JamiiStringUtils.getSafeString(userdata.getAddress1()));
            fetchUserProfileRESP.setAddress2(JamiiStringUtils.getSafeString(userdata.getAddress2()));
            fetchUserProfileRESP.setCity(JamiiStringUtils.getSafeString(userdata.getCity()));
            fetchUserProfileRESP.setState(JamiiStringUtils.getSafeString(userdata.getState()));
            fetchUserProfileRESP.setProvince(JamiiStringUtils.getSafeString(userdata.getProvince()));
            fetchUserProfileRESP.setCountry(JamiiStringUtils.getSafeString(userdata.getCountry()));
            fetchUserProfileRESP.setZipcode(JamiiStringUtils.getSafeString(userdata.getZipcode()));
            fetchUserProfileRESP.setPrivacy(userRecord.getPrivacy());
            
            jamiiDebug.info("User profile data fetched successfully for user: " + userRecord.getUsername());
        }
        else {
            jamiiDebug.warning("No profile data found for user: " + userRecord.getUsername());
            this.jamiiErrorsMessagesRESP.setFetchUserData_NoData();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
        }
    }

    /**
     * Generates the HTTP response for the fetch user data operation.
     * 
     * <p>Returns a success response with the user profile data if the fetch
     * was successful, or an error response if validation failed.
     * 
     * @return ResponseEntity containing the response data with appropriate status code
     */
    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {
            return new ResponseEntity<>(this.fetchUserProfileRESP.getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
