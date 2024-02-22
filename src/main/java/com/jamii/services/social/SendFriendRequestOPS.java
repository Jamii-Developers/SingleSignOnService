package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.requests.social.SendFriendRequestREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendFriendRequestOPS extends socialAbstract {

    private SendFriendRequestREQ sendFriendRequestREQ;

    public void setSendFriendRequestREQ(SendFriendRequestREQ sendFriendRequestREQ) {
        this.sendFriendRequestREQ = sendFriendRequestREQ;
    }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;

    public SendFriendRequestREQ getSendFriendRequestREQ() {
        return sendFriendRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }

}
