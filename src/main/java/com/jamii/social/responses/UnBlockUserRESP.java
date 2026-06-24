package com.jamii.social.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.users.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractResponses;

public class UnBlockUserRESP extends AbstractResponses {

    public UnBlockUserRESP( UserLoginTBL user ) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully unblocked @" + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_UNBLOCK_USER;
    }
}
