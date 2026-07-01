package com.jamii.utils;

import com.jamii.jUser.peer.DeviceInformation;
import com.jamii.jUser.peer.UserCookies;
import com.jamii.jUser.peer.UserLogin;
import com.jamii.jUser.model.DeviceInformationTBL;
import com.jamii.jUser.model.UserCookiesTBL;
import com.jamii.jUser.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Utility class for processing and validating user cookies and sessions.
 * 
 * <p>This class handles cookie validation by checking user keys, device information,
 * and session expiration. It integrates with the database peer to verify
 * the validity of user sessions.</p>
 */
@Component
public class JamiiCookieProcessor {

    /**
     * Default constructor for Spring dependency injection.
     */
    public JamiiCookieProcessor( ) { }

    /**
     * Constructor with parameters for manual initialization.
     * @param USER_KEY the user key to set
     * @param DEVICE_KEY the device key to set
     * @param USER_COOKIE the user cookie to set
     */
    public JamiiCookieProcessor( String USER_KEY, String DEVICE_KEY, String USER_COOKIE ) {
        this.USER_KEY = USER_KEY;
        this.DEVICE_KEY = DEVICE_KEY;
        this.USER_COOKIE = USER_COOKIE;
    }

    @Autowired
    private UserLogin userLogin;
    
    @Autowired
    private DeviceInformation deviceInformation;
    
    @Autowired
    private UserCookies userCookies;

    private String USER_KEY;
    private String DEVICE_KEY;
    private String USER_COOKIE;
    
    /** Cached validated user from the last cookie check */
    private UserLoginTBL validatedUser;
    
    /** Cached validated device from the last cookie check */
    private DeviceInformationTBL validatedDevice;
    
    /** Cached validated cookie from the last cookie check */
    private UserCookiesTBL validatedCookie;
    
    /** Cache key for validation results to avoid redundant database calls */
    private String lastValidationCacheKey;
    
    /** Cached validation result to avoid redundant processing */
    private Boolean lastValidationResult;
    
    /** Timestamp when last validation was performed */
    private LocalDateTime lastValidationTime;

    /**
     * Gets the user key.
     * @return the user key
     */
    public String getUSER_KEY() {
        return USER_KEY;
    }

    /**
     * Sets the user key.
     * @param USER_KEY the user key to set
     */
    public void setUSER_KEY(String USER_KEY) {
        this.USER_KEY = USER_KEY;
    }

    /**
     * Gets the device key.
     * @return the device key
     */
    public String getDEVICE_KEY() {
        return DEVICE_KEY;
    }

    /**
     * Sets the device key.
     * @param DEVICE_KEY the device key to set
     */
    public void setDEVICE_KEY(String DEVICE_KEY) {
        this.DEVICE_KEY = DEVICE_KEY;
    }

    /**
     * Gets the user cookie.
     * @return the user cookie
     */
    public String getUSER_COOKIE() {
        return USER_COOKIE;
    }

    /**
     * Sets the user cookie.
     * @param USER_COOKIE the user cookie to set
     */
    public void setUSER_COOKIE(String USER_COOKIE) {
        this.USER_COOKIE = USER_COOKIE;
    }

    /**
     * Gets the validated user from the last cookie check.
     * @return the validated user, or null if no valid cookie check was performed
     */
    public UserLoginTBL getValidatedUser() {
        return validatedUser;
    }

    /**
     * Gets the validated device from the last cookie check.
     * @return the validated device, or null if no valid cookie check was performed
     */
    public DeviceInformationTBL getValidatedDevice() {
        return validatedDevice;
    }

    /**
     * Gets the validated cookie from the last cookie check.
     * @return the validated cookie, or null if no valid cookie check was performed
     */
    public UserCookiesTBL getValidatedCookie() {
        return validatedCookie;
    }

