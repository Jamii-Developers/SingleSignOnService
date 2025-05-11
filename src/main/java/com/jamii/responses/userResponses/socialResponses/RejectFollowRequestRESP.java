package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class RejectFollowRequestRESP extends AbstractResponses {

    public RejectFollowRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully rejected a follow request from @" + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_REJECT_FOLLOW_REQUEST;
    }
}
