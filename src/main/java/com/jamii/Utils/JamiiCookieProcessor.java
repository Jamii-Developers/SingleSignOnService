package com.jamii.Utils;

import com.jamii.databaseconfig.controllers.DeviceInformation;
import com.jamii.databaseconfig.controllers.UserCookies;
import com.jamii.databaseconfig.controllers.UserLogin;
import com.jamii.databaseconfig.model.DeviceInformationTBL;
import com.jamii.databaseconfig.model.UserCookiesTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class JamiiCookieProcessor {

    public JamiiCookieProcessor( ) { }

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

    public String getUSER_KEY() {
        return USER_KEY;
    }

    public void setUSER_KEY(String USER_KEY) {
        this.USER_KEY = USER_KEY;
    }

    public String getDEVICE_KEY() {
        return DEVICE_KEY;
    }

    public void setDEVICE_KEY(String DEVICE_KEY) {
        this.DEVICE_KEY = DEVICE_KEY;
    }

    public String getUSER_COOKIE() {
        return USER_COOKIE;
    }

    public void setUSER_COOKIE(String USER_COOKIE) {
        this.USER_COOKIE = USER_COOKIE;
    }

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
