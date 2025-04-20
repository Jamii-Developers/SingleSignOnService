package com.jamii.responses.userResponses.clientCommunication;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class ContactSupportRESP extends AbstractResponses {

    public ContactSupportRESP() {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "Thank you for reaching out to support expect a response with 24-48 hrs";
        this.MSG_TYPE = JamiiConstants.RESPONSE_CONTACT_SUPPORT;
    }

}
