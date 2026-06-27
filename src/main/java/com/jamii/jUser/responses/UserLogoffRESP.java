package com.jamii.jUser.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.abstractClasses.AbstractResponses;

public class UserLogoffRESP extends AbstractResponses {

    public UserLogoffRESP() {
        this.UI_SUBJECT = "Success";
        this.UI_MESSAGE = "You have logged off Successfully";
        this.MSG_TYPE = JamiiConstants.RESPONSE_LOGOFF;
    }
}
