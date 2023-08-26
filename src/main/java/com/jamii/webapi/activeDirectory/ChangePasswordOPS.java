package com.jamii.webapi.activeDirectory;

import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiStringUtils;
import com.jamii.Utils.JamiiUserPasswordEncryptTool;
import com.jamii.requests.ChangePasswordREQ;
import com.jamii.webapi.jamiidb.controllers.PasswordHashRecordsCONT;
import com.jamii.webapi.jamiidb.controllers.UserLoginCONT;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
            return ;
        }

        //Check if the old password matches the old saved password
        String encryptedOldPassword = JamiiUserPasswordEncryptTool.encryptPassword( this.getChangePasswordREQ( ).getOld_password( ) );
        String encryptedNewPassword = JamiiUserPasswordEncryptTool.encryptPassword( this.getChangePasswordREQ( ).getNew_password( ) );;
        if( !JamiiStringUtils.equals( encryptedOldPassword, user.get( ).getPasswordsalt( ) ) ){
            JamiiDebug.warning( "This password doesn't match what we have in the system : " + getChangePasswordREQ( ).getUsername( ) );
            return;
        }

        user.get( ).setPasswordsalt( encryptedNewPassword );
        //Check if the new password matches the last 10 passwords the user used
        if( passwordHashRecordsCONT.isPasswordInLastTenRecords( user.get( ) ) ){
            JamiiDebug.warning( "This password matches the last ten the user has used :" + getChangePasswordREQ( ).getUsername( ) );
            return;
        }

        userLoginCONT.add( user.get( ) );
        passwordHashRecordsCONT.addUserNewPasswordRecord( user.get( ) ) ;

        passwordChangeSuccessful = true;
    }

    @Override
    public ResponseEntity<HashMap<String, String>> response() {

        if( !passwordChangeSuccessful ){
            JamiiDebug.warning( "Password Change unsuccessfully:" + getChangePasswordREQ( ).getUsername( ) );
        }

        return null;
    }

    @Override
    public void reset(){
        this.setChangePasswordREQ( null );
    }
}
