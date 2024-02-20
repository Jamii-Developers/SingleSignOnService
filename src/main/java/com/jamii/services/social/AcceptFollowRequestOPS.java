package com.jamii.services.social;

import com.jamii.requests.social.AcceptFollowRequestREQ;
import org.springframework.stereotype.Service;

@Service
public class AcceptFollowRequestOPS extends socialAbstract{

    private AcceptFollowRequestREQ acceptFollowRequestREQ;

    public void setAcceptFollowRequestREQ(AcceptFollowRequestREQ acceptFollowRequestREQ) {
        this.acceptFollowRequestREQ = acceptFollowRequestREQ;
    }

    public AcceptFollowRequestREQ getAcceptFollowRequestREQ() {
        return acceptFollowRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }
}
