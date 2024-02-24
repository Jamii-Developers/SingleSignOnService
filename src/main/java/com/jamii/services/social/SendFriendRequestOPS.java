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

import java.time.LocalDateTime;
import java.util.Objects;
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
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        Optional< UserRelationshipTBL > getSenderReceiverRelationship = userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FRIEND );
        Optional< UserRelationshipTBL > getReceiverSenderRelationship = userRelationshipCONT.fetch( receiver.get( ),sender.get( ), UserRelationshipTBL.TYPE_FRIEND );

        //Check if a friend request has been sent to the receiver
        if( getSenderReceiverRelationship.isPresent( ) && Objects.equals( getSenderReceiverRelationship.get( ).getStatus(), UserRelationshipTBL.STATUS_PENDING) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_FriendRequestIsAlreadyAvailable( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if a friend request has already been sent by the receiver
        if( getReceiverSenderRelationship.isPresent( ) && Objects.equals( getReceiverSenderRelationship.get( ).getStatus(), UserRelationshipTBL.STATUS_PENDING ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_FriendRequestHasBeenSentByTheReceiver( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if the users are already friends
        if( getSenderReceiverRelationship.isPresent( ) && Objects.equals( getSenderReceiverRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_ACCEPTED ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_AreAlreadyFriends( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if sender has been blocked
        if( getSenderReceiverRelationship.isPresent( ) && Objects.equals( getSenderReceiverRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_BLOCKED ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_BlockedUserVagueResponse( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check is sender blocked this receiver
        if( getReceiverSenderRelationship.isPresent( ) && Objects.equals( getReceiverSenderRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_BLOCKED) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_YouHaveBlockedThisUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if sender already has a rejected friend request
        if( getReceiverSenderRelationship.isPresent( ) && Objects.equals( getReceiverSenderRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_REJECTED ) ){
            getReceiverSenderRelationship.get( ).setStatus( UserRelationshipTBL.STATUS_PENDING );
            getReceiverSenderRelationship.get( ).setDateupdated( LocalDateTime.now( ) );
            this.userRelationshipCONT.update( getReceiverSenderRelationship.get( ) );
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
