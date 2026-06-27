package com.jamii.jUser.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.abstractClasses.AbstractResponses;

public class EditUserDataRESP extends AbstractResponses {

    public EditUserDataRESP() {
        this.UI_SUBJECT = "Profile information Updated!";
        this.UI_MESSAGE = "Your profile has been updated successfully!" ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_TYPE_EDIT_USER_DATA;
    }
}
