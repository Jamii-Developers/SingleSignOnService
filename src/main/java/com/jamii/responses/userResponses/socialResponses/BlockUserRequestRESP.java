package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class BlockUserRequestRESP extends AbstractResponses {

    public BlockUserRequestRESP(UserLoginTBL user ) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully blocked @" + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_BLOCK_USER_REQUEST;
    }
}
