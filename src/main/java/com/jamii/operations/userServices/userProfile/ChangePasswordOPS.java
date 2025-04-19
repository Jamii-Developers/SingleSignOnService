package com.jamii.operations.userServices.userProfile;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiStringUtils;
import com.jamii.Utils.JamiiUserPasswordEncryptTool;
import com.jamii.jamiidb.controllers.PasswordHashRecords;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.profileREQ.ChangePasswordServicesREQ;
import com.jamii.responses.userResponses.profileResponses.ChangePasswordRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChangePasswordOPS extends AbstractUserServicesOPS {

    @Autowired
    private PasswordHashRecords passwordHashRecords;
    @Autowired
    private UserLogin userLogin;

    @Override
    public void validateCookie( ) throws Exception{
        ChangePasswordServicesREQ req = ( ChangePasswordServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), ChangePasswordServicesREQ.class );
        setDeviceKey( req.getDeviceKey( ) );
        setUserKey( req.getUserKey( ) );
        setSessionKey( req.getSessionKey( ) );
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !getIsSuccessful( ) ){
            return;
        }

        ChangePasswordServicesREQ req = (ChangePasswordServicesREQ) JamiiMapperUtils.mapObject( getRequest( ), ChangePasswordServicesREQ.class );

        //Check if the username and email address are available in the system
        Optional <UserLoginTBL> user = userLogin.fetch( req.getEmailaddress( ), req.getUsername(), UserLogin.ACTIVE_ON );
        if( user.isEmpty( ) ){
            jamiiDebug.warning( "The user does not exist in the system : " + req.getUsername( ) );
            this.jamiiErrorsMessagesRESP.setPasswordChange_UsernameOrEmailAddressDoesNotExist( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        //Check if the old password matches the old saved password
        String encryptedOldPassword = JamiiUserPasswordEncryptTool.encryptPassword( req.getOld_password( ) );
        String encryptedNewPassword = JamiiUserPasswordEncryptTool.encryptPassword( req.getNew_password( ) );;
        if( !JamiiStringUtils.equals( encryptedOldPassword, user.get( ).getPasswordsalt( ) ) ){
            jamiiDebug.warning( "This password doesn't match what we have in the system : " + req.getUsername( ) );
            this.jamiiErrorsMessagesRESP.setPasswordChange_PasswordsNotMatching( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        user.get( ).setPasswordsalt( encryptedNewPassword );
        //Check if the new password matches the last 10 passwords the user used
        if( passwordHashRecords.isPasswordInLastTenRecords( user.get( ) ) ){
            jamiiDebug.warning( "This password matches the last ten the user has used :" + req.getUsername( ) );
            this.jamiiErrorsMessagesRESP.setPasswordChange_PasswordMatchesLastTen( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        userLogin.update( user.get( ) );
        passwordHashRecords.addUserNewPasswordRecord( user.get( ) ) ;

        setIsSuccessful( true );
    }


    @Override
    public ResponseEntity< ? > getResponse( ){

        if( this.JamiiError.isEmpty( ) ){
            ChangePasswordRESP changePasswordRESP = new ChangePasswordRESP( );
            return new ResponseEntity<>( changePasswordRESP.getJSONRESP( ), HttpStatus.OK );
        }

        return super.getResponse( );

    }
}
