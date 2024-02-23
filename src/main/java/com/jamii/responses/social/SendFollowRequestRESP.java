package com.jamii.responses.social;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class SendFollowRequestRESP extends AbstractResponses {

    public SendFollowRequestRESP( ) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You're now following this user";
        this.MSG_TYPE = JamiiConstants.RESPONSE_SEND_FRIEND_REQUEST;
    }
}
