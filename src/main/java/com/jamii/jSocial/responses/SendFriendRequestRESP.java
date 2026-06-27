package com.jamii.jSocial.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractResponses;

public class SendFriendRequestRESP extends AbstractResponses {

    public SendFriendRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "Your friend request was sent successfully to @" + user.getUsername( );
        this.MSG_TYPE = JamiiConstants.RESPONSE_SEND_FRIEND_REQUEST;
    }
}
