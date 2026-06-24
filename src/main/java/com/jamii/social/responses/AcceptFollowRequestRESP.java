package com.jamii.social.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.users.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractResponses;

public class AcceptFollowRequestRESP extends AbstractResponses {

    public AcceptFollowRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You're now being followed by " + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_ACCEPT_FOLLOW_REQUEST;
    }
}
