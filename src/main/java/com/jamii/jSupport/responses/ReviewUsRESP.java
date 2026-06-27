package com.jamii.jSupport.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.abstractClasses.AbstractResponses;

public class ReviewUsRESP extends AbstractResponses {

    public ReviewUsRESP() {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "Thank you for sharing your comments";
        this.MSG_TYPE = JamiiConstants.RESPONSE_TYPE_REVIEW_US;
    }

}
