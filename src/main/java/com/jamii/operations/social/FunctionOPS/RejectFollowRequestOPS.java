package com.jamii.operations.social.FunctionOPS;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRequestCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.requests.social.FunctionREQ.RejectFollowRequestREQ;
import com.jamii.responses.social.FunctionRESP.RejectFollowRequestRESP;
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
public class RejectFollowRequestOPS extends AbstractSocial {

    private RejectFollowRequestREQ rejectFollowRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRequestCONT userRequestCONT;

    public void setRejectFollowRequestREQ(RejectFollowRequestREQ rejectFollowRequestREQ) {
        this.rejectFollowRequestREQ = rejectFollowRequestREQ;
    }

    public RejectFollowRequestREQ getRejectFollowRequestREQ() {
        return rejectFollowRequestREQ;
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getRejectFollowRequestREQ( ).getDevicekey( );
        UserKey = getRejectFollowRequestREQ( ).getUserkey( );
        SessionKey = getRejectFollowRequestREQ().getSessionkey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetchByUserKey( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetchByUserKey( getRejectFollowRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setRejectFollowRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Fetch requests to user
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequestCONT.fetch( sender.get( ), receiver.get( ), UserRequestsTBL.TYPE_FOLLOW, UserRequestsTBL.STATUS_ACTIVE ) );
        requests.addAll( userRequestCONT.fetch( receiver.get( ), sender.get( ), UserRequestsTBL.TYPE_FOLLOW, UserRequestsTBL.STATUS_ACTIVE ) );

        Optional <UserRequestsTBL> validFollowRequest = requests.stream().filter( x -> Objects.equals( x.getStatus(), UserRequestsTBL.STATUS_ACTIVE ) && x.getReceiverid( ) == sender.get( ) ).findFirst( );

        //Check if friend request exists
        if( validFollowRequest.isPresent( ) ){

            // Deactivate the request
            validFollowRequest.get( ).setStatus( UserRequestsTBL.STATUS_INACTIVE );
            validFollowRequest.get( ).setDateupdated( LocalDateTime.now( ) );
            userRequestCONT.update( validFollowRequest.get( ) );

        }else{
            this.isSuccessful = false;
        }

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful && receiver.isPresent( ) ){
            RejectFollowRequestRESP rejectFollowRequestRESP = new RejectFollowRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( rejectFollowRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }else{
            this.jamiiErrorsMessagesRESP.setRejectFollowRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.receiver = null;
    }
}
