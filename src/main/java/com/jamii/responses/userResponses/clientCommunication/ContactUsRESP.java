package com.jamii.responses.userResponses.clientCommunication;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class ContactUsRESP extends AbstractResponses {

    public ContactUsRESP() {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "Thank you for sharing your comments";
        this.MSG_TYPE = JamiiConstants.RESPONSE_TYPE_CONTACT_US;
    }

}
