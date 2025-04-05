package com.jamii.operations.userServices.social;

import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRelationship;
import com.jamii.jamiidb.controllers.UserRequest;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.AcceptFollowRequestServicesREQ;
import com.jamii.responses.userResponses.socialResponses.AcceptFollowRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AcceptFollowRequestOPS extends AbstractUserServicesOPS {

    private AcceptFollowRequestServicesREQ acceptFollowRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserRelationship userRelationship;
    @Autowired
    private UserRequest userRequest;

    public void setAcceptFollowRequestREQ(AcceptFollowRequestServicesREQ acceptFollowRequestREQ) {
        this.acceptFollowRequestREQ = acceptFollowRequestREQ;
    }

    public AcceptFollowRequestServicesREQ getAcceptFollowRequestREQ() {
        return acceptFollowRequestREQ;
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getAcceptFollowRequestREQ( ).getDeviceKey( );
        UserKey = getAcceptFollowRequestREQ( ).getUserKey( );
        SessionKey = getAcceptFollowRequestREQ().getSessionKey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON );
        receiver = this.userLogin.fetchByUserKey( getAcceptFollowRequestREQ( ).getReceiveruserkey( ), UserLogin.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Fetch requests to user
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequest.fetch( sender.get( ), receiver.get( ), UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE ) );
        requests.addAll( userRequest.fetch( receiver.get( ), sender.get( ), UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE ) );

        Optional <UserRequestsTBL> validFollowRequest = requests.stream().filter( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE) && x.getReceiverid( ) == sender.get( ) ).findFirst();
        //Check if follow request exists
        if( validFollowRequest.isPresent( ) ){

            // Deactivate the request
            validFollowRequest.get( ).setStatus( UserRequest.STATUS_INACTIVE );
            validFollowRequest.get( ).setDateupdated( LocalDateTime.now( ) );
            userRequest.update( validFollowRequest.get( ) );

            //Accept follow request from sender
            this.userRelationship.add( receiver.get( ), sender.get( ), UserRelationship.TYPE_FOLLOW, UserRelationship.STATUS_ACTIVE );

        }else{
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful && receiver.isPresent( ) ){
            AcceptFollowRequestRESP acceptFollowRequestRESP = new AcceptFollowRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( acceptFollowRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }else{
            this.jamiiErrorsMessagesRESP.setAcceptFollowRequest_GenerateGenericError( );
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
