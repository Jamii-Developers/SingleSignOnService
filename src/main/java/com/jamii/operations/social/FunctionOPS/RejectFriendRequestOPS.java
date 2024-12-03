package com.jamii.operations.social.FunctionOPS;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRequestCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.requests.social.FunctionREQ.RejectFriendRequestREQ;
import com.jamii.responses.social.FunctionRESP.RejectFriendRequestRESP;
import com.jamii.operations.social.AbstractSocial;
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
public class RejectFriendRequestOPS extends AbstractSocial {

    private RejectFriendRequestREQ rejectFriendRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRequestCONT userRequestCONT;

    public void setRejectFriendRequestREQ( RejectFriendRequestREQ rejectFriendRequestREQ ) {
        this.rejectFriendRequestREQ = rejectFriendRequestREQ;
    }

    public RejectFriendRequestREQ getRejectFriendRequestREQ() {
        return rejectFriendRequestREQ;
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getRejectFriendRequestREQ( ).getDevicekey( );
        UserKey = getRejectFriendRequestREQ( ).getUserkey( );
        SessionKey = getRejectFriendRequestREQ().getSessionkey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetchByUserKey( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetchByUserKey( getRejectFriendRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setRejectFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Fetch requests to user
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequestCONT.fetch( sender.get( ), receiver.get( ), UserRequestsTBL.TYPE_FRIEND, UserRequestsTBL.STATUS_ACTIVE ) );
        requests.addAll( userRequestCONT.fetch( receiver.get( ), sender.get( ), UserRequestsTBL.TYPE_FRIEND, UserRequestsTBL.STATUS_ACTIVE ) );

        Optional <UserRequestsTBL> validFriendRequest = requests.stream().filter( x -> Objects.equals( x.getStatus(), UserRequestsTBL.STATUS_ACTIVE ) && x.getReceiverid( ) == sender.get( ) ).findFirst( );

        //Check if friend request exists
        if( validFriendRequest.isPresent( ) ){

            // Deactivate the request
            validFriendRequest.get( ).setStatus( UserRequestsTBL.STATUS_INACTIVE );
            validFriendRequest.get( ).setDateupdated( LocalDateTime.now( ) );
            userRequestCONT.update( validFriendRequest.get( ) );

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
