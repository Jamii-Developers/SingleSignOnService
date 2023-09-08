package com.jamii.responses;

import com.jamii.Utils.JamiiConstants;

public class CreateNewUserRESP extends AbstractResponses{

    private String MSGTYPE = JamiiConstants.RESPONSE_TYPE_CREATE_NEW_USER;
    private String USER_KEY;
    private String USERNAME;
    private String EMAIL_ADDRESS;
    private String DATE_CREATED;

    public String getMSGTYPE() {
        return MSGTYPE;
    }

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
}
