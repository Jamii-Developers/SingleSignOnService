package com.jamii.responses.activeDirectory.FunctionRESP;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class UserLoginRESP extends AbstractResponses {

    public UserLoginRESP( ) {
        this.UI_SUBJECT = "Login Successful!";
        this.UI_MESSAGE = "Your login was successful!";
        this.MSG_TYPE   = JamiiConstants.RESPONSE_TYPE_USERLOGIN;
    }

    private String USER_KEY;
    private String USERNAME;
    private String DEVICE_KEY;
    private String SESSION_KEY;
    private String EMAIL_ADDRESS;
    private String DATE_CREATED;
    private String EXPIRY_DATE;

    public String getUSER_KEY() {
        return USER_KEY;
    }

    public void setUSER_KEY(String USER_KEY) {
        this.USER_KEY = USER_KEY;
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

    public String getDATE_CREATED() {
        return DATE_CREATED;
    }

    public void setDATE_CREATED(String DATE_CREATED) {
        this.DATE_CREATED = DATE_CREATED;
    }

    public String getDEVICE_KEY() {
        return DEVICE_KEY;
    }

    public void setDEVICE_KEY(String DEVICE_KEY) {
        this.DEVICE_KEY = DEVICE_KEY;
    }

    public String getSESSION_KEY() {
        return SESSION_KEY;
    }

    public void setSESSION_KEY(String SESSION_KEY) {
        this.SESSION_KEY = SESSION_KEY;
    }

    public String getEXPIRY_DATE() {
        return EXPIRY_DATE;
    }

    public void setEXPIRY_DATE(String EXPIRY_DATE) {
        this.EXPIRY_DATE = EXPIRY_DATE;
    }
}
