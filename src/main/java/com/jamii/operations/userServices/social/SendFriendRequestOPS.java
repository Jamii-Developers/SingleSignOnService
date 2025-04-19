package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.UserBlockList;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRelationship;
import com.jamii.jamiidb.controllers.UserRequest;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.SendFriendRequestServicesREQ;
import com.jamii.responses.userResponses.socialResponses.SendFriendRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class SendFriendRequestOPS extends AbstractUserServicesOPS {

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

        // Check if both users exist in the system
        this.userLogin.data = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON ).orElse( null );
        this.userLogin.otherUser = this.userLogin.fetchByUserKey( req.getReceiveruserkey( ), UserLogin.ACTIVE_ON ).orElse( null );
        if( this.userLogin.data == null  || this.userLogin.otherUser == null ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        //Fetch requests to user
        this.userRequest.dataList.addAll( userRequest.fetch( this.userLogin.data , this.userLogin.otherUser , UserRequest.TYPE_FRIEND ) );
        this.userRequest.dataList.addAll( userRequest.fetch( this.userLogin.otherUser , this.userLogin.data , UserRequest.TYPE_FRIEND ) );

        //Fetch Block List
        this.userBlockList.dataList.addAll( userBlockList.fetch( this.userLogin.data , this.userLogin.otherUser, UserBlockList.STATUS_ACTIVE ) );
        this.userBlockList.dataList.addAll( userBlockList.fetch( this.userLogin.otherUser , this.userLogin.data , UserBlockList.STATUS_ACTIVE ) );

        //Fetch Relationships
        this.userRelationship.dataList.addAll( userRelationship.fetch( this.userLogin.data , this.userLogin.otherUser, UserRelationship.TYPE_FRIEND ) );
        this.userRelationship.dataList.addAll( userRelationship.fetch( this.userLogin.otherUser , this.userLogin.data , UserRelationship.TYPE_FRIEND ) );

        //Check if a friend request has been by the user sent to the receiver
        if( !this.userRequest.dataList.isEmpty() && this.userRequest.dataList.stream( ).anyMatch( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE ) && x.getSenderid( ) == this.userLogin.data ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_FriendRequestIsAlreadyAvailable( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }

        //Check if a friend request has already been sent by the receiver
        if( !this.userRequest.dataList.isEmpty() && this.userRequest.dataList.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE) && x.getSenderid( ) == this.userLogin.otherUser ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_FriendRequestHasBeenSentByTheReceiver( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }

        //Check if the users are already friends
        if( !this.userRelationship.dataList.isEmpty( ) && this.userRelationship.dataList.stream( ).anyMatch( x -> Objects.equals( x.getStatus(), UserRelationship.STATUS_ACTIVE ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_AreAlreadyFriends( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }

        //Check if sender has been blocked
        if( !this.userBlockList.dataList.isEmpty( ) && this.userBlockList.dataList.stream( ).anyMatch( x -> Objects.equals( x.getStatus( ), UserBlockList.STATUS_ACTIVE ) && x.getUserid( ) == this.userLogin.otherUser ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_BlockedUserVagueResponse( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }

        //Check is sender blocked this receiver
        if( !this.userBlockList.dataList.isEmpty( ) && this.userBlockList.dataList.stream( ).anyMatch( x -> Objects.equals( x.getStatus( ), UserBlockList.STATUS_ACTIVE ) && x.getUserid( ) == this.userLogin.data ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_YouHaveBlockedThisUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }

        // Create relationship
        this.userRequest.data.setSenderid( this.userLogin.data );
        this.userRequest.data.setReceiverid( this.userLogin.otherUser );
        this.userRequest.data.setType( UserRelationship.TYPE_FRIEND );
        this.userRequest.data.setStatus( UserRelationship.STATUS_ACTIVE );
        this.userRequest.data.setDateupdated( LocalDateTime.now( ) );
        this.userRequest.save( );

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( getIsSuccessful( ) ){
            SendFriendRequestRESP sendFriendRequestRESP = new SendFriendRequestRESP( this.userLogin.otherUser );
            return  new ResponseEntity< >( sendFriendRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }

}
