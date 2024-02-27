package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.requests.social.AcceptFollowRequestREQ;
import com.jamii.responses.social.AcceptFollowRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AcceptFollowRequestOPS extends socialAbstract{

    private AcceptFollowRequestREQ acceptFollowRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;

    public void setAcceptFollowRequestREQ(AcceptFollowRequestREQ acceptFollowRequestREQ) {
        this.acceptFollowRequestREQ = acceptFollowRequestREQ;
    }

    public AcceptFollowRequestREQ getAcceptFollowRequestREQ() {
        return acceptFollowRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

        DeviceKey = getAcceptFollowRequestREQ( ).getDevicekey( );
        UserKey = getAcceptFollowRequestREQ( ).getUserkey( );

        super.processRequest( );

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetch( getAcceptFollowRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        Optional< UserRelationshipTBL > getReceiverSenderRelationship = userRelationshipCONT.fetch( receiver.get( ),sender.get( ), UserRelationshipTBL.TYPE_FOLLOW );

        //Check if follow request exists
        if( getReceiverSenderRelationship.isPresent( ) ){

            //Accept follow request from sender
            getReceiverSenderRelationship.get( ).setStatus( UserRelationshipTBL.STATUS_ACCEPTED );
            getReceiverSenderRelationship.get( ).setDateupdated( LocalDateTime.now( ) );
            this.userRelationshipCONT.update( getReceiverSenderRelationship.get( ) );

            this.isSuccessful = true;

        }else{
            this.jamiiErrorsMessagesRESP.setAcceptFriendRequest_GenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){
            AcceptFollowRequestRESP acceptFollowRequestRESP = new AcceptFollowRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( acceptFollowRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.receiver = Optional.empty( );
    }
}
