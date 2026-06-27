package com.jamii.jSocial.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractResponses;

public class RejectFollowRequestRESP extends AbstractResponses {

    public RejectFollowRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully rejected a follow request from @" + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_REJECT_FOLLOW_REQUEST;
    }
}
