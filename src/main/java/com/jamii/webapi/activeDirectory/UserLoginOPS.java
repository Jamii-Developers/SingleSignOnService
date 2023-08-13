package com.jamii.webapi.activeDirectory;

import com.jamii.Utils.JamiiResponseErrorMessages;
import com.jamii.webapi.jamiidb.controllers.UserLoginInformationCONT;
import com.jamii.webapi.jamiidb.model.UserLoginInformationTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@SpringBootApplication
public class UserLoginOPS {


    @Autowired
    private UserLoginInformationCONT userLoginInformationCONT;
    protected UserLoginInformationTBL userData;
    protected String loginCredential;
    protected String loginPassword;
    protected Integer activeStatus = UserLoginInformationTBL.ACTIVE_ON;



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

    public void processRequest( ){

        UserLoginInformationTBL userData = userLoginInformationCONT.checkAndRetrieveValidLogin( this ) ;
        if ( userData != null ){
            this.userData = userData;
        }

        System.out.println( "Request has been received" );
    }

    public ResponseEntity< HashMap < String, String > > response(  ){

        if( this.userData == null ){
            return new ResponseEntity<>( JamiiResponseErrorMessages.loginError(), HttpStatus.BAD_REQUEST );
        }

        return null;
    }
}
