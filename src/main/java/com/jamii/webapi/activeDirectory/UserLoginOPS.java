package com.jamii.webapi.activeDirectory;

import com.jamii.Utils.JamiiResponseErrorMessages;
import com.jamii.Utils.JamiiUserPasswordEncryptTool;
import com.jamii.responses.MapUserLoginInformation;
import com.jamii.webapi.activeDirectory.controllers.UserLoginInformationCONT;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserLoginOPS extends activeDirectoryAbstract{

    private String loginCredential;
    private String loginPassword;
    private String loginDeviceId;

    @Autowired
    private UserLoginInformationCONT userLoginInformationCONT ;

    private UserLoginTBL userData ;
    private static final Integer activeStatus = UserLoginTBL.ACTIVE_ON;

    public String getLoginCredential() {
        return this.loginCredential;
    }

    public void setLoginCredential(String loginCredential) {
        this.loginCredential = loginCredential;
    }

    public String getLoginPassword() {
        return this.loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public String getLoginDeviceId() {
        return loginDeviceId;
    }

    public void setLoginDeviceId(String loginDeviceId) {
        this.loginDeviceId = loginDeviceId;
    }

    @Override
    public void processRequest( ){
        jamiiDebug.warning( "Request has been received" );

        UserLoginTBL userData = this.userLoginInformationCONT.checkAndRetrieveValidLogin( this ) ;

        if ( userData != null ){
            if( new JamiiUserPasswordEncryptTool( getLoginPassword( ), userData.getPasswordsalt( ) ).comparePasswords( ) ){
                this.userData = userData;
            }
        }
    }

    @Override
    public ResponseEntity< HashMap < String, String > > response( ){

        if( this.userData == null ){
            jamiiDebug.warning( this.getLoginCredential( ) + " is an invalid user");
            return new ResponseEntity<>( JamiiResponseErrorMessages.loginError(), HttpStatus.BAD_REQUEST );
        }

        jamiiDebug.warning( "User has been found" );
        MapUserLoginInformation response = new MapUserLoginInformation( this.userData );
        return new ResponseEntity< >( response.getResponseMap() , HttpStatus.ACCEPTED) ;


    }

    @Override
    public void reset(){
        this.userData = null ;
    }
}
