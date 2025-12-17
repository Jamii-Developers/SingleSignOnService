package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

@Deprecated /** Use RejectFriendRequestResp Instead **/
public class RemoveFollowRequestRESP extends AbstractResponses {

    public RemoveFollowRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully removed a follow request from " + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_REMOVE_FOLLOW_REQUEST;
    }
}
