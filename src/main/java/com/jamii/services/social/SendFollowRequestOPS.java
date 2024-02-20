package com.jamii.services.social;

import com.jamii.requests.social.SendFollowRequestREQ;
import org.springframework.stereotype.Service;

@Service
public class SendFollowRequestOPS extends socialAbstract{

    private SendFollowRequestREQ sendFollowRequestREQ;

    public void setSendFollowRequestREQ(SendFollowRequestREQ sendFollowRequestREQ) {
        this.sendFollowRequestREQ = sendFollowRequestREQ;
    }

    public SendFollowRequestREQ getSendFollowRequestREQ() {
        return sendFollowRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }
}
