package com.jamii.social.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.users.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractResponses;

public class BlockUserRequestRESP extends AbstractResponses {

    public BlockUserRequestRESP(UserLoginTBL user ) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully blocked @" + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_BLOCK_USER_REQUEST;
    }
}
