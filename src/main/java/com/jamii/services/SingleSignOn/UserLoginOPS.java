package com.jamii.services.SingleSignOn;

import com.jamii.requests.activeDirectory.UserLoginREQ;
import com.jamii.responses.activeDirectory.UserLoginRESP;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserLoginOPS extends activeDirectoryAbstract {

    @Autowired
    private UserLoginCONT userLoginCONT;

    private UserLoginREQ userLoginREQ;

    private UserLoginTBL userData;

    public UserLoginREQ getUserLoginREQ() {
        return userLoginREQ;
    }

    public void setUserLoginREQ(UserLoginREQ userLoginREQ) {
        this.userLoginREQ = userLoginREQ;
    }

    @Override
    public void processRequest() {



        UserLoginTBL userlogin = this.userLoginCONT.checkAndRetrieveValidLogin(this);

        if ( userlogin != null ) {
            this.userData = userlogin;
            return;
        }
        this.jamiiErrorsMessagesRESP.setLoginError( );
        this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;

    }


    @Override
    public ResponseEntity< String > getResponse( ){

        if( this.JamiiError.isEmpty( ) ){
            StringBuilder response = new StringBuilder( );

            UserLoginRESP userLoginRESP = new UserLoginRESP(  );
            userLoginRESP.setUSER_KEY( this.userData.getUserKey( ) );
            userLoginRESP.setUSERNAME( this.userData.getUsername( ) );
            userLoginRESP.setEMAIL_ADDRESS( this.userData.getEmailaddress( ) );
            userLoginRESP.setDATE_CREATED( this.userData.getDatecreated( ).toString( ) );
            response.append(  userLoginRESP.getJSONRESP( ) );
            return new ResponseEntity<>( response.toString( ),HttpStatus.OK );
        }

        return super.getResponse( );

    }

    @Override
    public void reset( ){
        super.reset( );
        this.userData = null ;
        this.setUserLoginREQ( null ) ;
    }
}
