package com.jamii.administrative.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.abstractClasses.AbstractResponses;

public class ContactSupportRESP extends AbstractResponses {

    public ContactSupportRESP() {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "Thank you for reaching out to support expect a response with 24-48 hrs";
        this.MSG_TYPE = JamiiConstants.RESPONSE_CONTACT_SUPPORT;
    }

}
