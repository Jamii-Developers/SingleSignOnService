package com.jamii.services.singleSignOn;

import com.jamii.Utils.JamiiRandomKeyToolGen;
import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.activeDirectory.UserLoginREQ;
import com.jamii.responses.activeDirectory.UserLoginRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserLoginOPS extends ActiveDirectoryAbstract {

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private DeviceInformationCONT deviceInformationCONT;

    private UserLoginREQ userLoginREQ;
    private UserLoginTBL userData;
    private DeviceInformationTBL userDeviceInformation;
    private Boolean loginWasSuccessful = false;

    public UserLoginREQ getUserLoginREQ() {
        return userLoginREQ;
    }

    public void setUserLoginREQ(UserLoginREQ userLoginREQ) {
        this.userLoginREQ = userLoginREQ;
    }

    @Override
    public void processRequest() {

        this.userData = this.userLoginCONT.checkAndRetrieveValidLogin(this);

        if ( this.userData == null ) {
            this.jamiiErrorsMessagesRESP.setLoginError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        boolean checkIfKeyExists = false;
        JamiiRandomKeyToolGen keyToolGen = new JamiiRandomKeyToolGen( );
        keyToolGen.setLen( 50 );
        keyToolGen.setInclude_letters( true );
        keyToolGen.setInclude_numbers( true );
        keyToolGen.setInclude_special_chars( true );
        String key = "";
        while( !checkIfKeyExists ){
            key = keyToolGen.generate( );
            checkIfKeyExists = this.deviceInformationCONT.checkIfKeyExisitsInTheDatabase( key );
        }

        this.userDeviceInformation = this.deviceInformationCONT.add( this.userData, key, getUserLoginREQ().getLoginDeviceName( ) );

        this.loginWasSuccessful = true;
    }


    @Override
    public ResponseEntity< String > getResponse( ){

        if( this.loginWasSuccessful  ){
            StringBuilder response = new StringBuilder( );

            UserLoginRESP userLoginRESP = new UserLoginRESP(  );
            userLoginRESP.setUSER_KEY( this.userData.getUserKey( ) );
            userLoginRESP.setUSERNAME( this.userData.getUsername( ) );
            userLoginRESP.setEMAIL_ADDRESS( this.userData.getEmailaddress( ) );
            userLoginRESP.setDATE_CREATED( this.userData.getDatecreated( ).toString( ) );
            userLoginRESP.setDEVICE_KEY( this.userDeviceInformation.getDevicekey( ) );

            response.append(  userLoginRESP.getJSONRESP( ) );

            return new ResponseEntity<>( response.toString( ),HttpStatus.OK );
        }

        return super.getResponse( );

    }

    @Override
    public void reset( ){
        super.reset( );
        this.loginWasSuccessful = false;
        this.userData = null ;
        this.userDeviceInformation = null;
        this.setUserLoginREQ( null ) ;
    }
}
