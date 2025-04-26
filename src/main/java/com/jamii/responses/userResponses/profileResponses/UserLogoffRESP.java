package com.jamii.responses.userResponses.profileResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class UserLogoffRESP extends AbstractResponses {

    public UserLogoffRESP() {
        this.UI_SUBJECT = "Success";
        this.UI_MESSAGE = "You have logged off Successfully";
        this.MSG_TYPE = JamiiConstants.RESPONSE_LOGOFF;
    }
}
