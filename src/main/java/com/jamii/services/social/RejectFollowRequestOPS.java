package com.jamii.services.social;

import com.jamii.requests.social.RejectFollowRequestREQ;
import org.springframework.stereotype.Service;

@Service
public class RejectFollowRequestOPS extends socialAbstract{

    private RejectFollowRequestREQ rejectFollowRequestREQ;

    public void setRejectFollowRequestREQ(RejectFollowRequestREQ rejectFollowRequestREQ) {
        this.rejectFollowRequestREQ = rejectFollowRequestREQ;
    }

    public RejectFollowRequestREQ getRejectFollowRequestREQ() {
        return rejectFollowRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }
}
