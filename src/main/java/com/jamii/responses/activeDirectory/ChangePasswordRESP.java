package com.jamii.responses.activeDirectory;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class ChangePasswordRESP extends AbstractResponses {

    public ChangePasswordRESP ( ){
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "Your password change was successful";
        this.MSG_TYPE = JamiiConstants.RESPONSE_TYPE_CHANGE_PASSWORD;
    }
}
