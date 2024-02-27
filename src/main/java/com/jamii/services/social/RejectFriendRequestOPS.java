package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.requests.social.RejectFriendRequestREQ;
import com.jamii.responses.social.RejectFriendRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class RejectFriendRequestOPS extends socialAbstract{

    private RejectFriendRequestREQ rejectFriendRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;

    public void setRejectFriendRequestREQ( RejectFriendRequestREQ rejectFriendRequestREQ ) {
        this.rejectFriendRequestREQ = rejectFriendRequestREQ;
    }

    public RejectFriendRequestREQ getRejectFriendRequestREQ() {
        return rejectFriendRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

        DeviceKey = getRejectFriendRequestREQ( ).getDevicekey( );
        UserKey = getRejectFriendRequestREQ( ).getUserkey( );

        super.processRequest( );

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetch( getRejectFriendRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setRejectFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
        }

        //Fetch the friend request
        Optional<UserRelationshipTBL> getSenderReceiverRelationship = this.userRelationshipCONT.fetch( receiver.get( ), sender.get( ), UserRelationshipTBL.TYPE_FRIEND );

        //Check if friend request exists
        if( getSenderReceiverRelationship.isPresent( ) && Objects.equals( getSenderReceiverRelationship.get().getStatus(), UserRelationshipTBL.STATUS_PENDING) ){

            getSenderReceiverRelationship.get( ).setStatus( UserRelationshipTBL.STATUS_REJECTED );
            getSenderReceiverRelationship.get( ).setDateupdated( LocalDateTime.now( ) );
            this.userRelationshipCONT.update( getSenderReceiverRelationship.get( ) );
            this.isSuccessful = true;
        }else{
            this.jamiiErrorsMessagesRESP.setRejectFriendRequestOPS_FriendRequestNoLongerExists( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
        }
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){
            RejectFriendRequestRESP rejectFriendRequestRESP = new RejectFriendRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( rejectFriendRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.receiver = Optional.empty( );
    }
}
