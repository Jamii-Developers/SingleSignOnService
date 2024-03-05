package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.requests.social.BlockFriendRequestREQ;
import com.jamii.responses.social.BlockFriendRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BlockFriendRequestOPS extends socialAbstract{

    private BlockFriendRequestREQ blockFriendRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;

    public void setBlockFriendRequestREQ(BlockFriendRequestREQ blockFriendRequestREQ) {
        this.blockFriendRequestREQ = blockFriendRequestREQ;
    }

    public BlockFriendRequestREQ getBlockFriendRequestREQ() {
        return blockFriendRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

        DeviceKey = getBlockFriendRequestREQ( ).getDevicekey( );
        UserKey = getBlockFriendRequestREQ( ).getUserkey( );

        super.processRequest( );

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetch( getBlockFriendRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setBlockFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Fetch the friend requests
        Optional<UserRelationshipTBL> getSenderReceiverRelationshipFriend = this.userRelationshipCONT.fetch( receiver.get( ), sender.get( ), UserRelationshipTBL.TYPE_FRIEND );
        Optional<UserRelationshipTBL> getReceiverSenderRelationshipFriend = this.userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FRIEND );
        //Fetch the follow request
        Optional<UserRelationshipTBL> getSenderReceiverRelationshipFollow = this.userRelationshipCONT.fetch( receiver.get( ), sender.get( ), UserRelationshipTBL.TYPE_FOLLOW );
        Optional<UserRelationshipTBL> getReceiverSenderRelationshipFollow = this.userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW );

        //Block any friend relationships that the sender has with the receiver
        if( getSenderReceiverRelationshipFriend.isPresent( ) ){
            getSenderReceiverRelationshipFriend.get( ).setStatus( UserRelationshipTBL.STATUS_BLOCKED );
            getSenderReceiverRelationshipFriend.get( ).setDateupdated( LocalDateTime.now( ) );
            userRelationshipCONT.update( getSenderReceiverRelationshipFriend.get( ) );
        }

        //Set any friend relationships the receiver has with the sender
        if( getReceiverSenderRelationshipFriend.isPresent( ) ){
            getReceiverSenderRelationshipFriend.get( ).setStatus( UserRelationshipTBL.STATUS_NO_RELATIONSHIP );
            getReceiverSenderRelationshipFriend.get( ).setDateupdated( LocalDateTime.now( ) );
            userRelationshipCONT.update( getReceiverSenderRelationshipFriend.get( ) );
        }

        //Block any follow relationships that the sender has with the receiver
        if( getSenderReceiverRelationshipFollow.isPresent( ) ){
            getSenderReceiverRelationshipFollow.get( ).setStatus( UserRelationshipTBL.STATUS_BLOCKED );
            getSenderReceiverRelationshipFollow.get( ).setDateupdated( LocalDateTime.now( ) );
            userRelationshipCONT.update( getSenderReceiverRelationshipFollow.get( ) );
        }

        //Set any follow relationships the receiver has with the sender
        if( getReceiverSenderRelationshipFollow.isPresent( ) ){
            getReceiverSenderRelationshipFollow.get( ).setStatus( UserRelationshipTBL.STATUS_NO_RELATIONSHIP );
            getReceiverSenderRelationshipFollow.get( ).setDateupdated( LocalDateTime.now( ) );
            userRelationshipCONT.update( getReceiverSenderRelationshipFollow.get( ) );
        }

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful && receiver.isPresent( ) ){
            BlockFriendRequestRESP blockFriendRequestRESP = new BlockFriendRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( blockFriendRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }else{
            this.jamiiErrorsMessagesRESP.setBlockFriendRequestOPS_GenerateGenericError( );
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
