package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class UnFollowRESP extends AbstractResponses {

    public UnFollowRESP( UserLoginTBL user ) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully unfollowed @" + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_UN_FOLLOW;
    }
}
