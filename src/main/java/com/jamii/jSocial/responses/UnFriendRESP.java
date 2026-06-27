package com.jamii.jSocial.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractResponses;

public class UnFriendRESP extends AbstractResponses {

    public UnFriendRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully removed @" + user.getUsername( ) + " as a friend." ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_UN_FRIEND;
    }
}
