package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.requests.social.SendFollowRequestREQ;
import com.jamii.responses.social.SendFollowRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class SendFollowRequestOPS extends socialAbstract{

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

    @Override
    public void processRequest() throws Exception {

        DeviceKey = getSendFollowRequestREQ( ).getDevicekey( );
        UserKey = getSendFollowRequestREQ( ).getUserkey( );

        super.processRequest( );

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetch( getSendFollowRequestREQ( ).getReceiveruserkey(), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        Optional< UserRelationshipTBL > getSenderReceiverRelationship = userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW );
        Optional< UserRelationshipTBL > getReceiverSenderRelationship = userRelationshipCONT.fetch( receiver.get( ), sender.get( ), UserRelationshipTBL.TYPE_FOLLOW );

        //Check if there's a pending Sender request
        if( getSenderReceiverRelationship.isPresent( ) && Objects.equals( getSenderReceiverRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_PENDING ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_PendingFollowRequest( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if user is already following this user
        if( getSenderReceiverRelationship.isPresent( ) && Objects.equals( getSenderReceiverRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_ACCEPTED ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_AlreadyFollowingTheUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if sender has been blocked
        if( getReceiverSenderRelationship.isPresent( ) && Objects.equals(getReceiverSenderRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_BLOCKED)){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_BlockedUserVagueResponse( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check is sender blocked this receiver
        if( getSenderReceiverRelationship.isPresent( ) &&  Objects.equals( getSenderReceiverRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_BLOCKED ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_YouHaveBlockedThisUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        if( Objects.equals( receiver.get( ).getPrivacy( ), UserLoginTBL.PRIVACY_ON ) ){
            userRelationshipCONT.add( sender.get( ) , receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW, UserRelationshipTBL.STATUS_PENDING );
            followRequestType = 1 ;
        }else{

            //Check if sender already has a no relationship follow request
            if( getReceiverSenderRelationship.isPresent( ) && Objects.equals( getReceiverSenderRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_NO_RELATIONSHIP ) ){
                getReceiverSenderRelationship.get( ).setStatus( UserRelationshipTBL.STATUS_PENDING );
                getReceiverSenderRelationship.get( ).setDateupdated( LocalDateTime.now( ) );
                this.userRelationshipCONT.update( getReceiverSenderRelationship.get( ) );
                return;
            }

            userRelationshipCONT.add( sender.get( ) , receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW, UserRelationshipTBL.STATUS_ACCEPTED );
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
