package com.jamii.operations.userServices.social;

import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRequest;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.RejectFriendRequestServicesREQ;
import com.jamii.responses.userResponses.socialResponses.RejectFriendRequestRESP;
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
public class RejectFriendRequestOPS extends AbstractUserServicesOPS {

    private RejectFriendRequestServicesREQ rejectFriendRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserRequest userRequest;

    public void setRejectFriendRequestREQ( RejectFriendRequestServicesREQ rejectFriendRequestREQ ) {
        this.rejectFriendRequestREQ = rejectFriendRequestREQ;
    }

    public RejectFriendRequestServicesREQ getRejectFriendRequestREQ() {
        return rejectFriendRequestREQ;
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getRejectFriendRequestREQ( ).getDeviceKey( );
        UserKey = getRejectFriendRequestREQ( ).getUserKey( );
        SessionKey = getRejectFriendRequestREQ().getSessionKey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON );
        receiver = this.userLogin.fetchByUserKey( getRejectFriendRequestREQ( ).getReceiveruserkey( ), UserLogin.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setRejectFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Fetch requests to user
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequest.fetch( sender.get( ), receiver.get( ), UserRequest.TYPE_FRIEND, UserRequest.STATUS_ACTIVE ) );
        requests.addAll( userRequest.fetch( receiver.get( ), sender.get( ), UserRequest.TYPE_FRIEND, UserRequest.STATUS_ACTIVE ) );

        Optional <UserRequestsTBL> validFriendRequest = requests.stream().filter( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE ) && x.getReceiverid( ) == sender.get( ) ).findFirst( );

        //Check if friend request exists
        if( validFriendRequest.isPresent( ) ){

            // Deactivate the request
            validFriendRequest.get( ).setStatus( UserRequest.STATUS_INACTIVE );
            validFriendRequest.get( ).setDateupdated( LocalDateTime.now( ) );
            userRequest.update( validFriendRequest.get( ) );

        }else{
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful && receiver.isPresent( ) ){
            RejectFriendRequestRESP rejectFriendRequestRESP = new RejectFriendRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( rejectFriendRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }else{
            this.jamiiErrorsMessagesRESP.setRejectFriendRequestOPS_GenerateGenericError( );
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
