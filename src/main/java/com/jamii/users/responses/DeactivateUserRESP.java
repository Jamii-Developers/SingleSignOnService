package com.jamii.users.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.abstractClasses.AbstractResponses;

public class DeactivateUserRESP extends AbstractResponses {

    public DeactivateUserRESP( ) {
        this.UI_SUBJECT = "Account Deactivation Successful!";
        this.UI_MESSAGE = "Your account has been deactivated!";
        this.MSG_TYPE = JamiiConstants.RESPONSE_TYPE_EDIT_DEACTIVATE;
    }
}
