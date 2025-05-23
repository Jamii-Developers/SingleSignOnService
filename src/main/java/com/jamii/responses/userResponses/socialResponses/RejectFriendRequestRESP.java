package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class RejectFriendRequestRESP extends AbstractResponses {

    public RejectFriendRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully rejected a friend request from @" + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_REJECT_FRIEND_REQUEST;
    }
}
