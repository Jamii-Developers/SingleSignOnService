package com.jamii.jUser.services;

import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.ViewUserProfileServicesREQ;
import com.jamii.jSocial.responses.ViewUserProfileRESP;
import com.jamii.jUser.peer.UserData;
import com.jamii.jUser.peer.UserLogin;
import com.jamii.jUser.model.UserDataTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.utils.JamiiMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
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
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
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
    
    /** Request object containing target user information */
    protected ViewUserProfileServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link ViewUserProfileServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new ViewUserProfileServicesREQ();
        req = (ViewUserProfileServicesREQ) JamiiMapperUtils.mapObject(getRequest(), ViewUserProfileServicesREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
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

        // Request parameters are already mapped in setUserRequestData()

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

        // Store users in the peer fields for response generation
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

    /**
     * Fetches user profile with caching for improved performance.
     * 
     * <p>This method caches user profile data to reduce database queries
     * for frequently accessed profiles. The cache key is the user ID.</p>
     * 
     * @param user the user to fetch profile for
     * @return Optional containing UserDataTBL if found, empty otherwise
     */
    @Cacheable(value = "user-profiles", key = "#user.id")
    public Optional<UserDataTBL> fetchUserProfileWithCache(UserLoginTBL user) {
        return this.userData.fetch(user, UserData.CURRENT_STATUS_ON);
    }

    /**
     * Fetches user login information with caching.
     * 
     * <p>This method caches user login data to reduce database queries
     * for frequently accessed user information.</p>
     * 
     * @param userId the user ID to fetch login info for
     * @return Optional containing UserLoginTBL if found, empty otherwise
     */
    @Cacheable(value = "user-profiles", key = "'login:' + #userId")
    public Optional<UserLoginTBL> fetchUserLoginWithCache(Integer userId) {
        return this.userLogin.fetch(userId, UserLogin.ACTIVE_ON);
    }

    /**
     * Evicts user profile cache when user data is updated.
     * 
     * <p>This method should be called when a user updates their profile
     * to invalidate cached profile data.</p>
     * 
     * @param userId the user ID whose cache should be evicted
     */
    @CacheEvict(value = "user-profiles", key = "#userId")
    public void evictUserProfileCache(Integer userId) {
        // User profile cache eviction - method body can be empty
    }

    /**
     * Evicts user login cache when user account is updated.
     * 
     * <p>This method should be called when a user's account information
     * is updated (e.g., password change, status change).</p>
     * 
     * @param userId the user ID whose cache should be evicted
     */
    @CacheEvict(value = "user-profiles", key = "'login:' + #userId")
    public void evictUserLoginCache(Integer userId) {
        // User login cache eviction - method body can be empty
    }

    /**
     * Evicts all user-related cache entries for a user.
     * 
     * <p>This method should be called when a user's account undergoes
     * significant changes that affect all cached data.</p>
     * 
     * @param userId the user ID whose caches should be evicted
     */
    @CacheEvict(value = "user-profiles", allEntries = true)
    public void evictAllUserCaches(Integer userId) {
        // All user caches eviction - method body can be empty
    }
}
