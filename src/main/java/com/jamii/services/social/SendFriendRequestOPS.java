package com.jamii.services.social;

import com.jamii.requests.social.SendFriendRequestREQ;
import org.springframework.stereotype.Service;

@Service
public class SendFriendRequestOPS extends socialAbstract {

    private SendFriendRequestREQ sendFriendRequestREQ;

    public void setSendFriendRequestREQ(SendFriendRequestREQ sendFriendRequestREQ) {
        this.sendFriendRequestREQ = sendFriendRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }

}
