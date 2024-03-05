package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.requests.social.AcceptFriendRequestREQ;
import com.jamii.responses.social.AcceptFriendRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class AcceptFriendRequestOPS extends socialAbstract {

    private AcceptFriendRequestREQ acceptFriendRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;

    public void setAcceptFriendRequestREQ( AcceptFriendRequestREQ acceptFriendRequestREQ ) {
        this.acceptFriendRequestREQ = acceptFriendRequestREQ;
    }

    public AcceptFriendRequestREQ getAcceptFriendRequestREQ() {
        return acceptFriendRequestREQ;
    }


    @Override
    public void processRequest() throws Exception {

        DeviceKey = getAcceptFriendRequestREQ( ).getDevicekey( );
        UserKey = getAcceptFriendRequestREQ( ).getUserkey( );

        super.processRequest( );

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetch( getAcceptFriendRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        Optional< UserRelationshipTBL > getSenderReceiverRelationship = userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FRIEND );
        Optional< UserRelationshipTBL > getReceiverSenderRelationship = userRelationshipCONT.fetch( receiver.get( ),sender.get( ), UserRelationshipTBL.TYPE_FRIEND );

        //Check if friend request exists
        if( getReceiverSenderRelationship.isEmpty( )  ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }


        if( getReceiverSenderRelationship.isPresent( ) ){

            if( Objects.equals( getReceiverSenderRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_ACCEPTED ) || Objects.equals( getReceiverSenderRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_BLOCKED ) ){
                this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
                this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
                this.isSuccessful = false;
                return;
            }

            //Accept friend request from sender
            getReceiverSenderRelationship.get( ).setStatus( UserRelationshipTBL.STATUS_ACCEPTED );
            getReceiverSenderRelationship.get( ).setDateupdated( LocalDateTime.now( ) );
            this.userRelationshipCONT.update( getReceiverSenderRelationship.get( ) );

            //Create a new record of the friend record if the sender receiver does not exist
            if( getSenderReceiverRelationship.isPresent( ) ){
                getSenderReceiverRelationship.get( ).setStatus( UserRelationshipTBL.STATUS_ACCEPTED );
                getSenderReceiverRelationship.get( ).setDateupdated( LocalDateTime.now( ) );
                this.userRelationshipCONT.update( getSenderReceiverRelationship.get( ) );
            }else{
                this.userRelationshipCONT.add( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FRIEND, UserRelationshipTBL.STATUS_ACCEPTED );
            }

        }else{
            this.jamiiErrorsMessagesRESP.setAcceptFriendRequest_GenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful && receiver.isPresent()){
            AcceptFriendRequestRESP acceptFriendRequestRESP = new AcceptFriendRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( acceptFriendRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }else{
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.receiver = Optional.empty( );
    }
}