    /**
     * Validates the cookie by checking user key, device information, and session expiration.
     * 
     * <p>This method performs the following validations:</p>
     * <ul>
     *     <li>Checks if user key exists in the system</li>
     *     <li>Checks if the device has been connected before</li>
     *     <li>Checks if the session exists</li>
     *     <li>Checks if the device was connected within the last 5 days</li>
     *     <li>Checks if the cookie has not expired</li>
     * </ul>
     * <p>On successful validation, the user, device, and cookie entities are cached
     * and can be retrieved via getter methods to avoid redundant database queries.</p>
     * 
     * <p>Performance optimization: This method uses intelligent caching to avoid
     * redundant database calls. If the same cookie is validated multiple times
     * within a short period, the cached result is returned.</p>
     * 
     * @return true if the cookie is valid, false otherwise
     */
    public Boolean checkCookieIsValid( ){

        // Create cache key from current validation parameters
        String currentCacheKey = getUSER_KEY() + "|" + getDEVICE_KEY() + "|" + getUSER_COOKIE();
        LocalDateTime now = LocalDateTime.now();
        
        // Check if we have a recent cached result (within 30 seconds)
        if (lastValidationCacheKey != null && 
            lastValidationCacheKey.equals(currentCacheKey) && 
            lastValidationTime != null && 
            lastValidationTime.plusSeconds(30).isAfter(now)) {
            
            // Return cached result without database calls
            return lastValidationResult;
        }

        // Reset cached entities
        this.validatedUser = null;
        this.validatedDevice = null;
        this.validatedCookie = null;

        // Check if user key exists in the system
        Optional<UserLoginTBL> user = this.userLogin.fetchByUserKey( getUSER_KEY( ), UserLogin.ACTIVE_ON );
        if( user.isEmpty( ) ){
            cacheValidationResult(currentCacheKey, now, false);
            return false;
        }
        this.validatedUser = user.get();

        // Check if the device has been connected before
        Optional< DeviceInformationTBL > deviceData = user.get().getDeviceInformation().stream().filter( device -> device.getDevicekey().equals( getDEVICE_KEY( ) ) &&  device.getActive().equals( DeviceInformation.ACTIVE_STATUS_ENABLED  ) ).findFirst();
        if( deviceData.isEmpty( ) ){
            cacheValidationResult(currentCacheKey, now, false);
            return false;
        }
        this.validatedDevice = deviceData.get();

        // Check if session exists
        Optional<UserCookiesTBL> cookieData = user.get().getUserCookies().stream().filter( cookie -> cookie.getSessionkey().equals( getUSER_COOKIE( ) ) && cookie.isActive() == UserCookies.ACTIVE_STATUS_ENABLED ).findFirst();
        if( cookieData.isEmpty( ) ){
            cacheValidationResult(currentCacheKey, now, false);
            return false;
        }
        this.validatedCookie = cookieData.get();

        // Check when was the last time the device was connected
        if( now.minusDays( 5 ).isAfter( deviceData.get().getLastconnected( ) ) ){
            deviceData.get( ).setActive( DeviceInformation.ACTIVE_STATUS_DISABLED );
            this.deviceInformation.save( deviceData.get( ) );
            cacheValidationResult(currentCacheKey, now, false);
            return false;
        }

        if( now.isAfter( cookieData.get( ).getExpiredate( ) ) ){
            cookieData.get( ).setActive( UserCookies.ACTIVE_STATUS_DISABLED );
            this.userCookies.save( cookieData.get( ) );
            cacheValidationResult(currentCacheKey, now, false);
            return false;
        }

        // Update last connected time and save
        deviceData.get( ).setLastconnected( now );
        this.deviceInformation.save( deviceData.get( ) );

        cacheValidationResult(currentCacheKey, now, true);
        return true;
    }
    
    /**
     * Caches the validation result to avoid redundant database calls.
     * 
     * @param cacheKey the cache key for this validation
     * @param validationTime the time when validation was performed
     * @param result the validation result
     */
    private void cacheValidationResult(String cacheKey, LocalDateTime validationTime, Boolean result) {
        this.lastValidationCacheKey = cacheKey;
        this.lastValidationTime = validationTime;
        this.lastValidationResult = result;
    }
    
    /**
     * Clears all cached validation data.
     * 
     * <p>This should be called when user credentials change or when
     * forcing a fresh validation is required.</p>
     */
    public void clearValidationCache() {
        this.lastValidationCacheKey = null;
        this.lastValidationTime = null;
        this.lastValidationResult = null;
        this.validatedUser = null;
        this.validatedDevice = null;
        this.validatedCookie = null;
    }
}
