package com.jamii.services.social;

import com.jamii.requests.social.GetFollowerRequestListREQ;
import org.springframework.stereotype.Service;

@Service
public class GetFollowerListOPS extends socialAbstract{

    private GetFollowerRequestListREQ getFollowerRequestListREQ;

    public GetFollowerRequestListREQ getGetFollowerRequestListREQ() {
        return getFollowerRequestListREQ;
    }

    public void setGetFollowerRequestListREQ(GetFollowerRequestListREQ getFollowerRequestListREQ) {
        this.getFollowerRequestListREQ = getFollowerRequestListREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }
}
