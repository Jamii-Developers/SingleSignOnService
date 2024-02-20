package com.jamii.services.social;

import com.jamii.requests.social.BlockFriendRequestREQ;
import org.springframework.stereotype.Service;

@Service
public class BlockFriendRequestOPS extends socialAbstract{

    private BlockFriendRequestREQ blockFriendRequestREQ;

    public void setBlockFriendRequestREQ(BlockFriendRequestREQ blockFriendRequestREQ) {
        this.blockFriendRequestREQ = blockFriendRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }
}
