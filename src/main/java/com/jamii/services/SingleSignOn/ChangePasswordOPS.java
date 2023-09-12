package com.jamii.services.SingleSignOn;

import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiStringUtils;
import com.jamii.Utils.JamiiUserPasswordEncryptTool;
import com.jamii.requests.activeDirectory.ChangePasswordREQ;
import com.jamii.responses.activeDirectory.ChangePasswordRESP;
import com.jamii.jamiidb.controllers.PasswordHashRecordsCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChangePasswordOPS extends activeDirectoryAbstract {

    @Autowired
    private PasswordHashRecordsCONT passwordHashRecordsCONT;
    @Autowired
    private UserLoginCONT userLoginCONT;

    protected Boolean passwordChangeSuccessful = false;

    public ChangePasswordOPS() {
    }

    private ChangePasswordREQ changePasswordREQ;

    public ChangePasswordREQ getChangePasswordREQ() {
        return changePasswordREQ;
    }

    public void setChangePasswordREQ(ChangePasswordREQ changePasswordREQ) {
        this.changePasswordREQ = changePasswordREQ;
    }

    @Override
    public void processRequest() throws Exception {

        //Check if the username and email address are available in the sustem
        Optional <UserLoginTBL> user = userLoginCONT.fetch( this.getChangePasswordREQ( ).getEmailaddress( ), this.getChangePasswordREQ().getUsername(), UserLoginTBL.ACTIVE_ON );
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "The user does not exist in the system : " + getChangePasswordREQ( ).getUsername( ) );
            this.jamiiErrorsMessagesRESP.setPasswordChange_UsernameOrEmailAddressDoesNotExist( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        //Check if the old password matches the old saved password
        String encryptedOldPassword = JamiiUserPasswordEncryptTool.encryptPassword( this.getChangePasswordREQ( ).getOld_password( ) );
        String encryptedNewPassword = JamiiUserPasswordEncryptTool.encryptPassword( this.getChangePasswordREQ( ).getNew_password( ) );;
        if( !JamiiStringUtils.equals( encryptedOldPassword, user.get( ).getPasswordsalt( ) ) ){
            JamiiDebug.warning( "This password doesn't match what we have in the system : " + getChangePasswordREQ( ).getUsername( ) );
            this.jamiiErrorsMessagesRESP.setPasswordChange_PasswordsNotMatching( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        user.get( ).setPasswordsalt( encryptedNewPassword );
        //Check if the new password matches the last 10 passwords the user used
        if( passwordHashRecordsCONT.isPasswordInLastTenRecords( user.get( ) ) ){
            JamiiDebug.warning( "This password matches the last ten the user has used :" + getChangePasswordREQ( ).getUsername( ) );
            this.jamiiErrorsMessagesRESP.setPasswordChange_PasswordMatchesLastTen( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        userLoginCONT.update( user.get( ) );
        passwordHashRecordsCONT.addUserNewPasswordRecord( user.get( ) ) ;

        passwordChangeSuccessful = true;
    }


    @Override
    public ResponseEntity< String > getResponse( ){

        if( this.JamiiError.isEmpty( ) ){
            StringBuilder response = new StringBuilder( );
            ChangePasswordRESP changePasswordRESP = new ChangePasswordRESP( );
            response.append(  changePasswordRESP.getJSONRESP( ) );
            return new ResponseEntity<>( response.toString( ), HttpStatus.OK );
        }

        return super.getResponse( );

    }

    @Override
    public void reset(){
        super.reset( );
        this.passwordChangeSuccessful = false;
        this.setChangePasswordREQ( null );
    }
}
