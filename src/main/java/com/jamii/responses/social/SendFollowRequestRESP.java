package com.jamii.responses.social;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class SendFollowRequestRESP extends AbstractResponses {

    public SendFollowRequestRESP( int followRequestType ) {
        if( followRequestType == 0 ){
            this.UI_SUBJECT = "Success!";
            this.UI_MESSAGE = "You're now following this user";
            this.MSG_TYPE = JamiiConstants.RESPONSE_SEND_FOLLOW_REQUEST;
        }else if ( followRequestType == 1 ){
            this.UI_SUBJECT = "Success!";
            this.UI_MESSAGE = "You have requested to follow this user";
            this.MSG_TYPE = JamiiConstants.RESPONSE_SEND_FOLLOW_REQUEST;
        }

    }
}
