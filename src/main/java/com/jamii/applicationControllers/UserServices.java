package com.jamii.applicationControllers;

import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiLoggingUtils;
import com.jamii.Utils.JamiiMapperUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServices {

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
    private final Map<String, Object> directoryMap = new HashMap<>();

    @PostConstruct
    private void initPathing() {
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

    public ResponseEntity<?> processRequest( String operation, Object requestPayload ) {

        try {
            jamiiDebug.info("Received request for operation: " + operation);

            // Lookup the handler
            Object handler = directoryMap.get(operation);

            if (handler instanceof ReviewUsOPS) {
                ((ReviewUsOPS) handler).reset();
                return ((ReviewUsOPS) handler).run(requestPayload);
            }

            if (handler instanceof UserFileDirectoryUpdateOPS) {
                ((UserFileDirectoryUpdateOPS) handler).reset();
                return ((UserFileDirectoryUpdateOPS) handler).run(requestPayload);
            }

            if (handler instanceof UserFileDeleteOPS) {
                ((UserFileDeleteOPS) handler).reset();
                return ((UserFileDeleteOPS) handler).run(requestPayload);
            }

            if (handler instanceof UserFileDownloadOPS) {
                ((UserFileDownloadOPS) handler).reset();
                return ((UserFileDownloadOPS) handler).run(requestPayload);
            }

            if (handler instanceof ChangePasswordOPS) {
                ((ChangePasswordOPS) handler).reset();
                return ((ChangePasswordOPS) handler).run(requestPayload);
            }

            if (handler instanceof EditUserDataOPS) {
                ((EditUserDataOPS) handler).reset();
                return ((EditUserDataOPS) handler).run(requestPayload);
            }

            if (handler instanceof FetchUserDataOPS) {
                ((FetchUserDataOPS) handler).reset();
                return ((FetchUserDataOPS) handler).run(requestPayload);
            }

            if (handler instanceof DeactivateUserOPS) {
                ((DeactivateUserOPS) handler).reset();
                return ((DeactivateUserOPS) handler).run(requestPayload);
            }

            if (handler instanceof UserLogoffOPS) {
                ((UserLogoffOPS) handler).reset();
                return ((UserLogoffOPS) handler).run(requestPayload);
            }

            if (handler instanceof AcceptFriendRequestOPS) {
                ((AcceptFriendRequestOPS) handler).reset();
                return ((AcceptFriendRequestOPS) handler).run(requestPayload);
            }

            if (handler instanceof AcceptFollowRequestOPS) {
                ((AcceptFollowRequestOPS) handler).reset();
                return ((AcceptFollowRequestOPS) handler).run(requestPayload);
            }

            if (handler instanceof BlockUserOPS) {
                ( (BlockUserOPS) handler).reset( );
                return ( (BlockUserOPS) handler).run(requestPayload);
            }

            if (handler instanceof GetBlockUserListOPS) {
                ( (GetBlockUserListOPS) handler).reset( );
                return ( (GetBlockUserListOPS) handler).run(requestPayload);
            }

            if (handler instanceof GetFollowerListOPS) {
                ( (GetFollowerListOPS) handler).reset( );
                return ( (GetFollowerListOPS) handler).run(requestPayload);
            }

            if (handler instanceof GetFollowRequestListOPS) {
                ( (GetFollowRequestListOPS) handler).reset( );
                return ( (GetFollowRequestListOPS) handler).run(requestPayload);
            }

            if (handler instanceof GetFriendListOPS) {
                ( (GetFriendListOPS) handler).reset( );
                return ( (GetFriendListOPS) handler).run(requestPayload);
            }

            if (handler instanceof GetFriendRequestListOPS) {
                ( (GetFriendRequestListOPS) handler).reset( );
                return ( ( GetFriendRequestListOPS ) handler).run(requestPayload);
            }

            if (handler instanceof RejectFollowRequestOPS) {
                ( (RejectFollowRequestOPS) handler).reset( );
                return ( ( RejectFollowRequestOPS ) handler).run(requestPayload);
            }

            if (handler instanceof RejectFriendRequestOPS) {
                ( (RejectFriendRequestOPS) handler).reset( );
                return ( ( RejectFriendRequestOPS ) handler).run(requestPayload);
            }

            if (handler instanceof SearchUsersOPS) {
                ( (SearchUsersOPS) handler).reset( );
                return ( ( SearchUsersOPS ) handler).run(requestPayload);
            }

            if (handler instanceof SendFollowRequestOPS) {
                ( (SendFollowRequestOPS) handler).reset( );
                return ( ( SendFollowRequestOPS ) handler).run(requestPayload);
            }

            if (handler instanceof SendFriendRequestOPS) {
                ( (SendFriendRequestOPS) handler).reset( );
                return ( ( SendFriendRequestOPS ) handler).run(requestPayload);
            }

            if (handler instanceof ContactSupportOPS) {
                ( (ContactSupportOPS) handler).reset( );
                return ( ( ContactSupportOPS ) handler).run( requestPayload );
            }

        }catch( Exception e ){
            jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , null ) ;
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> processMultipartRequest(String operation, String userKey, String deviceKey, String sessionKey, MultipartFile file) {

        try{
            jamiiDebug.info("Received request for operation: " + operation);

            // Lookup the handler
            Object handler = directoryMap.get(operation);

            if ( handler instanceof UserFileUploadOPS){
                ( (UserFileUploadOPS) handler).reset( );
                return ( (UserFileUploadOPS) handler).run(JamiiMapperUtils.mapUploadFileObject( userKey, deviceKey, sessionKey, file ) );
            }
        }catch( Exception e ){
            jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , null ) ;
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }
}
