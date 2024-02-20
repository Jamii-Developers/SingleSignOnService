package com.jamii.services.social;

import com.jamii.requests.social.RemoveFollowRequestREQ;
import org.springframework.stereotype.Service;

@Service
public class RemoveFollowRequestOPS extends socialAbstract{

    private RemoveFollowRequestREQ removeFollowRequestREQ;

    public void setRemoveFollowRequestREQ(RemoveFollowRequestREQ removeFollowRequestREQ) {
        this.removeFollowRequestREQ = removeFollowRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }
}
