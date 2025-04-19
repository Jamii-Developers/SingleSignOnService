package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.UserBlockList;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRelationship;
import com.jamii.jamiidb.controllers.UserRequest;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.SearchUserServicesREQ;
import com.jamii.requests.userServices.socialREQ.SendFollowRequestServicesREQ;
import com.jamii.responses.userResponses.socialResponses.SendFollowRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class SendFollowRequestOPS extends AbstractUserServicesOPS {

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
        SendFollowRequestServicesREQ req = ( SendFollowRequestServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), SearchUserServicesREQ.class );
        setDeviceKey( req.getDeviceKey( ) );
        setUserKey( req.getUserKey( ) );
        setSessionKey( req.getSessionKey() );
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !getIsSuccessful() ){
            return;
        }

        SendFollowRequestServicesREQ req = ( SendFollowRequestServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), SearchUserServicesREQ.class );

        // Check if both users exist in the system
        this.userLogin.data = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON ).orElse( null );
        this.userLogin.otherUser = this.userLogin.fetchByUserKey( req.getReceiveruserkey( ), UserLogin.ACTIVE_ON ).orElse( null );
        if( this.userLogin.data == null  || this.userLogin.otherUser == null ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        //Fetch requests to user
        this.userRequest.dataList.addAll( userRequest.fetch( this.userLogin.data, this.userLogin.otherUser, UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE ) );
        this.userRequest.dataList.addAll( userRequest.fetch( this.userLogin.otherUser, this.userLogin.data, UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE ) );

        //Fetch Block List
        this.userBlockList.dataList.addAll( userBlockList.fetch( this.userLogin.data, this.userLogin.otherUser, UserBlockList.STATUS_ACTIVE ) );
        this.userBlockList.dataList.addAll( userBlockList.fetch( this.userLogin.otherUser, this.userLogin.data, UserBlockList.STATUS_ACTIVE ) );

        //Fetch Relationships
        this.userRelationship.dataList.addAll( userRelationship.fetch( this.userLogin.data, this.userLogin.otherUser, UserRelationship.TYPE_FOLLOW ) );
        this.userRelationship.dataList.addAll( userRelationship.fetch( this.userLogin.otherUser, this.userLogin.data, UserRelationship.TYPE_FOLLOW ) );


        //Check if user is already following this user
        if( !this.userRelationship.dataList.isEmpty() && this.userRelationship.dataList.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE) && x.getSenderid( ) == this.userLogin.data ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_AlreadyFollowingTheUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if the user has been blocked by the receiver
        if( !this.userBlockList.dataList.isEmpty() && this.userBlockList.dataList.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE ) && x.getUserid( ) == this.userLogin.otherUser) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_BlockedUserVagueResponse( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check is user has blocked this receiver
        if( !this.userBlockList.dataList.isEmpty() && this.userBlockList.dataList.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserBlockList.STATUS_ACTIVE ) && x.getBlockedid( ) == this.userLogin.data )  ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_YouHaveBlockedThisUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        if( Objects.equals( this.userLogin.otherUser.getPrivacy( ), UserLogin.PRIVACY_ON ) ){
            this.userRequest.data.setSenderid( this.userLogin.data );
            this.userRequest.data.setReceiverid( this.userLogin.otherUser);
            this.userRequest.data.setType( UserRelationship.TYPE_FOLLOW );
            this.userRequest.data.setStatus( UserRequest.STATUS_ACTIVE );
            this.userRequest.data.setDateupdated( LocalDateTime.now( ) );
            this.userRequest.save( );
        }else{
            this.userRelationship.data.setSenderid( this.userLogin.data );
            this.userRelationship.data.setReceiverid( this.userLogin.otherUser);
            this.userRelationship.data.setType( UserRelationship.TYPE_FOLLOW );
            this.userRelationship.data.setStatus( UserRelationship.STATUS_ACTIVE );
            this.userRelationship.data.setDateupdated( LocalDateTime.now( ) );
            this.userRelationship.save( );
        }

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( getIsSuccessful( ) ){
            SendFollowRequestRESP sendFollowRequestRESP = new SendFollowRequestRESP( UserRelationship.TYPE_FOLLOW, this.userLogin.otherUser);
            return  new ResponseEntity< >( sendFollowRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }

}
