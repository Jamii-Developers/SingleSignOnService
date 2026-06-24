package com.jamii.social.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.users.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractResponses;

@Deprecated /** Use RejectFollowRequestResp Instead **/
public class RemoveFriendRequestRESP extends AbstractResponses {

    public RemoveFriendRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully removed a friend request from " + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_REJECT_FRIEND_REQUEST;
    }

}
