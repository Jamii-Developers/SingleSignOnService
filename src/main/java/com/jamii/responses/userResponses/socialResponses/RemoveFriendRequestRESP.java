package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

@Deprecated /** Use RejectFollowRequestResp Instead **/
public class RemoveFriendRequestRESP extends AbstractResponses {

    public RemoveFriendRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully removed a friend request from " + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_REJECT_FRIEND_REQUEST;
    }

}
