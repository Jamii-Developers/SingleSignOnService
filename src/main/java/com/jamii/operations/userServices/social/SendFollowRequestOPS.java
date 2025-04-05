package com.jamii.operations.userServices.social;

import com.jamii.jamiidb.controllers.UserBlockList;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRelationship;
import com.jamii.jamiidb.controllers.UserRequest;
import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.SendFollowRequestServicesREQ;
import com.jamii.responses.userResponses.socialResponses.SendFollowRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SendFollowRequestOPS extends AbstractUserServicesOPS {

    private SendFollowRequestServicesREQ sendFollowRequestREQ;
    private Integer followRequestType = 0 ;
    private Optional<UserLoginTBL> receiver;

    public void setSendFollowRequestREQ(SendFollowRequestServicesREQ sendFollowRequestREQ) {
        this.sendFollowRequestREQ = sendFollowRequestREQ;
    }

    public SendFollowRequestServicesREQ getSendFollowRequestREQ() {
        return sendFollowRequestREQ;
    }

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
        DeviceKey = getSendFollowRequestREQ( ).getDeviceKey( );
        UserKey = getSendFollowRequestREQ( ).getUserKey( );
        SessionKey = getSendFollowRequestREQ().getSessionKey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON );
        receiver = this.userLogin.fetchByUserKey( getSendFollowRequestREQ( ).getReceiveruserkey(), UserLogin.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        //Fetch requests to user
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequest.fetch( sender.get( ), receiver.get( ), UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE ) );
        requests.addAll( userRequest.fetch( receiver.get( ), sender.get( ), UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE ) );
        //Fetch Block List
        List<UserBlockListTBL> blockList = new ArrayList<>( );
        blockList.addAll( userBlockList.fetch( sender.get( ), receiver.get( ), UserBlockList.STATUS_ACTIVE ) );
        blockList.addAll( userBlockList.fetch( receiver.get( ), sender.get( ), UserBlockList.STATUS_ACTIVE ) );
        //Fetch Relationships
        List<UserRelationshipTBL> relationship = new ArrayList<>( );
        relationship.addAll( userRelationship.fetch( sender.get( ), receiver.get( ), UserRelationship.TYPE_FOLLOW ) );
        relationship.addAll( userRelationship.fetch(  receiver.get( ), sender.get( ), UserRelationship.TYPE_FOLLOW ) );

        //Check if there's a pending Sender request
        if( !requests.isEmpty() && requests.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE ) && x.getSenderid( ) == sender.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_PendingFollowRequest( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if user is already following this user
        if( !relationship.isEmpty() && relationship.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE) && x.getSenderid( ) == sender.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_AlreadyFollowingTheUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if sender has been blocked
        if( !blockList.isEmpty() && blockList.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE ) && x.getBlockedid( ) == sender.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_BlockedUserVagueResponse( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check is sender blocked this receiver
        if( !blockList.isEmpty() && blockList.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserBlockList.STATUS_ACTIVE ) && x.getBlockedid( ) == receiver.get( ) )  ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_YouHaveBlockedThisUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        if( Objects.equals( receiver.get( ).getPrivacy( ), UserLogin.PRIVACY_ON ) ){
            userRequest.add( sender.get( ) , receiver.get( ), UserRelationship.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE );
            followRequestType = 1 ;
        }else{
            userRelationship.add( sender.get( ) , receiver.get( ), UserRelationship.TYPE_FOLLOW, UserRelationship.STATUS_ACTIVE );
        }

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){
            SendFollowRequestRESP sendFollowRequestRESP = new SendFollowRequestRESP( followRequestType, receiver.get( ) );
            return  new ResponseEntity< >( sendFollowRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.receiver = Optional.empty( );
    }
}
