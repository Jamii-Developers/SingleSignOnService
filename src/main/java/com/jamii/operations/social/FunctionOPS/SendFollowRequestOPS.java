package com.jamii.operations.social.FunctionOPS;

import com.jamii.jamiidb.controllers.UserBlockListCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.controllers.UserRequestCONT;
import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.requests.social.FunctionREQ.SendFollowRequestREQ;
import com.jamii.responses.social.FunctionRESP.SendFollowRequestRESP;
import com.jamii.operations.social.AbstractSocial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SendFollowRequestOPS extends AbstractSocial {

    private SendFollowRequestREQ sendFollowRequestREQ;
    private Integer followRequestType = 0 ;
    private Optional<UserLoginTBL> receiver;

    public void setSendFollowRequestREQ(SendFollowRequestREQ sendFollowRequestREQ) {
        this.sendFollowRequestREQ = sendFollowRequestREQ;
    }

    public SendFollowRequestREQ getSendFollowRequestREQ() {
        return sendFollowRequestREQ;
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
    public void validateCookie( ) throws Exception{
        DeviceKey = getSendFollowRequestREQ( ).getDevicekey( );
        UserKey = getSendFollowRequestREQ( ).getUserkey( );
        SessionKey = getSendFollowRequestREQ().getSessionkey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetchByUserKey( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetchByUserKey( getSendFollowRequestREQ( ).getReceiveruserkey(), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        //Fetch requests to user
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequestCONT.fetch( sender.get( ), receiver.get( ), UserRequestsTBL.TYPE_FOLLOW, UserRequestsTBL.STATUS_ACTIVE ) );
        requests.addAll( userRequestCONT.fetch( receiver.get( ), sender.get( ), UserRequestsTBL.TYPE_FOLLOW, UserRequestsTBL.STATUS_ACTIVE ) );
        //Fetch Block List
        List<UserBlockListTBL> blockList = new ArrayList<>( );
        blockList.addAll( userBlockListCONT.fetch( sender.get( ), receiver.get( ), UserBlockListTBL.STATUS_ACTIVE ) );
        blockList.addAll( userBlockListCONT.fetch( receiver.get( ), sender.get( ), UserBlockListTBL.STATUS_ACTIVE ) );
        //Fetch Relationships
        List<UserRelationshipTBL> relationship = new ArrayList<>( );
        relationship.addAll( userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW ) );
        relationship.addAll( userRelationshipCONT.fetch(  receiver.get( ), sender.get( ),UserRelationshipTBL.TYPE_FOLLOW ) );

        //Check if there's a pending Sender request
        if( !requests.isEmpty() && requests.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserRequestsTBL.STATUS_ACTIVE ) && x.getSenderid( ) == sender.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_PendingFollowRequest( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if user is already following this user
        if( !relationship.isEmpty() && relationship.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserRelationshipTBL.STATUS_ACTIVE) && x.getSenderid( ) == sender.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_AlreadyFollowingTheUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if sender has been blocked
        if( !blockList.isEmpty() && blockList.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserBlockListTBL.STATUS_ACTIVE ) && x.getBlockedid( ) == sender.get( ) ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_BlockedUserVagueResponse( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check is sender blocked this receiver
        if( !blockList.isEmpty() && blockList.stream().anyMatch( x -> Objects.equals( x.getStatus(), UserBlockListTBL.STATUS_ACTIVE ) && x.getBlockedid( ) == receiver.get( ) )  ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_YouHaveBlockedThisUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        if( Objects.equals( receiver.get( ).getPrivacy( ), UserLoginTBL.PRIVACY_ON ) ){
            userRequestCONT.add( sender.get( ) , receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW, UserRequestsTBL.STATUS_ACTIVE );
            followRequestType = 1 ;
        }else{
            userRelationshipCONT.add( sender.get( ) , receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW, UserRelationshipTBL.STATUS_ACTIVE );
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
