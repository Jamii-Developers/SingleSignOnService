package com.jamii.operations.userServices.userProfile;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.UserCookiesCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserCookiesTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.profileREQ.UserLogoffREQ;
import com.jamii.responses.userResponses.profileResponses.UserLogoffRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class UserLogoffOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private DeviceInformationCONT deviceInformationCONT;
    @Autowired
    private UserCookiesCONT userCookiesCONT;

    @Override
    public void validateCookie( ) throws Exception{
        UserLogoffREQ req = (UserLogoffREQ) JamiiMapperUtils.mapObject( getRequest( ), UserLogoffREQ.class );
        setDeviceKey( req.getDeviceKey( ) );
        setUserKey( req.getUserKey( ) );
        setSessionKey( req.getSessionKey( ) );
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !getIsSuccessful( ) ){
            return;
        }

        UserLogoffREQ req = (UserLogoffREQ) JamiiMapperUtils.mapObject( getRequest( ), UserLogoffREQ.class );

        Optional<UserLoginTBL> user = this.userLoginCONT.fetchByUserKey( req.getUserKey( ), UserLoginTBL.ACTIVE_ON );

        if ( user.isEmpty( ) ) {
            this.jamiiErrorsMessagesRESP.setLoginError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        Optional<DeviceInformationTBL> device = this.deviceInformationCONT.fetch( user.get( ), req.getDeviceKey());

        if ( device.isEmpty( )) {
            this.jamiiErrorsMessagesRESP.setLoginError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }else{
            device.get().setActive( DeviceInformationTBL.ACTIVE_STATUS_DISABLED );
            this.deviceInformationCONT.update( device.get( ) );
        }

        Optional<UserCookiesTBL> cookie = this.userCookiesCONT.fetch( user.get( ), device.get( ), req.getSessionKey( ) ,UserCookiesTBL.ACTIVE_STATUS_ENABLED );

        if ( cookie.isEmpty( ) ) {
            this.jamiiErrorsMessagesRESP.setLoginError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
        }else{
            cookie.get().setActive( UserCookiesTBL.ACTIVE_STATUS_DISABLED );
            this.userCookiesCONT.update( cookie.get( ) );
        }

        setIsSuccessful( true );

    }

    @Override
    public ResponseEntity< ? > getResponse( ){

        if( getIsSuccessful( ) ){

            StringBuilder response = new StringBuilder( );
            UserLogoffRESP userLogoffRESP = new UserLogoffRESP(  );
            response.append( userLogoffRESP.getJSONRESP( ) );
            return new ResponseEntity<>( response.toString( ), HttpStatus.OK );
        }
        return super.getResponse( );
    }
}
