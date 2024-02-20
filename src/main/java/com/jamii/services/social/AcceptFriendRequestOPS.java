package com.jamii.services.social;

import com.jamii.requests.social.AcceptFriendRequestREQ;
import org.springframework.stereotype.Service;

@Service
public class AcceptFriendRequestOPS extends socialAbstract {

    private AcceptFriendRequestREQ acceptFriendRequestREQ;

    public void setAcceptFriendRequestREQ( AcceptFriendRequestREQ acceptFriendRequestREQ ) {
        this.acceptFriendRequestREQ = acceptFriendRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }
}
