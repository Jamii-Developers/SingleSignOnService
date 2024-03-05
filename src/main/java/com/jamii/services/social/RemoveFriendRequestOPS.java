package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.requests.social.RemoveFriendRequestREQ;
import com.jamii.responses.social.RemoveFriendRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class RemoveFriendRequestOPS extends socialAbstract{

    private RemoveFriendRequestREQ removeFriendRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;

    public void setRemoveFriendRequestREQ(RemoveFriendRequestREQ removeFriendRequestREQ) {
        this.removeFriendRequestREQ = removeFriendRequestREQ;
    }

    public RemoveFriendRequestREQ getRemoveFriendRequestREQ() {
        return removeFriendRequestREQ;
    }

    @Override
    public void processRequest( ) throws Exception{

        DeviceKey = getRemoveFriendRequestREQ( ).getDevicekey( );
        UserKey = getRemoveFriendRequestREQ( ).getUserkey( );

        super.processRequest( );

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetch( getRemoveFriendRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setRemoveFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Fetch the friend request
        Optional<UserRelationshipTBL> getReceiverSenderRelationship = this.userRelationshipCONT.fetch( receiver.get( ),sender.get( ), UserRelationshipTBL.TYPE_FRIEND );

        //Check if friend request exists
        if( getReceiverSenderRelationship.isPresent( ) && Objects.equals( getReceiverSenderRelationship.get( ).getStatus( ), UserRelationshipTBL.STATUS_PENDING ) ){

            getReceiverSenderRelationship.get( ).setStatus( UserRelationshipTBL.STATUS_NO_RELATIONSHIP );
            getReceiverSenderRelationship.get( ).setDateupdated( LocalDateTime.now( ) );
            this.userRelationshipCONT.update( getReceiverSenderRelationship.get( ) );

        }else{
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful && receiver.isPresent( ) ){
            RemoveFriendRequestRESP removeFollowRequestRESP = new RemoveFriendRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( removeFollowRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }else{
            this.jamiiErrorsMessagesRESP.setRemoveFriendRequestOPS_GenerateGenericError( );
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
