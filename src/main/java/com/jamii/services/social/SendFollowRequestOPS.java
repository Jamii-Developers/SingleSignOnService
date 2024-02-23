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

import java.util.Optional;

@Service
public class SendFollowRequestOPS extends socialAbstract{

    private SendFollowRequestREQ sendFollowRequestREQ;

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
        Optional<UserLoginTBL> receiver = this.userLoginCONT.fetch( getSendFollowRequestREQ( ).getReceiveruserkey(), UserLoginTBL.ACTIVE_ON );

        //Check if a follow request has been sent to the receiver
        Optional<UserRelationshipTBL> checkIfTheresAPendingRequestFromTheSender = userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW, UserRelationshipTBL.STATUS_ACCEPTED );
        if( checkIfTheresAPendingRequestFromTheSender.isPresent( ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_AlreadyFollowingTheUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if sender has been blocked
        Optional< UserRelationshipTBL > checkIfSenderIsBlocked = userRelationshipCONT.fetch( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW, UserRelationshipTBL.STATUS_BLOCKED );
        if( checkIfSenderIsBlocked.isPresent( ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_BlockedUserVagueResponse( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check is sender blocked this receiver
        Optional< UserRelationshipTBL > checkIfReceiverHasBeenBlocked = userRelationshipCONT.fetch( receiver.get( ), sender.get( ),  UserRelationshipTBL.TYPE_FOLLOW, UserRelationshipTBL.STATUS_BLOCKED );
        if( checkIfReceiverHasBeenBlocked.isPresent( ) ){
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_YouHaveBlockedThisUser( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        userRelationshipCONT.add( sender.get( ) , receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW, UserRelationshipTBL.STATUS_ACCEPTED );

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){
            SendFollowRequestRESP sendFollowRequestRESP = new SendFollowRequestRESP( );
            return  new ResponseEntity< >( sendFollowRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }
}
