package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.requests.social.SendFriendRequestREQ;
import com.jamii.responses.social.SendFriendRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SendFriendRequestOPS extends socialAbstract {

    private SendFriendRequestREQ sendFriendRequestREQ;

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



    @Override
    public void processRequest( ) throws Exception {

        DeviceKey = getSendFriendRequestREQ( ).getDevicekey( );
        UserKey = getSendFriendRequestREQ( ).getUserkey( );

        super.processRequest( );

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        Optional<UserLoginTBL> receiver = this.userLoginCONT.fetch( getSendFriendRequestREQ( ).getReceiveruserkey(), UserLoginTBL.ACTIVE_ON );

        //Check if a friend request has been sent to the receiver
        Optional< UserRelationshipTBL > checkIfTheresAPendingRequestFromTheSender = userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FRIEND, UserRelationshipTBL.STATUS_PENDING );
        if( checkIfTheresAPendingRequestFromTheSender.isPresent( ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_FriendRequestIsAlreadyAvailable( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if a friend request has already been sent by the receiver
        Optional< UserRelationshipTBL > checkIfTheresAPendingRequestFromTheReciver = userRelationshipCONT.fetch( receiver.get( ),sender.get( ), UserRelationshipTBL.TYPE_FRIEND, UserRelationshipTBL.STATUS_PENDING );
        if( checkIfTheresAPendingRequestFromTheReciver.isPresent( ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_FriendRequestHasBeenSentByTheReceiver( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if the users are already friends
        Optional< UserRelationshipTBL > checkIfUsersAreAlreadyFriends = userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FRIEND, UserRelationshipTBL.STATUS_ACCEPTED );
        if( checkIfUsersAreAlreadyFriends.isPresent( ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_AreAlreadyFriends( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if sender has been blocked
        Optional< UserRelationshipTBL > checkIfSenderIsBlocked = userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FRIEND, UserRelationshipTBL.STATUS_BLOCKED );
        if( checkIfSenderIsBlocked.isPresent( ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_BlockedUserVagueResponse( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check is sender blocked this receiver
        Optional< UserRelationshipTBL > checkIfReceiverHasBeenBlocked = userRelationshipCONT.fetch( receiver.get( ), sender.get( ),  UserRelationshipTBL.TYPE_FRIEND, UserRelationshipTBL.STATUS_BLOCKED );
        if( checkIfReceiverHasBeenBlocked.isPresent( ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_YouHaveBlockedThisUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        userRelationshipCONT.add( sender.get( ) , receiver.get( ), UserRelationshipTBL.TYPE_FRIEND, UserRelationshipTBL.STATUS_PENDING );
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){
            SendFriendRequestRESP sendFriendRequestRESP = new SendFriendRequestRESP( );
            return  new ResponseEntity< >( sendFriendRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }

}
