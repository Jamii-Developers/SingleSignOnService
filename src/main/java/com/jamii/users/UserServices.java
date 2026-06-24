package com.jamii.users;

import com.jamii.utils.JamiiDebug;
import com.jamii.utils.JamiiLoggingUtils;
import com.jamii.utils.JamiiMapperUtils;
import com.jamii.abstractClasses.AbstractApplicationControllers;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.administrative.services.clientCommunication.ContactSupportOPS;
import com.jamii.administrative.services.clientCommunication.ReviewUsOPS;
import com.jamii.jDrive.services.UserFileDeleteOPS;
import com.jamii.jDrive.services.UserFileDirectoryUpdateOPS;
import com.jamii.jDrive.services.UserFileDownloadOPS;
import com.jamii.jDrive.services.UserFileUploadOPS;
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
import com.jamii.users.services.ChangePasswordOPS;
import com.jamii.users.services.DeactivateUserOPS;
import com.jamii.users.services.EditUserDataOPS;
import com.jamii.users.services.FetchUserDataOPS;
import com.jamii.users.services.UserLogoffOPS;
import com.jamii.users.services.SessionValidator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserServices
        extends AbstractApplicationControllers
{

    private final JamiiDebug jamiiDebug = new JamiiDebug(this.getClass());
    private final Map<String, AbstractUserServicesOPS> directoryMap = new HashMap<>();

    @Autowired JamiiLoggingUtils jamiiLoggingUtils;
    @Autowired ReviewUsOPS reviewUsOPS;
    @Autowired UserFileUploadOPS userFileUploadOPS;
    @Autowired UserFileDirectoryUpdateOPS userFileDirectoryUpdateOPS;
    @Autowired UserFileDeleteOPS userFileDeleteOPS;
    @Autowired UserFileDownloadOPS userFileDownloadOPS;
    @Autowired ChangePasswordOPS changePasswordOPS;
    @Autowired EditUserDataOPS editUserDataOPS;
    @Autowired FetchUserDataOPS fetchUserDataOPS;
    @Autowired DeactivateUserOPS deactivateUserOPS;
    @Autowired UserLogoffOPS userLogoffOPS;
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
    @Autowired ContactSupportOPS contactSupportOPS;
    @Autowired SessionValidator sessionValidator;
    @Autowired RemoveFollowerOPS removeFollowerOPS;
    @Autowired UnFriendOPS unFriendOPS;
    @Autowired UnBlockUserOPS unBlockUserOPS;
    @Autowired UnfollowOPS unfollowOPS;
    @Autowired ViewUserProfileOPS viewUserProfileOPS;

    @PostConstruct
    protected void initPathing()
    {
        directoryMap.put("reviewus", reviewUsOPS);
        directoryMap.put("userfileupload", userFileUploadOPS);
        directoryMap.put("userdirupd", userFileDirectoryUpdateOPS);
        directoryMap.put("userfiledel", userFileDeleteOPS);
        directoryMap.put("userfiledwnld", userFileDownloadOPS);
        directoryMap.put("chngpassword", changePasswordOPS);
        directoryMap.put("editprofile", editUserDataOPS);
        directoryMap.put("fetchprofile", fetchUserDataOPS);
        directoryMap.put("deactivateuser", deactivateUserOPS);
        directoryMap.put("userlogoff", userLogoffOPS);
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
        directoryMap.put("contactsupport", contactSupportOPS);
        directoryMap.put("validate-session", sessionValidator);
        directoryMap.put("removefollower", removeFollowerOPS);
        directoryMap.put("unfriend", unFriendOPS);
        directoryMap.put("unblockuser", unBlockUserOPS);
        directoryMap.put("unfollow", unfollowOPS);
        directoryMap.put("viewuserprofile", viewUserProfileOPS);
    }

    public ResponseEntity<?> processJSONRequest(String operation, Object requestPayload)
    {

        try {
            jamiiDebug.info("Received request for operation: " + operation);

            // Lookup the handler
            AbstractUserServicesOPS handler = directoryMap.get(operation);

            if (handler == null) {
                jamiiDebug.warn("Unknown Service-Header : " + operation);
                throw new Exception("Operation could not be found " + operation);
            }

            handler.reset();
            return handler.run(requestPayload);
        }
        catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger(this.getClass().getName(), e, null);
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> processMultipartRequest(String operation, String userKey, String deviceKey, String sessionKey, MultipartFile file)
    {

        try {
            jamiiDebug.info("Received request for operation: " + operation);

            // Lookup the handler
            AbstractUserServicesOPS handler = directoryMap.get(operation);

            if (handler == null) {
                jamiiDebug.warn("Unknown Service-Header: " + operation);
                throw new Exception("Operation could not be found " + operation);
            }

            handler.reset();
            return handler.run(JamiiMapperUtils.mapUploadFileObject(userKey, deviceKey, sessionKey, file));
        }
        catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger(this.getClass().getName(), e, null);
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }
}
