package com.jamii.webapi.activeDirectory;

import com.jamii.responses.JamiiResponseErrorMessages;
import com.jamii.requests.UserLoginREQ;
import com.jamii.responses.MapUserLoginInformation;
import com.jamii.webapi.jamiidb.controllers.UserLoginCONT;
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

    public void setUserLoginREQ( UserLoginREQ userLoginREQ ) {
        this.userLoginREQ = userLoginREQ ;
    }

    @Override
    public void processRequest( ){
        jamiiDebug.warning( "Request has been received" );

        UserLoginTBL userlogin = this.userLoginCONT.checkAndRetrieveValidLogin( this ) ;

        if ( userlogin != null ){
                this.userData = userlogin;
        }
    }

    @Override
    public ResponseEntity< HashMap < String, String > > response( ){

        if( this.userData == null ){
            jamiiDebug.warning( this.userLoginREQ.getLoginCredential( ) + " is an invalid user");
            return new ResponseEntity<>( JamiiResponseErrorMessages.loginError( ), HttpStatus.OK );
        }

        jamiiDebug.warning( "User has been found" );
        MapUserLoginInformation response = new MapUserLoginInformation( this.userData );
        return new ResponseEntity< >( response.getResponseMap( ) , HttpStatus.OK ) ;

    }

    @Override
    public void reset(){
        this.userData = null ;
        this.setUserLoginREQ( null ) ;
    }
}
