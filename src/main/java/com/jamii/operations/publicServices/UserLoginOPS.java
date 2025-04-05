package com.jamii.operations.publicServices;

import com.jamii.Utils.JamiiDateUtils;
import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiRandomKeyToolGen;
import com.jamii.jamiidb.controllers.DeviceInformation;
import com.jamii.jamiidb.controllers.UserCookies;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserCookiesTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.publicServices.UserLoginREQ;
import com.jamii.responses.publicResponses.UserLoginRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class UserLoginOPS extends AbstractPublicServices {

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private DeviceInformation deviceInformation;
    @Autowired
    private UserCookies userCookies;

    private UserLoginTBL userData;
    private DeviceInformationTBL userDeviceInformation;
    private UserCookiesTBL userCookie;

    @Override
    public void processRequest() {
        UserLoginREQ req = (UserLoginREQ) JamiiMapperUtils.mapObject( getRequest( ), UserLoginREQ.class );

        this.userData = this.userLogin.checkAndRetrieveValidLogin( req.getLoginCredential(), req.getLoginPassword() );

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
            checkIfKeyExists = this.deviceInformation.checkIfKeyExisitsInTheDatabase( this.userData ,key );
        }

        this.userDeviceInformation = this.deviceInformation.add( this.userData, key, req.getLoginDeviceName( ), req.getLocation() );

        boolean checkIfSessionKeyExists = false;
        JamiiRandomKeyToolGen sessionkeyToolGen = new JamiiRandomKeyToolGen( );
        sessionkeyToolGen.setLen( 70 );
        sessionkeyToolGen.setInclude_letters( true );
        sessionkeyToolGen.setInclude_numbers( true );
        sessionkeyToolGen.setInclude_special_chars( true );
        String sessionkey = "";
        while( !checkIfSessionKeyExists ){
            sessionkey = sessionkeyToolGen.generate( );
            checkIfSessionKeyExists = this.userCookies.checkIfKeyExisitsInTheDatabase( this.userData,this.userDeviceInformation,sessionkey );
        }

        this.userCookie = this.userCookies.add( this.userData, this.userDeviceInformation, sessionkey, req.getRememberLogin());

        setIsSuccessful( true );
    }


    @Override
    public ResponseEntity< ? > getResponse( ){

        if( getIsSuccessful( )  ){
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
        this.userData = null ;
        this.userDeviceInformation = null;
    }
}
