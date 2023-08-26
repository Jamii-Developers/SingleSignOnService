package com.jamii.webapi.activeDirectory;

import com.jamii.Utils.JamiiResponseErrorMessages;
import com.jamii.Utils.JamiiStringUtils;
import com.jamii.Utils.JamiiUserPasswordEncryptTool;
import com.jamii.responses.MapUserLoginInformation;
import com.jamii.webapi.jamiidb.controllers.UserLoginCONT;
import com.jamii.requests.UserLoginREQ;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserLoginOPS extends activeDirectoryAbstract{

    @Autowired
    private UserLoginCONT userLoginCONT;

    private UserLoginREQ userLoginREQ;

    private UserLoginTBL userData ;


    public UserLoginREQ getUserLoginREQ() {
        return userLoginREQ;
    }

    public void setUserLoginREQ(UserLoginREQ userLoginREQ) {
        this.userLoginREQ = userLoginREQ;
    }

    @Override
    public void processRequest( ){
        jamiiDebug.warning( "Request has been received" );

        UserLoginTBL userData = this.userLoginCONT.checkAndRetrieveValidLogin( this ) ;

        String encryptPassword = JamiiUserPasswordEncryptTool.doEncrypt( this.userLoginREQ.getLoginPassword( ) );
        if ( userData != null ){
            if( JamiiStringUtils.equals( encryptPassword, userData.getPasswordsalt( ) ) ){
                this.userData = userData;
            }
        }
    }

    @Override
    public ResponseEntity< HashMap < String, String > > response( ){

        if( this.userData == null ){
            jamiiDebug.warning( this.userLoginREQ.getLoginCredential( ) + " is an invalid user");
            return new ResponseEntity<>( JamiiResponseErrorMessages.loginError(), HttpStatus.BAD_REQUEST );
        }

        jamiiDebug.warning( "User has been found" );
        MapUserLoginInformation response = new MapUserLoginInformation( this.userData );
        return new ResponseEntity< >( response.getResponseMap() , HttpStatus.ACCEPTED) ;


    }

    @Override
    public void reset(){
        this.userData = null ;
        this.setUserLoginREQ( null ) ;
    }
}
