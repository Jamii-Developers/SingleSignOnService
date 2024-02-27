package com.jamii.responses.social;

import com.jamii.Utils.JamiiConstants;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class RemoveFollowRequestRESP extends AbstractResponses {

    public RemoveFollowRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully rejected a friend request from " + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_REMOVE_FOLLOW_REQUEST;
    }
}
