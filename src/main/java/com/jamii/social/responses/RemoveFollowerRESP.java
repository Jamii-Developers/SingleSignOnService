package com.jamii.social.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.users.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractResponses;

public class RemoveFollowerRESP extends AbstractResponses {

    public RemoveFollowerRESP( UserLoginTBL user ) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully removed @" + user.getUsername( ) + " as a follower" ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_REJECT_REMOVE_FOLLOWER;
    }
}
