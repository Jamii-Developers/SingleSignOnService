package com.jamii.services.social;

import com.jamii.requests.social.GetFollowerListREQ;
import org.springframework.stereotype.Service;

@Service
public class GetFollowRequestListOPS extends socialAbstract{

    private GetFollowerListREQ getFollowerListREQ;

    public GetFollowerListREQ getGetFollowerListREQ() {
        return getFollowerListREQ;
    }

    public void setGetFollowerListREQ(GetFollowerListREQ getFollowerListREQ) {
        this.getFollowerListREQ = getFollowerListREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }
}
