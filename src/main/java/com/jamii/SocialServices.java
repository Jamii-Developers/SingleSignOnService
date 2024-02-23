package com.jamii;

import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.social.SearchUserREQ;
import com.jamii.requests.social.SendFollowRequestREQ;
import com.jamii.requests.social.SendFriendRequestREQ;
import com.jamii.services.social.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*") // Apply to the entire controller
@SpringBootApplication
@RestController
public class SocialServices {

    @Autowired
    private AcceptFollowRequestOPS acceptFollowRequestOPS;
    @Autowired
    private AcceptFriendRequestOPS acceptFriendRequestOPS;
    @Autowired
    private BlockFollowRequestOPS blockFollowRequestOPS;
    @Autowired
    private BlockFriendRequestOPS blockFriendRequestOPS;
    @Autowired
    private RejectFollowRequestOPS rejectFollowRequestOPS;
    @Autowired
    private RejectFriendRequestOPS rejectFriendRequestOPS;
    @Autowired
    private RemoveFollowRequestOPS removeFollowRequestOPS;
    @Autowired
    private RemoveFriendRequestOPS removeFriendRequestOPS;
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
        this.searchUsersOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.searchUsersOPS.getResponse( );
    }

    @PostMapping( path = "sendfriendrequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< ? > sendfriendrequest(@RequestBody SendFriendRequestREQ sendFriendRequestREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.sendFriendRequestOPS.reset( );
        this.sendFriendRequestOPS.setSendFriendRequestREQ( sendFriendRequestREQ );
        this.sendFriendRequestOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.sendFriendRequestOPS.getResponse( );
    }

    @PostMapping( path = "sendfollowrequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< ? > sendfollowrequest(@RequestBody SendFollowRequestREQ sendFollowRequestREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.sendFollowRequestOPS.reset( );
        this.sendFollowRequestOPS.setSendFollowRequestREQ( sendFollowRequestREQ );
        this.sendFollowRequestOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.sendFollowRequestOPS.getResponse( );
    }



}
