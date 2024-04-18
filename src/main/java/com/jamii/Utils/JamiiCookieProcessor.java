package com.jamii.Utils;

import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class JamiiCookieProcessor {

    public JamiiCookieProcessor( ) { }

    public JamiiCookieProcessor( String USER_KEY, String DEVICE_KEY ) {
        this.USER_KEY = USER_KEY;
        this.DEVICE_KEY = DEVICE_KEY;
    }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private DeviceInformationCONT deviceInformationCONT;

    private String USER_KEY;
    private String DEVICE_KEY;

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

        // Check when was the last time the device was connected
        if( deviceData.get( ).getLastconnected( ).compareTo( LocalDateTime.now( ) ) > 30 ){
            deviceData.get( ).setActive( DeviceInformationTBL.ACTIVE_STATUS_DISABLED );
            this.deviceInformationCONT.update( deviceData.get( ) );
            return false;
        }

        deviceData.get( ).setLastconnected( LocalDateTime.now( ) );
        this.deviceInformationCONT.update( deviceData.get( ) );

        return true;
    }

}
