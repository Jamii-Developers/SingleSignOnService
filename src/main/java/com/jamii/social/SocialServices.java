package com.jamii.social;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import com.jamii.social.services.AcceptFollowRequestOPS;
import com.jamii.social.services.AcceptFriendRequestOPS;
import com.jamii.social.services.BlockUserOPS;
import com.jamii.social.services.GetBlockUserListOPS;
import com.jamii.social.services.GetFollowRequestListOPS;
import com.jamii.social.services.GetFollowerListOPS;
import com.jamii.social.services.GetFriendListOPS;
import com.jamii.social.services.GetFriendRequestListOPS;
import com.jamii.social.services.RejectFollowRequestOPS;
import com.jamii.social.services.RejectFriendRequestOPS;
import com.jamii.social.services.RemoveFollowerOPS;
import com.jamii.social.services.SearchUsersOPS;
import com.jamii.social.services.SendFollowRequestOPS;
import com.jamii.social.services.SendFriendRequestOPS;
import com.jamii.social.services.UnBlockUserOPS;
import com.jamii.social.services.UnFriendOPS;
import com.jamii.social.services.UnfollowOPS;
import com.jamii.social.services.ViewUserProfileOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/social/")
@CrossOrigin(origins = "*")
public class SocialServices
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
    @Autowired SearchUsersOPS searchUsersOPS;
    @Autowired SendFollowRequestOPS sendFollowRequestOPS;
    @Autowired SendFriendRequestOPS sendFriendRequestOPS;
    @Autowired RemoveFollowerOPS removeFollowerOPS;
    @Autowired UnFriendOPS unFriendOPS;
    @Autowired UnBlockUserOPS unBlockUserOPS;
    @Autowired UnfollowOPS unfollowOPS;
    @Autowired ViewUserProfileOPS viewUserProfileOPS;


    @Override
    protected void initPathing()
    {
        directoryMap.put("searchuser", searchUsersOPS);
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
        directoryMap.put("viewuserprofile", viewUserProfileOPS);
    }
}
