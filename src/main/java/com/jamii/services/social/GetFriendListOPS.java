package com.jamii.services.social;

import com.jamii.requests.social.GetFriendListREQ;
import org.springframework.stereotype.Service;

@Service
public class GetFriendListOPS extends socialAbstract{

    private GetFriendListREQ getFriendListREQ;

    public GetFriendListREQ getGetFriendListREQ() {
        return getFriendListREQ;
    }

    public void setGetFriendListREQ(GetFriendListREQ getFriendListREQ) {
        this.getFriendListREQ = getFriendListREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }
}
