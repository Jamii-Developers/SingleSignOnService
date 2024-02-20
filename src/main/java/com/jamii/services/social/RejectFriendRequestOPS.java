package com.jamii.services.social;

import com.jamii.requests.social.RejectFriendRequestREQ;
import org.springframework.stereotype.Service;

@Service
public class RejectFriendRequestOPS extends socialAbstract{

    private RejectFriendRequestREQ rejectFriendRequestREQ;

    public void setRejectFriendRequestREQ( RejectFriendRequestREQ rejectFriendRequestREQ ) {
        this.rejectFriendRequestREQ = rejectFriendRequestREQ;
    }

    public RejectFriendRequestREQ getRejectFriendRequestREQ() {
        return rejectFriendRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }
}
