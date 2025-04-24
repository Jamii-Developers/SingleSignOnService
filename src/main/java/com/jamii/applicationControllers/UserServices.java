package com.jamii.applicationControllers;

import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiLoggingUtils;
import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.operations.userServices.clientCommunication.ContactSupportOPS;
import com.jamii.operations.userServices.clientCommunication.ReviewUsOPS;
import com.jamii.operations.userServices.fileManagement.UserFileDeleteOPS;
import com.jamii.operations.userServices.fileManagement.UserFileDirectoryUpdateOPS;
import com.jamii.operations.userServices.fileManagement.UserFileDownloadOPS;
import com.jamii.operations.userServices.fileManagement.UserFileUploadOPS;
import com.jamii.operations.userServices.social.*;
import com.jamii.operations.userServices.userProfile.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserServices extends AbstractApplicationControllers {

    @Autowired
    JamiiLoggingUtils jamiiLoggingUtils;
    @Autowired
    ReviewUsOPS reviewUsOPS;
    @Autowired
    UserFileUploadOPS userFileUploadOPS;
    @Autowired
    UserFileDirectoryUpdateOPS userFileDirectoryUpdateOPS;
    @Autowired
    UserFileDeleteOPS userFileDeleteOPS;
    @Autowired
    UserFileDownloadOPS userFileDownloadOPS;
    @Autowired
    ChangePasswordOPS changePasswordOPS;
    @Autowired
    EditUserDataOPS editUserDataOPS;
    @Autowired
    FetchUserDataOPS fetchUserDataOPS;
    @Autowired
    DeactivateUserOPS deactivateUserOPS;
    @Autowired
    UserLogoffOPS userLogoffOPS;
    @Autowired
    AcceptFollowRequestOPS acceptFollowRequestOPS;
    @Autowired
    AcceptFriendRequestOPS acceptFriendRequestOPS;
    @Autowired
    BlockUserOPS blockUserOPS;
    @Autowired
    GetBlockUserListOPS getBlockUserListOPS;
    @Autowired
    GetFollowerListOPS getFollowerListOPS;
    @Autowired
    GetFollowRequestListOPS getFollowRequestListOPS;
    @Autowired
    GetFriendListOPS getFriendListOPS;
    @Autowired
    GetFriendRequestListOPS getFriendRequestListOPS;
    @Autowired
    RejectFollowRequestOPS rejectFollowRequestOPS;
    @Autowired
    RejectFriendRequestOPS rejectFriendRequestOPS;
    @Autowired
    SearchUsersOPS searchUsersOPS;
    @Autowired
    SendFollowRequestOPS sendFollowRequestOPS;
    @Autowired
    SendFriendRequestOPS sendFriendRequestOPS;
    @Autowired
    ContactSupportOPS contactSupportOPS;

    private final JamiiDebug jamiiDebug = new JamiiDebug( this.getClass() );
    private final Map<String, AbstractUserServicesOPS > directoryMap = new HashMap<>();

    @PostConstruct
    protected void initPathing() {
        directoryMap.put( "reviewus", reviewUsOPS);
        directoryMap.put( "userfileupload", userFileUploadOPS);
        directoryMap.put( "userdirupd", userFileDirectoryUpdateOPS );
        directoryMap.put( "userfiledel", userFileDeleteOPS );
        directoryMap.put( "userfiledwnld", userFileDownloadOPS );
        directoryMap.put( "chngpassword", changePasswordOPS );
        directoryMap.put( "editprofile", editUserDataOPS );
        directoryMap.put( "fetchprofile", fetchUserDataOPS);
        directoryMap.put( "deactivateuser", deactivateUserOPS);
        directoryMap.put( "userlogoff", userLogoffOPS );
        directoryMap.put( "searchuser", searchUsersOPS );
        directoryMap.put( "sendfriendrequest", sendFriendRequestOPS );
        directoryMap.put( "getfriendrequestist", getFriendRequestListOPS );
        directoryMap.put( "getfollowerlist", getFollowerListOPS );
        directoryMap.put( "getfollowerrequestlist", getFollowRequestListOPS );
        directoryMap.put( "sendfollowrequest", sendFollowRequestOPS );
        directoryMap.put( "acceptfriendrequest", acceptFriendRequestOPS );
        directoryMap.put( "acceptfollowrequest", acceptFollowRequestOPS );
        directoryMap.put( "rejectfriendrequest", rejectFriendRequestOPS );
        directoryMap.put( "rejectfollowrequest", rejectFollowRequestOPS );
        directoryMap.put( "blockuser", blockUserOPS);
        directoryMap.put( "getfriendlist", getFriendListOPS );
        directoryMap.put( "getblockuserlist", getBlockUserListOPS);
        directoryMap.put( "contactsupport", contactSupportOPS);


    }

    public ResponseEntity<?> processJSONRequest(String operation, Object requestPayload ) {

        try {
            jamiiDebug.info("Received request for operation: " + operation);

            // Lookup the handler
            AbstractUserServicesOPS handler = directoryMap.get( operation );

            if (handler == null) {
                jamiiDebug.warn("Unknown operation: " + operation);
                throw new Exception( "Operation could not be found " + operation );
            }

            handler.reset( );
            return handler.run(requestPayload);

        }catch( Exception e ){
            jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , null ) ;
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> processMultipartRequest( String operation, String userKey, String deviceKey, String sessionKey, MultipartFile file) {

        try{
            jamiiDebug.info("Received request for operation: " + operation);

            // Lookup the handler
            AbstractUserServicesOPS handler = directoryMap.get(operation);

            if (handler == null) {
                jamiiDebug.warn("Unknown operation: " + operation);
                throw new Exception( "Operation could not be found " + operation );
            }

            handler.reset();
            return handler.run( JamiiMapperUtils.mapUploadFileObject( userKey, deviceKey, sessionKey, file ));

        }catch( Exception e ){
            jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , null ) ;
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }
}
