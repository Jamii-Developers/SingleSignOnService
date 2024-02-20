package com.jamii;

import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.social.AcceptFollowRequestREQ;
import com.jamii.requests.social.AcceptFriendRequestREQ;
import com.jamii.requests.social.BlockFollowRequestREQ;
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

    private final JamiiDebug jamiiDebug = new JamiiDebug( );

    public static void main( String[ ] args ) {
        SpringApplication.run( SocialServices.class, args);
    }

    @PostMapping( path = "acceptfriendrequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< String > acceptfriendrequest(@RequestBody AcceptFriendRequestREQ acceptFriendRequestREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.acceptFriendRequestOPS.reset( );
        this.acceptFriendRequestOPS.setAcceptFriendRequestREQ( acceptFriendRequestREQ );
        this.acceptFriendRequestOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.acceptFriendRequestOPS.getResponse( );
    }

    @PostMapping( path = "acceptfollowrequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< String > acceptfollowrequest(@RequestBody AcceptFollowRequestREQ acceptFriendRequestREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.acceptFollowRequestOPS.reset( );
        this.acceptFollowRequestOPS.setAcceptFollowRequestREQ( acceptFriendRequestREQ );
        this.acceptFollowRequestOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.acceptFriendRequestOPS.getResponse( );
    }

    @PostMapping( path = "blockfollowrequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< String > blockfollowrequest(@RequestBody BlockFollowRequestREQ blockFollowRequestREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.blockFollowRequestOPS.reset( );
        this.blockFollowRequestOPS.setBlockFollowRequestREQ( blockFollowRequestREQ );
        this.blockFollowRequestOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.blockFollowRequestOPS.getResponse( );
    }

}
