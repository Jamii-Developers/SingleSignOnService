package com.jamii.responses.activeDirectory.FunctionRESP;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class UserLogoffRESP extends AbstractResponses {

    public UserLogoffRESP() {
        UI_SUBJECT = "Success";
        UI_MESSAGE = "You have logged off Successfully";
        MSG_TYPE = JamiiConstants.RESPONSE_LOGOFF;
    }
}
