package com.jamii.Utils;

import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.UserCookiesCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserCookiesTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
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
    private UserLoginCONT userLoginCONT;
    @Autowired
    private DeviceInformationCONT deviceInformationCONT;
    @Autowired
    private UserCookiesCONT userCookiesCONT;

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
        Optional<UserLoginTBL> user = this.userLoginCONT.fetchByUserKey( getUSER_KEY( ), UserLoginTBL.ACTIVE_ON );
        if( user.isEmpty( ) ){
            return false;
        }

        // Check if the device has been connected before
        Optional< DeviceInformationTBL > deviceData = this.deviceInformationCONT.fetch( user.get( ), getDEVICE_KEY( ), DeviceInformationTBL.ACTIVE_STATUS_ENABLED );
        if( deviceData.isEmpty( ) ){
            return false;
        }

        // Check if session exists
        Optional<UserCookiesTBL> cookieData = this.userCookiesCONT.fetch( user.get( ), deviceData.get( ), getUSER_COOKIE( ) ,UserCookiesTBL.ACTIVE_STATUS_ENABLED );
        if( cookieData.isEmpty( ) ){
            return false;
        }

        // Check when was the last time the device was connected
        if( LocalDateTime.now( ).minusDays( 5 ).isAfter( deviceData.get().getLastconnected( ) ) ){
            deviceData.get( ).setActive( DeviceInformationTBL.ACTIVE_STATUS_DISABLED );
            this.deviceInformationCONT.update( deviceData.get( ) );
            return false;
        }

        if( LocalDateTime.now( ).isAfter( cookieData.get( ).getExpiredate( ) ) ){
            cookieData.get( ).setActive( UserCookiesTBL.ACTIVE_STATUS_DISABLED );
            this.userCookiesCONT.update( cookieData.get( ) );
            return false;
        }

        deviceData.get( ).setLastconnected( LocalDateTime.now( ) );
        this.deviceInformationCONT.update( deviceData.get( ) );

        return true;
    }
}
