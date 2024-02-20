package com.jamii.services.social;

import com.jamii.requests.social.RemoveFriendRequestREQ;
import org.springframework.stereotype.Service;

@Service
public class RemoveFriendRequestOPS {

    private RemoveFriendRequestREQ removeFriendRequestREQ;

    public void setRemoveFriendRequestREQ(RemoveFriendRequestREQ removeFriendRequestREQ) {
        this.removeFriendRequestREQ = removeFriendRequestREQ;
    }

    public RemoveFriendRequestREQ getRemoveFriendRequestREQ() {
        return removeFriendRequestREQ;
    }
}
