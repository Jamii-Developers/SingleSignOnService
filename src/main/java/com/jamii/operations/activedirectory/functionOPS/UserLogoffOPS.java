package com.jamii.operations.activedirectory.functionOPS;

import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.UserCookiesCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserCookiesTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.activedirectory.AbstractUserDirectory;
import com.jamii.requests.activeDirectory.FunctionREQ.UserLogoffREQ;
import com.jamii.responses.activeDirectory.FunctionRESP.UserLogoffRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class UserLogoffOPS extends AbstractUserDirectory {

    private UserLogoffREQ userLogoffREQ;

    public UserLogoffREQ getUserLogoffREQ() {
        return userLogoffREQ;
    }

    public void setUserLogoffREQ(UserLogoffREQ userLogoffREQ) {
        this.userLogoffREQ = userLogoffREQ;
    }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private DeviceInformationCONT deviceInformationCONT;
    @Autowired
    private UserCookiesCONT userCookiesCONT;

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getUserLogoffREQ( ).getDevicekey( );
        UserKey = getUserLogoffREQ( ).getUserkey();
        SessionKey = getUserLogoffREQ( ).getSessionkey( );
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> user = this.userLoginCONT.fetchByUserKey( getUserLogoffREQ( ).getUserkey( ), UserLoginTBL.ACTIVE_ON );

        if ( user.isEmpty( ) ) {
            this.jamiiErrorsMessagesRESP.setLoginError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        Optional<DeviceInformationTBL> device = this.deviceInformationCONT.fetch( user.get( ), getUserLogoffREQ().getDevicekey());

        if ( device.isEmpty( )) {
            this.jamiiErrorsMessagesRESP.setLoginError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }else{
            device.get().setActive( DeviceInformationTBL.ACTIVE_STATUS_DISABLED );
            this.deviceInformationCONT.update( device.get( ) );
        }

        Optional<UserCookiesTBL> cookie = this.userCookiesCONT.fetch( user.get( ), device.get( ), getUserLogoffREQ().getSessionkey( ) ,UserCookiesTBL.ACTIVE_STATUS_ENABLED );

        if ( cookie.isEmpty( ) ) {
            this.jamiiErrorsMessagesRESP.setLoginError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }else{
            cookie.get().setActive( UserCookiesTBL.ACTIVE_STATUS_DISABLED );
            this.userCookiesCONT.update( cookie.get( ) );
        }

    }

    @Override
    public ResponseEntity< ? > getResponse( ){

        if( this.isSuccessful  ){

            StringBuilder response = new StringBuilder( );
            UserLogoffRESP userLogoffRESP = new UserLogoffRESP(  );
            response.append( userLogoffRESP.getJSONRESP( ) );
            return new ResponseEntity<>( response.toString( ), HttpStatus.OK );
        }
        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.isSuccessful = true;
        this.setUserLogoffREQ( null ); ;
    }
}
