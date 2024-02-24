package com.jamii.responses.social;

import com.jamii.Utils.JamiiConstants;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class AcceptFollowRequestRESP extends AbstractResponses {

    public AcceptFollowRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You're now being followed by " + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_ACCEPT_FOLLOW_REQUEST;
    }
}
