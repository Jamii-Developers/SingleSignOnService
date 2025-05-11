package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class UnFriendRESP extends AbstractResponses {

    public UnFriendRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully removed @" + user.getUsername( ) + "as a friend" ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_REJECT_REMOVED_FRIEND;
    }
}
