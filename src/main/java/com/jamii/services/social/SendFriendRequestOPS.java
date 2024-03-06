package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserBlockListCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.controllers.UserRequestCONT;
import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.requests.social.SendFriendRequestREQ;
import com.jamii.responses.social.SendFriendRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SendFriendRequestOPS extends socialAbstract {

    private SendFriendRequestREQ sendFriendRequestREQ;
    private Optional< UserLoginTBL > receiver;

    public void setSendFriendRequestREQ(SendFriendRequestREQ sendFriendRequestREQ) {
        this.sendFriendRequestREQ = sendFriendRequestREQ;
    }

    public SendFriendRequestREQ getSendFriendRequestREQ() {
        return sendFriendRequestREQ;
    }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;
    @Autowired
    private UserRequestCONT userRequestCONT;
    @Autowired
    private UserBlockListCONT userBlockListCONT;



    @Override
    public void processRequest( ) throws Exception {

        DeviceKey = getSendFriendRequestREQ( ).getDevicekey( );
        UserKey = getSendFriendRequestREQ( ).getUserkey( );

        super.processRequest( );

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetch( getSendFriendRequestREQ( ).getReceiveruserkey(), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        //Fetch requests to user
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequestCONT.fetch( sender.get( ), receiver.get( ), UserRequestsTBL.TYPE_FRIEND ) );
        requests.addAll( userRequestCONT.fetch( receiver.get( ), sender.get( ), UserRequestsTBL.TYPE_FRIEND ) );
        //Fetch Block List
        List<UserBlockListTBL> blockList = new ArrayList<>( );
        blockList.addAll( userBlockListCONT.fetch( sender.get( ), receiver.get( ), UserBlockListTBL.STATUS_ACTIVE ) );
        blockList.addAll( userBlockListCONT.fetch( receiver.get( ), sender.get( ), UserBlockListTBL.STATUS_ACTIVE ) );
        //Fetch Relationships
        List<UserRelationshipTBL> relationship = new ArrayList<>( );
        relationship.addAll( userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FRIEND ) );
        relationship.addAll( userRelationshipCONT.fetch(  receiver.get( ), sender.get( ),UserRelationshipTBL.TYPE_FRIEND ) );

        //Check if a friend request has been sent to the receiver
        if( !requests.isEmpty() && requests.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserRequestsTBL.STATUS_ACTIVE) && x.getSenderid( ) == sender.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_FriendRequestIsAlreadyAvailable( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if a friend request has already been sent by the receiver
        if( !requests.isEmpty() && requests.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserRequestsTBL.STATUS_ACTIVE) && x.getSenderid( ) == receiver.get( ) )){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_FriendRequestHasBeenSentByTheReceiver( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if the users are already friends
        if( !relationship.isEmpty( ) && relationship.stream( ).anyMatch( x -> Objects.equals( x.getStatus(), UserRelationshipTBL.STATUS_ACTIVE ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_AreAlreadyFriends( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if sender has been blocked
        if( !blockList.isEmpty( ) && blockList.stream( ).anyMatch( x -> Objects.equals( x.getStatus( ), UserBlockListTBL.STATUS_ACTIVE ) && x.getUserid( ) == receiver.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_BlockedUserVagueResponse( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check is sender blocked this receiver
        if( !blockList.isEmpty( ) && blockList.stream( ).anyMatch( x -> Objects.equals( x.getStatus( ), UserBlockListTBL.STATUS_ACTIVE ) && x.getUserid( ) == sender.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_YouHaveBlockedThisUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        userRequestCONT.add( sender.get( ) , receiver.get( ), UserRelationshipTBL.TYPE_FRIEND, UserRelationshipTBL.STATUS_ACTIVE );
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){
            SendFriendRequestRESP sendFriendRequestRESP = new SendFriendRequestRESP( this.receiver.get( ) );
            return  new ResponseEntity< >( sendFriendRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.receiver = Optional.empty( );
    }

}
