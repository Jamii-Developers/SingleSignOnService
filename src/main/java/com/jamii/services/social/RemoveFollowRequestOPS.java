package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.requests.social.RemoveFollowRequestREQ;
import com.jamii.responses.social.RemoveFollowRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class RemoveFollowRequestOPS extends socialAbstract{

    private RemoveFollowRequestREQ removeFollowRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;

    public void setRemoveFollowRequestREQ(RemoveFollowRequestREQ removeFollowRequestREQ) {
        this.removeFollowRequestREQ = removeFollowRequestREQ;
    }

    public RemoveFollowRequestREQ getRemoveFollowRequestREQ() {
        return removeFollowRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

        DeviceKey = getRemoveFollowRequestREQ( ).getDevicekey( );
        UserKey = getRemoveFollowRequestREQ( ).getUserkey( );

        super.processRequest( );

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetch( getRemoveFollowRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setRemoveFollowRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Fetch the follow request
        Optional<UserRelationshipTBL> getSenderReceiverRelationship = this.userRelationshipCONT.fetch( sender.get( ),receiver.get( ), UserRelationshipTBL.TYPE_FOLLOW );

        //Check if follow request exists
        if( getSenderReceiverRelationship.isPresent( ) && Objects.equals( getSenderReceiverRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_PENDING ) ){

            getSenderReceiverRelationship.get( ).setStatus( UserRelationshipTBL.STATUS_NO_RELATIONSHIP );
            getSenderReceiverRelationship.get( ).setDateupdated( LocalDateTime.now( ) );
            this.userRelationshipCONT.update( getSenderReceiverRelationship.get( ) );
        }else{
            this.jamiiErrorsMessagesRESP.setRemoveFollowRequestOPS_FollowRequestNoLongerExists( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
        }

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful && receiver.isPresent( ) ){
            RemoveFollowRequestRESP removeFollowRequestRESP = new RemoveFollowRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( removeFollowRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }else{
            this.jamiiErrorsMessagesRESP.setRemoveFollowRequestOPS_GenerateGenericError( );
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
