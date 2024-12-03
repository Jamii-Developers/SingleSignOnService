package com.jamii.operations.social.FunctionOPS;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.controllers.UserRequestCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.requests.social.FunctionREQ.AcceptFriendRequestREQ;
import com.jamii.responses.social.FunctionRESP.AcceptFriendRequestRESP;
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
public class AcceptFriendRequestOPS extends AbstractSocial {

    private AcceptFriendRequestREQ acceptFriendRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;
    @Autowired
    private UserRequestCONT userRequestCONT;

    public void setAcceptFriendRequestREQ( AcceptFriendRequestREQ acceptFriendRequestREQ ) {
        this.acceptFriendRequestREQ = acceptFriendRequestREQ;
    }

    public AcceptFriendRequestREQ getAcceptFriendRequestREQ() {
        return acceptFriendRequestREQ;
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getAcceptFriendRequestREQ( ).getDevicekey( );
        UserKey = getAcceptFriendRequestREQ( ).getUserkey( );
        SessionKey = getAcceptFriendRequestREQ().getSessionkey();
        super.validateCookie( );
    }


    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetchByUserKey( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetchByUserKey( getAcceptFriendRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setAcceptFriendRequest_GenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        //Fetch requests to user
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequestCONT.fetch( sender.get( ), receiver.get( ), UserRequestsTBL.TYPE_FRIEND, UserRequestsTBL.STATUS_ACTIVE ) );
        requests.addAll( userRequestCONT.fetch( receiver.get( ), sender.get( ), UserRequestsTBL.TYPE_FRIEND, UserRequestsTBL.STATUS_ACTIVE ) );

        Optional <UserRequestsTBL> validFriendRequest = requests.stream().filter( x -> Objects.equals( x.getStatus(), UserRequestsTBL.STATUS_ACTIVE ) && x.getReceiverid( ) == sender.get( ) ).findFirst();
        //Check if follow request exists
        if( validFriendRequest.isPresent( ) ){

            // Deactivate the request
            validFriendRequest.get( ).setStatus( UserRequestsTBL.STATUS_INACTIVE );
            validFriendRequest.get( ).setDateupdated( LocalDateTime.now( ) );
            userRequestCONT.update( validFriendRequest.get( ) );

            //Accept follow request from sender
            this.userRelationshipCONT.add( sender.get( ), receiver.get( ), UserRelationshipTBL.TYPE_FRIEND, UserRelationshipTBL.STATUS_ACTIVE );

        }else{
            this.isSuccessful = false;
        }


    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful && receiver.isPresent()){
            AcceptFriendRequestRESP acceptFriendRequestRESP = new AcceptFriendRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( acceptFriendRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }else{
            this.jamiiErrorsMessagesRESP.setAcceptFriendRequest_GenericError( );
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
