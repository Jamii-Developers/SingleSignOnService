package com.jamii.jSocial.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractResponses;

/** Use RejectFriendRequestResp Instead **/
@Deprecated
public class RemoveFollowRequestRESP extends AbstractResponses {

    public RemoveFollowRequestRESP(UserLoginTBL user) {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "You have successfully removed a follow request from " + user.getUsername( ) ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_REMOVE_FOLLOW_REQUEST;
    }
}
