package com.jamii.responses.clientCommunication;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class ContactUsRESP extends AbstractResponses {

    public ContactUsRESP() {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "Thank you for sharing your comments";
    }
    private String MSGTYPE = JamiiConstants.RESPONSE_TYPE_CONTACT_US;

    public String getMSGTYPE() {
        return MSGTYPE;
    }
}
