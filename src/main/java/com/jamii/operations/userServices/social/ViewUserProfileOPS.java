package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.UserData;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.ViewUserProfileServicesREQ;
import com.jamii.responses.userResponses.socialResponses.ViewUserProfileRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ViewUserProfileOPS extends AbstractUserServicesOPS {


    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserData userData;

    @Override
    public void validateCookie( ) throws Exception{
        ViewUserProfileServicesREQ req = ( ViewUserProfileServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), ViewUserProfileServicesREQ.class );
        setDeviceKey( req.getDeviceKey( ) );
        setUserKey( req.getUserKey( ) );
        setSessionKey( req.getSessionKey() );
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !getIsSuccessful( ) ){
            return;
        }

        ViewUserProfileServicesREQ req = ( ViewUserProfileServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), ViewUserProfileServicesREQ.class );

        // Check if both users exist in the system
        this.userLogin.data = new UserLoginTBL( );
        this.userLogin.otherUser = new UserLoginTBL( );
        this.userLogin.data = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON ).orElse( null );
        this.userLogin.otherUser = this.userLogin.fetchByUserKey( req.getTargetUserKey( ), UserLogin.ACTIVE_ON ).orElse( null );
        if( this.userLogin.data == null  || this.userLogin.otherUser == null ){
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        this.userData.data = new UserDataTBL( );
        this.userData.data = this.userData.fetch( this.userLogin.otherUser, UserData.CURRENT_STATUS_ON ).orElse( null );

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( getIsSuccessful() ){

            return new ResponseEntity< >( new ViewUserProfileRESP( this.userLogin.otherUser, this.userData.data ).getJSONRESP( ), HttpStatus.OK ) ;
        }
        return super.getResponse( );
    }
}
