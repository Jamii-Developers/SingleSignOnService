package com.jamii.responses.userResponses.profileResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class DeactivateUserRESP extends AbstractResponses {

    public DeactivateUserRESP( ) {
        this.UI_SUBJECT = "Account Deactivation Successful!";
        this.UI_MESSAGE = "Your account has been deactivated!";
        this.MSG_TYPE = JamiiConstants.RESPONSE_TYPE_EDIT_DEACTIVATE;
    }
}
