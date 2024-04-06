package com.jamii.responses.activeDirectory;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class ReactivateUserRESP extends AbstractResponses {

    public ReactivateUserRESP( ) {
        this.UI_SUBJECT = "Account Reactivation Successful";
        this.UI_MESSAGE = "Your account has been reactivated successfully!";
        this.MSG_TYPE = JamiiConstants.RESPONSE_TYPE_EDIT_REACTIVATE;
    }
}
