package com.jamii.services.social;

import com.jamii.requests.social.BlockFollowRequestREQ;
import org.springframework.stereotype.Service;

@Service
public class BlockFollowRequestOPS extends socialAbstract{

    private BlockFollowRequestREQ blockFollowRequestREQ;

    public void setBlockFollowRequestREQ(BlockFollowRequestREQ blockFollowRequestREQ) {
        this.blockFollowRequestREQ = blockFollowRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }
}
