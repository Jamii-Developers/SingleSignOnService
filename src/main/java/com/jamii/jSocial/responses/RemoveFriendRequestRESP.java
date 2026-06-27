package com.jamii.jSocial.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractResponses;

/** Use RejectFollowRequestResp Instead **/
@Deprecated
public class RemoveFriendRequestRESP extends AbstractResponses {

    public RemoveFriendRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully removed a friend request from " + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_REJECT_FRIEND_REQUEST;
    }

}
