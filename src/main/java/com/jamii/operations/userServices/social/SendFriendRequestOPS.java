package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.UserBlockList;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRelationship;
import com.jamii.jamiidb.controllers.UserRequest;
import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.SendFriendRequestServicesREQ;
import com.jamii.responses.userResponses.socialResponses.SendFriendRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SendFriendRequestOPS extends AbstractUserServicesOPS {

    private SendFriendRequestServicesREQ sendFriendRequestREQ;
    private Optional< UserLoginTBL > receiver;


    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserRelationship userRelationship;
    @Autowired
    private UserRequest userRequest;
    @Autowired
    private UserBlockList userBlockList;

    @Override
    public void validateCookie( ) throws Exception{
        SendFriendRequestServicesREQ req = (SendFriendRequestServicesREQ) JamiiMapperUtils.mapObject( getRequest( ), SendFriendRequestServicesREQ.class );
        setDeviceKey( req.getDeviceKey( ) );
        setUserKey( req.getUserKey( ) );
        setSessionKey( req.getSessionKey() );
        super.validateCookie( );
    }

    @Override
    public void processRequest( ) throws Exception {

        if( !getIsSuccessful( ) ){
            return;
        }

        SendFriendRequestServicesREQ req = (SendFriendRequestServicesREQ) JamiiMapperUtils.mapObject( getRequest( ), SendFriendRequestServicesREQ.class );

        Optional<UserLoginTBL> sender = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON );
        receiver = this.userLogin.fetchByUserKey( req.getReceiveruserkey(), UserLogin.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        //Fetch requests to user
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequest.fetch( sender.get( ), receiver.get( ), UserRequest.TYPE_FRIEND ) );
        requests.addAll( userRequest.fetch( receiver.get( ), sender.get( ), UserRequest.TYPE_FRIEND ) );
        //Fetch Block List
        List<UserBlockListTBL> blockList = new ArrayList<>( );
        blockList.addAll( userBlockList.fetch( sender.get( ), receiver.get( ), UserBlockList.STATUS_ACTIVE ) );
        blockList.addAll( userBlockList.fetch( receiver.get( ), sender.get( ), UserBlockList.STATUS_ACTIVE ) );
        //Fetch Relationships
        List<UserRelationshipTBL> relationship = new ArrayList<>( );
        relationship.addAll( userRelationship.fetch( sender.get( ), receiver.get( ), UserRelationship.TYPE_FRIEND ) );
        relationship.addAll( userRelationship.fetch(  receiver.get( ), sender.get( ), UserRelationship.TYPE_FRIEND ) );

        //Check if a friend request has been sent to the receiver
        if( !requests.isEmpty() && requests.stream( ).anyMatch( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE ) && x.getSenderid( ) == sender.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_FriendRequestIsAlreadyAvailable( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }

        //Check if a friend request has already been sent by the receiver
        if( !requests.isEmpty() && requests.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE) && x.getSenderid( ) == receiver.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_FriendRequestHasBeenSentByTheReceiver( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }

        //Check if the users are already friends
        if( !relationship.isEmpty( ) && relationship.stream( ).anyMatch( x -> Objects.equals( x.getStatus(), UserRelationship.STATUS_ACTIVE ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_AreAlreadyFriends( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }

        //Check if sender has been blocked
        if( !blockList.isEmpty( ) && blockList.stream( ).anyMatch( x -> Objects.equals( x.getStatus( ), UserBlockList.STATUS_ACTIVE ) && x.getUserid( ) == receiver.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_BlockedUserVagueResponse( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }

        //Check is sender blocked this receiver
        if( !blockList.isEmpty( ) && blockList.stream( ).anyMatch( x -> Objects.equals( x.getStatus( ), UserBlockList.STATUS_ACTIVE ) && x.getUserid( ) == sender.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_YouHaveBlockedThisUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }

        userRequest.add( sender.get( ) , receiver.get( ), UserRelationship.TYPE_FRIEND, UserRelationship.STATUS_ACTIVE );
        setIsSuccessful( true );
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( getIsSuccessful( ) ){
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
