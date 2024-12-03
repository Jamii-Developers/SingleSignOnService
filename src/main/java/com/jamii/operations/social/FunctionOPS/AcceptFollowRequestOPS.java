package com.jamii.operations.social.FunctionOPS;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.controllers.UserRequestCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.requests.social.FunctionREQ.AcceptFollowRequestREQ;
import com.jamii.responses.social.FunctionRESP.AcceptFollowRequestRESP;
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
public class AcceptFollowRequestOPS extends AbstractSocial {

    private AcceptFollowRequestREQ acceptFollowRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;
    @Autowired
    private UserRequestCONT userRequestCONT;

    public void setAcceptFollowRequestREQ(AcceptFollowRequestREQ acceptFollowRequestREQ) {
        this.acceptFollowRequestREQ = acceptFollowRequestREQ;
    }

    public AcceptFollowRequestREQ getAcceptFollowRequestREQ() {
        return acceptFollowRequestREQ;
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getAcceptFollowRequestREQ( ).getDevicekey( );
        UserKey = getAcceptFollowRequestREQ( ).getUserkey( );
        SessionKey = getAcceptFollowRequestREQ().getSessionkey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetchByUserKey( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetchByUserKey( getAcceptFollowRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Fetch requests to user
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequestCONT.fetch( sender.get( ), receiver.get( ), UserRequestsTBL.TYPE_FOLLOW, UserRequestsTBL.STATUS_ACTIVE ) );
        requests.addAll( userRequestCONT.fetch( receiver.get( ), sender.get( ), UserRequestsTBL.TYPE_FOLLOW, UserRequestsTBL.STATUS_ACTIVE ) );

        Optional <UserRequestsTBL> validFollowRequest = requests.stream().filter( x -> Objects.equals( x.getStatus(), UserRequestsTBL.STATUS_ACTIVE) && x.getReceiverid( ) == sender.get( ) ).findFirst();
        //Check if follow request exists
        if( validFollowRequest.isPresent( ) ){

            // Deactivate the request
            validFollowRequest.get( ).setStatus( UserRequestsTBL.STATUS_INACTIVE );
            validFollowRequest.get( ).setDateupdated( LocalDateTime.now( ) );
            userRequestCONT.update( validFollowRequest.get( ) );

            //Accept follow request from sender
            this.userRelationshipCONT.add( receiver.get( ), sender.get( ), UserRelationshipTBL.TYPE_FOLLOW, UserRelationshipTBL.STATUS_ACTIVE );

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
