package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.requests.social.BlockFollowRequestREQ;
import com.jamii.responses.social.BlockFollowRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BlockFollowRequestOPS extends socialAbstract{

    private BlockFollowRequestREQ blockFollowRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;

    public void setBlockFollowRequestREQ(BlockFollowRequestREQ blockFollowRequestREQ) {
        this.blockFollowRequestREQ = blockFollowRequestREQ;
    }

    public BlockFollowRequestREQ getBlockFollowRequestREQ() {
        return blockFollowRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

        DeviceKey = getBlockFollowRequestREQ( ).getDevicekey( );
        UserKey = getBlockFollowRequestREQ( ).getUserkey( );

        super.processRequest( );

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetch( getBlockFollowRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setBlockFollowRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
        }

        //Fetch the friend requests
        Optional<UserRelationshipTBL> getSenderReceiverRelationshipFriend = this.userRelationshipCONT.fetch( receiver.get( ), sender.get( ), UserRelationshipTBL.TYPE_FRIEND );
        Optional<UserRelationshipTBL> getReceiverSenderRelationshipFriend = this.userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FRIEND );
        //Fetch the follow request
        Optional<UserRelationshipTBL> getSenderReceiverRelationshipFollow = this.userRelationshipCONT.fetch( receiver.get( ), sender.get( ), UserRelationshipTBL.TYPE_FOLLOW );
        Optional<UserRelationshipTBL> getReceiverSenderRelationshipFollow = this.userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW );

        //Block any friend relationships that the sender has with the receiver
        if( getReceiverSenderRelationshipFriend.isPresent( ) ){
            getReceiverSenderRelationshipFriend.get( ).setStatus( UserRelationshipTBL.STATUS_BLOCKED );
            getReceiverSenderRelationshipFriend.get( ).setDateupdated( LocalDateTime.now( ) );
            userRelationshipCONT.update( getReceiverSenderRelationshipFriend.get( ) );
        }else{
            userRelationshipCONT.add( receiver.get( ), sender.get( ),UserRelationshipTBL.TYPE_FRIEND, UserRelationshipTBL.STATUS_BLOCKED );
        }

        //Set any friend relationships the receiver has with the sender
        if( getSenderReceiverRelationshipFriend.isPresent( ) ){
            getSenderReceiverRelationshipFriend.get( ).setStatus( UserRelationshipTBL.STATUS_NO_RELATIONSHIP );
            getSenderReceiverRelationshipFriend.get( ).setDateupdated( LocalDateTime.now( ) );
            userRelationshipCONT.update( getSenderReceiverRelationshipFriend.get( ) );
        }else{
            userRelationshipCONT.add( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FRIEND, UserRelationshipTBL.STATUS_NO_RELATIONSHIP );
        }

        //Block any follow relationships that the sender has with the receiver
        if( getReceiverSenderRelationshipFollow.isPresent( ) ){
            getReceiverSenderRelationshipFollow.get( ).setStatus( UserRelationshipTBL.STATUS_BLOCKED );
            getReceiverSenderRelationshipFollow.get( ).setDateupdated( LocalDateTime.now( ) );
            userRelationshipCONT.update( getReceiverSenderRelationshipFollow.get( ) );
        }else{
            userRelationshipCONT.add(  receiver.get( ), sender.get( ), UserRelationshipTBL.TYPE_FOLLOW, UserRelationshipTBL.STATUS_BLOCKED );
        }

        //Set any follow relationships the receiver has with the sender
        if( getSenderReceiverRelationshipFollow.isPresent( ) ){
            getSenderReceiverRelationshipFollow.get( ).setStatus( UserRelationshipTBL.STATUS_NO_RELATIONSHIP );
            getSenderReceiverRelationshipFollow.get( ).setDateupdated( LocalDateTime.now( ) );
            userRelationshipCONT.update( getSenderReceiverRelationshipFollow.get( ) );
        }else{
            userRelationshipCONT.add(   sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW, UserRelationshipTBL.STATUS_NO_RELATIONSHIP );
        }

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful && receiver.isPresent( ) ){
            BlockFollowRequestRESP blockFollowRequestRESP = new BlockFollowRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( blockFollowRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }else{
            this.jamiiErrorsMessagesRESP.setBlockFollowRequestOPS_GenerateGenericError( );
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
