package com.jamii.jPublic.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.abstractClasses.AbstractResponses;

public class ReactivateUserRESP
        extends AbstractResponses
{

    public ReactivateUserRESP()
    {
        this.UI_SUBJECT = "Account Reactivation Successful";
        this.UI_MESSAGE = "Your account has been reactivated successfully!";
        this.MSG_TYPE = JamiiConstants.RESPONSE_TYPE_EDIT_REACTIVATE;
    }
}
