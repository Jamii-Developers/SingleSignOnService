package com.jamii.Utils;

import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JamiiCookieProcessor {

    public JamiiCookieProcessor( ) { }

    public JamiiCookieProcessor(String USER_KEY, String DEVICE_KEY, String USERNAME, String EMAIL_ADDRESS) {
        this.USER_KEY = USER_KEY;
        this.DEVICE_KEY = DEVICE_KEY;
        this.USERNAME = USERNAME;
        this.EMAIL_ADDRESS = EMAIL_ADDRESS;
    }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private DeviceInformationCONT deviceInformationCONT;

    private String USER_KEY;
    private String DEVICE_KEY;
    private String USERNAME;
    private String EMAIL_ADDRESS;

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

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getEMAIL_ADDRESS() {
        return EMAIL_ADDRESS;
    }

    public void setEMAIL_ADDRESS(String EMAIL_ADDRESS) {
        this.EMAIL_ADDRESS = EMAIL_ADDRESS;
    }

    public Boolean checkCookieIsValid( ){

        Optional<UserLoginTBL> user = this.userLoginCONT.fetch( getEMAIL_ADDRESS( ), getUSERNAME( ), getUSER_KEY( ), UserLoginTBL.ACTIVE_ON );

        if( user.isEmpty( ) ){
            return false;
        }

        Optional< DeviceInformationTBL > deviceData = this.deviceInformationCONT.fetch( user.get( ), getDEVICE_KEY( ), DeviceInformationTBL.ACTIVE_STATUS_ENABLED );

        if( deviceData.isEmpty( ) ){
            return false;
        }

        return true;
    }

}
