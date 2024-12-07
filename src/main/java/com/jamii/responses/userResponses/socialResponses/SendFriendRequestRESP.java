package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class SendFriendRequestRESP extends AbstractResponses {

    public SendFriendRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You're friend request was sent successfully to " + user.getUsername( );
        this.MSG_TYPE = JamiiConstants.RESPONSE_SEND_FRIEND_REQUEST;
    }
}
