package com.jamii.social.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.users.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractResponses;

public class UnFollowRESP extends AbstractResponses {

    public UnFollowRESP( UserLoginTBL user ) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully unfollowed @" + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_UN_FOLLOW;
    }
}
