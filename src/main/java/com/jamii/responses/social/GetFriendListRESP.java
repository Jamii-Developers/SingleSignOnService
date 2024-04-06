package com.jamii.responses.social;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class GetFriendListRESP extends AbstractResponses {

    public GetFriendListRESP( ) {
        this.MSG_TYPE = JamiiConstants.RESPONSE_GET_FRIEND_LIST_REQUEST;
    }

    private String USER_KEY;
    private String USERNAME;
    private String FIRSTNAME;
    private String LASTNAME;

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

    public String getFIRSTNAME() {
        return FIRSTNAME;
    }

    public void setFIRSTNAME(String FIRSTNAME) {
        this.FIRSTNAME = FIRSTNAME;
    }

    public String getLASTNAME() {
        return LASTNAME;
    }

    public void setLASTNAME(String LASTNAME) {
        this.LASTNAME = LASTNAME;
    }
}
