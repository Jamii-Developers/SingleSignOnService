package com.jamii;

import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.social.*;
import com.jamii.services.social.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*") // Apply to the entire controller
@RestController
public class SocialServices {

    @Autowired
    private AcceptFollowRequestOPS acceptFollowRequestOPS;
    @Autowired
    private AcceptFriendRequestOPS acceptFriendRequestOPS;
    @Autowired
    private BlockUserRequestOPS blockUserRequestOPS;
    @Autowired
    private RejectFollowRequestOPS rejectFollowRequestOPS;
    @Autowired
    private RejectFriendRequestOPS rejectFriendRequestOPS;
    @Autowired
    private SendFollowRequestOPS sendFollowRequestOPS;
    @Autowired
    private SendFriendRequestOPS sendFriendRequestOPS;
    @Autowired
    private GetFollowerListOPS getFollowerListOPS;
    @Autowired
    private GetFollowRequestListOPS getFollowRequestListOPS;
    @Autowired
    private GetFriendListOPS getFriendListOPS;
    @Autowired
    private GetFriendRequestListOPS getFriendRequestListOPS;
    @Autowired
    private SearchUsersOPS searchUsersOPS;

    private final JamiiDebug jamiiDebug = new JamiiDebug( );

    public static void main( String[ ] args ) {
        SpringApplication.run( SocialServices.class, args);
    }

    @PostMapping( path = "searchuser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< ? > searchuser(@RequestBody SearchUserREQ searchUserREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.searchUsersOPS.reset( );
        this.searchUsersOPS.setSearchUserREQ( searchUserREQ );
        this.searchUsersOPS.validateCookie( );
        this.searchUsersOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.searchUsersOPS.getResponse( );
    }

    @PostMapping( path = "sendfriendrequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< ? > sendfriendrequest(@RequestBody SendFriendRequestREQ sendFriendRequestREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.sendFriendRequestOPS.reset( );
        this.sendFriendRequestOPS.setSendFriendRequestREQ( sendFriendRequestREQ );
        this.sendFriendRequestOPS.validateCookie( );
        this.sendFriendRequestOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.sendFriendRequestOPS.getResponse( );
    }

    @PostMapping( path = "sendfollowrequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< ? > sendfollowrequest(@RequestBody SendFollowRequestREQ sendFollowRequestREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.sendFollowRequestOPS.reset( );
        this.sendFollowRequestOPS.setSendFollowRequestREQ( sendFollowRequestREQ );
        this.sendFollowRequestOPS.validateCookie( );
        this.sendFollowRequestOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.sendFollowRequestOPS.getResponse( );
    }

    @PostMapping( path = "acceptFriendRequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< ? > acceptFriendRequest(@RequestBody AcceptFriendRequestREQ acceptFriendRequestREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.acceptFriendRequestOPS.reset( );
        this.acceptFriendRequestOPS.setAcceptFriendRequestREQ( acceptFriendRequestREQ );
        this.acceptFriendRequestOPS.validateCookie( );
        this.acceptFriendRequestOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.acceptFriendRequestOPS.getResponse( );
    }

    @PostMapping( path = "acceptFollowRequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< ? > acceptFollowRequest(@RequestBody AcceptFollowRequestREQ acceptFollowRequestREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.acceptFollowRequestOPS.reset( );
        this.acceptFollowRequestOPS.setAcceptFollowRequestREQ( acceptFollowRequestREQ );
        this.acceptFollowRequestOPS.validateCookie( );
        this.acceptFollowRequestOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.acceptFollowRequestOPS.getResponse( );
    }



    @PostMapping( path = "rejectFriendRequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< ? > rejectFriendRequest(@RequestBody RejectFriendRequestREQ rejectFriendRequestREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.rejectFriendRequestOPS.reset( );
        this.rejectFriendRequestOPS.setRejectFriendRequestREQ( rejectFriendRequestREQ );
        this.rejectFriendRequestOPS.validateCookie( );
        this.rejectFriendRequestOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.rejectFriendRequestOPS.getResponse( );
    }

    @PostMapping( path = "rejectFollowRequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< ? > rejectFollowRequest(@RequestBody RejectFollowRequestREQ rejectFollowRequestREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.rejectFollowRequestOPS.reset( );
        this.rejectFollowRequestOPS.setRejectFollowRequestREQ( rejectFollowRequestREQ );
        this.rejectFollowRequestOPS.validateCookie( );
        this.rejectFollowRequestOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.rejectFollowRequestOPS.getResponse( );
    }

    @PostMapping( path = "blockUserRequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< ? > blockUserRequest(@RequestBody BlockUserRequestREQ blockUserRequestREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.blockUserRequestOPS.reset( );
        this.blockUserRequestOPS.setBlockUserRequestREQ( blockUserRequestREQ );
        this.blockUserRequestOPS.validateCookie(  );
        this.blockUserRequestOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.blockUserRequestOPS.getResponse( );
    }



}
