package com.jamii.jSocial;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import com.jamii.jSocial.services.AcceptFollowRequestOPS;
import com.jamii.jSocial.services.AcceptFriendRequestOPS;
import com.jamii.jSocial.services.BlockUserOPS;
import com.jamii.jSocial.services.GetBlockUserListOPS;
import com.jamii.jSocial.services.GetFollowRequestListOPS;
import com.jamii.jSocial.services.GetFollowerListOPS;
import com.jamii.jSocial.services.GetFriendListOPS;
import com.jamii.jSocial.services.GetFriendRequestListOPS;
import com.jamii.jSocial.services.RejectFollowRequestOPS;
import com.jamii.jSocial.services.RejectFriendRequestOPS;
import com.jamii.jSocial.services.RemoveFollowerOPS;
import com.jamii.jSocial.services.SendFollowRequestOPS;
import com.jamii.jSocial.services.SendFriendRequestOPS;
import com.jamii.jSocial.services.UnBlockUserOPS;
import com.jamii.jSocial.services.UnFriendOPS;
import com.jamii.jSocial.services.UnfollowOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jsocial/")
@CrossOrigin(origins = "*")
public class JSocialServices
    extends AbstractApplicationControllers
{
    @Autowired AcceptFollowRequestOPS acceptFollowRequestOPS;
    @Autowired AcceptFriendRequestOPS acceptFriendRequestOPS;
    @Autowired BlockUserOPS blockUserOPS;
    @Autowired GetBlockUserListOPS getBlockUserListOPS;
    @Autowired GetFollowerListOPS getFollowerListOPS;
    @Autowired GetFollowRequestListOPS getFollowRequestListOPS;
    @Autowired GetFriendListOPS getFriendListOPS;
    @Autowired GetFriendRequestListOPS getFriendRequestListOPS;
    @Autowired RejectFollowRequestOPS rejectFollowRequestOPS;
    @Autowired RejectFriendRequestOPS rejectFriendRequestOPS;
    @Autowired SendFollowRequestOPS sendFollowRequestOPS;
    @Autowired SendFriendRequestOPS sendFriendRequestOPS;
    @Autowired RemoveFollowerOPS removeFollowerOPS;
    @Autowired UnFriendOPS unFriendOPS;
    @Autowired UnBlockUserOPS unBlockUserOPS;
    @Autowired UnfollowOPS unfollowOPS;


    @Override
    protected void initPathing()
    {
        directoryMap.put("sendfriendrequest", sendFriendRequestOPS);
        directoryMap.put("getfriendrequestlist", getFriendRequestListOPS);
        directoryMap.put("getfollowerlist", getFollowerListOPS);
        directoryMap.put("getfollowerrequestlist", getFollowRequestListOPS);
        directoryMap.put("sendfollowrequest", sendFollowRequestOPS);
        directoryMap.put("acceptfriendrequest", acceptFriendRequestOPS);
        directoryMap.put("acceptfollowrequest", acceptFollowRequestOPS);
        directoryMap.put("rejectfriendrequest", rejectFriendRequestOPS);
        directoryMap.put("rejectfollowrequest", rejectFollowRequestOPS);
        directoryMap.put("blockuser", blockUserOPS);
        directoryMap.put("getfriendlist", getFriendListOPS);
        directoryMap.put("getblockuserlist", getBlockUserListOPS);
        directoryMap.put("removefollower", removeFollowerOPS);
        directoryMap.put("unfriend", unFriendOPS);
        directoryMap.put("unblockuser", unBlockUserOPS);
        directoryMap.put("unfollow", unfollowOPS);
    }
}
