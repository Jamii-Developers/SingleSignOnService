package com.jamii.responses.social;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class SendFriendRequestRESP extends AbstractResponses {

    public SendFriendRequestRESP( ) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You're friend request was sent successfully";
        this.MSG_TYPE = JamiiConstants.RESPONSE_SEND_FRIEND_REQUEST;
    }
}
