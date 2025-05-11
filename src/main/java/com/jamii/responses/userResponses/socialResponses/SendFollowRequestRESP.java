package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class SendFollowRequestRESP extends AbstractResponses {

    public void init(int followRequestType, UserLoginTBL user ){
        if( followRequestType == 0 ){
            this.UI_SUBJECT = "Success!";
            this.UI_MESSAGE = "You have requested to follow " + user.getUsername( ) ;
            this.MSG_TYPE = JamiiConstants.RESPONSE_SEND_FOLLOW_REQUEST;
        }else if ( followRequestType == 1 ){
            this.UI_SUBJECT = "Success!";
            this.UI_MESSAGE = "You're now following " + user.getUsername( );
            this.MSG_TYPE = JamiiConstants.RESPONSE_SEND_FOLLOW_REQUEST;

        }
    }
}
