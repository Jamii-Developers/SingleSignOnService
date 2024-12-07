package com.jamii.operations.activedirectory.functionOPS;

import com.jamii.Utils.JamiiDateUtils;
import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiRandomKeyToolGen;
import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.UserCookiesCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserCookiesTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.activedirectory.AbstractPublicDirectory;
import com.jamii.requests.activeDirectory.FunctionREQ.UserLoginREQ;
import com.jamii.responses.activeDirectory.FunctionRESP.UserLoginRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class UserLoginOPS extends AbstractPublicDirectory {

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private DeviceInformationCONT deviceInformationCONT;
    @Autowired
    private UserCookiesCONT userCookiesCONT;

    private UserLoginTBL userData;
    private DeviceInformationTBL userDeviceInformation;
    private UserCookiesTBL userCookie;
    private Boolean loginWasSuccessful = false;

    @Override
    public void processRequest() {
        UserLoginREQ req = (UserLoginREQ) JamiiMapperUtils.mapObject( getRequest( ), UserLoginREQ.class );

        this.userData = this.userLoginCONT.checkAndRetrieveValidLogin( req );

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
            checkIfKeyExists = this.deviceInformationCONT.checkIfKeyExisitsInTheDatabase( this.userData ,key );
        }

        this.userDeviceInformation = this.deviceInformationCONT.add( this.userData, key, req.getLoginDeviceName( ), req.getLocation() );

        boolean checkIfSessionKeyExists = false;
        JamiiRandomKeyToolGen sessionkeyToolGen = new JamiiRandomKeyToolGen( );
        sessionkeyToolGen.setLen( 70 );
        sessionkeyToolGen.setInclude_letters( true );
        sessionkeyToolGen.setInclude_numbers( true );
        sessionkeyToolGen.setInclude_special_chars( true );
        String sessionkey = "";
        while( !checkIfSessionKeyExists ){
            sessionkey = sessionkeyToolGen.generate( );
            checkIfSessionKeyExists = this.userCookiesCONT.checkIfKeyExisitsInTheDatabase( this.userData,this.userDeviceInformation,sessionkey );
        }

        this.userCookie = this.userCookiesCONT.add( this.userData, this.userDeviceInformation, sessionkey, req.getRememberLogin());

        this.loginWasSuccessful = true;
    }


    @Override
    public ResponseEntity< ? > getResponse( ){

        if( this.loginWasSuccessful  ){
            StringBuilder response = new StringBuilder( );

            UserLoginRESP userLoginRESP = new UserLoginRESP(  );
            userLoginRESP.setUSER_KEY( this.userData.getUserKey( ) );
            userLoginRESP.setUSERNAME( this.userData.getUsername( ) );
            userLoginRESP.setEMAIL_ADDRESS( this.userData.getEmailaddress( ) );
            userLoginRESP.setDATE_CREATED( JamiiDateUtils.COOKIE_DATE.format( this.userCookie.getDatecreated( ).atZone(ZoneId.of("GMT") ) )  );
            userLoginRESP.setDEVICE_KEY( this.userDeviceInformation.getDevicekey( ) );
            userLoginRESP.setSESSION_KEY( this.userCookie.getSessionkey( ) );
            userLoginRESP.setEXPIRY_DATE( JamiiDateUtils.COOKIE_DATE.format( this.userCookie.getExpiredate( ).atZone(ZoneId.of("GMT") ) ) );

            response.append( userLoginRESP.getJSONRESP( ) );

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
    }
}
