package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class RemoveFollowerRESP extends AbstractResponses {

    public RemoveFollowerRESP( UserLoginTBL user ) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully removed @" + user.getUsername( ) + "as a follower" ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_REJECT_REMOVE_FOLLOWER;
    }
}
