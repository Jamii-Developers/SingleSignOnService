package com.jamii.webapi.activeDirectory;

import com.jamii.webapi.jamiidb.controllers.UserLoginInformationCONT;
import org.springframework.http.ResponseEntity;

public class UserLoginOPS {

    public UserLoginOPS( String loginCredential, String loginPassword ) {
        setLoginCredential( loginCredential );
        setLoginPassword( loginPassword );
    }

    private UserLoginInformationCONT userLoginInformationCONT;
    private ResponseEntity< String > RESPONSE;
    private String loginCredential;
    private String loginPassword;

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

    public void processRequest( ){

        userLoginInformationCONT.checkifLoginIsValid( this );

        System.out.println( "Request has been received" );

    }

    public ResponseEntity<String> response(){
        return RESPONSE;
    }
}
