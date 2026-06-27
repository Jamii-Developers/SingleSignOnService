package com.jamii.utils;

import com.jamii.jUser.controller.DeviceInformation;
import com.jamii.jUser.controller.UserCookies;
import com.jamii.jUser.controller.UserLogin;
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
 * and session expiration. It integrates with the database controllers to verify
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
     * @return true if the cookie is valid, false otherwise
     */
    public Boolean checkCookieIsValid( ){

        // Check if user key exists in the system
        Optional<UserLoginTBL> user = this.userLogin.fetchByUserKey( getUSER_KEY( ), UserLogin.ACTIVE_ON );
        if( user.isEmpty( ) ){
            return false;
        }

        // Check if the device has been connected before
        Optional< DeviceInformationTBL > deviceData = this.deviceInformation.fetch( user.get( ), getDEVICE_KEY( ), DeviceInformation.ACTIVE_STATUS_ENABLED );
        if( deviceData.isEmpty( ) ){
            return false;
        }

        // Check if session exists
        Optional<UserCookiesTBL> cookieData = this.userCookies.fetch( user.get( ), deviceData.get( ), getUSER_COOKIE( ) , UserCookies.ACTIVE_STATUS_ENABLED );
        if( cookieData.isEmpty( ) ){
            return false;
        }

        // Check when was the last time the device was connected
        if( LocalDateTime.now( ).minusDays( 5 ).isAfter( deviceData.get().getLastconnected( ) ) ){
            deviceData.get( ).setActive( DeviceInformation.ACTIVE_STATUS_DISABLED );
            this.deviceInformation.update( deviceData.get( ) );
            return false;
        }

        if( LocalDateTime.now( ).isAfter( cookieData.get( ).getExpiredate( ) ) ){
            cookieData.get( ).setActive( UserCookies.ACTIVE_STATUS_DISABLED );
            this.userCookies.update( cookieData.get( ) );
            return false;
        }

        deviceData.get( ).setLastconnected( LocalDateTime.now( ) );
        this.deviceInformation.update( deviceData.get( ) );

        return true;
    }
}
