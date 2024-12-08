package com.jamii.operations.userServices.userProfile;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.profileREQ.DeactivateUserREQ;
import com.jamii.responses.userResponses.profileResponses.DeactivateUserRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeactivateUserOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLoginCONT userLoginCONT;

    @Override
    public void validateCookie( ) throws Exception{
        DeactivateUserREQ req = (DeactivateUserREQ) JamiiMapperUtils.mapObject( getRequest( ), DeactivateUserREQ.class );
        setDeviceKey( req.getDeviceKey( ) );
        setUserKey( req.getUserKey( ) );
        setSessionKey( req.getSessionKey( ) );
        super.validateCookie( );
    }


    @Override
    public void processRequest( ) throws Exception {

        if( !getIsSuccessful( ) ){
            return;
        }

        DeactivateUserREQ req = (DeactivateUserREQ) JamiiMapperUtils.mapObject( getRequest( ), DeactivateUserREQ.class );
        //Check if the user exists as active
        Optional< UserLoginTBL > user = userLoginCONT.fetch( req.getEmailaddress( ), req.getUsername( ), UserLoginTBL.ACTIVE_ON );
        if( user.isEmpty( ) ){
            jamiiDebug.warning( "No activated user matches this information " + req.getUsername( ) );
            this.jamiiErrorsMessagesRESP.setDeactivateUser_UsernameOrEmailAddressDoesNotExist( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        //Check if the password is valid
        if( !userLoginCONT.isPasswordValid( req.getPassword( ), user.get( ) ) ){
            jamiiDebug.warning( "Password is incorrect " + req.getUsername( ) );
            this.jamiiErrorsMessagesRESP.setDeactivateUser_PasswordsNotMatching( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        //Deactivate user
        userLoginCONT.deactivateUser( user.get( ) );
        setIsSuccessful( true );
    }

    @Override
    public ResponseEntity< ? > getResponse() {

        if( getIsSuccessful( ) ){
            DeactivateUserREQ req = (DeactivateUserREQ) JamiiMapperUtils.mapObject( getRequest( ), DeactivateUserREQ.class );
            jamiiDebug.info( String.format( "This account has been deactivated : %s ", req.getUsername( ) ) );
            DeactivateUserRESP deactivateUserRESP = new DeactivateUserRESP( );
            return new ResponseEntity<>( deactivateUserRESP.getJSONRESP( ), HttpStatus.OK );
        }

        return super.getResponse( );
    }
}
