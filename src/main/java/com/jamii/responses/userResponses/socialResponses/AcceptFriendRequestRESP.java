package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class AcceptFriendRequestRESP extends AbstractResponses {

    public AcceptFriendRequestRESP( UserLoginTBL user ) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You're now friends with @" + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_ACCEPT_FRIEND_REQUEST;
    }

}
