package com.jamii.jSocial.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractResponses;

public class AcceptFriendRequestRESP extends AbstractResponses {

    public AcceptFriendRequestRESP( UserLoginTBL user ) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You're now friends with @" + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_ACCEPT_FRIEND_REQUEST;
    }

}
