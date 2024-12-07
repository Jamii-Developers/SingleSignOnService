package com.jamii.responses.userResponses.profileResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class EditUserDataRESP extends AbstractResponses {

    public EditUserDataRESP() {
        this.UI_SUBJECT = "Profile information Updated!";
        this.UI_MESSAGE = "Your profile has been updated successfully!" ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_TYPE_EDIT_USER_DATA;
    }
}
