package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class UnBlockUserRESP extends AbstractResponses {

    public UnBlockUserRESP( UserLoginTBL user ) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully unblocked @" + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_UNBLOCK_USER;
    }
}
