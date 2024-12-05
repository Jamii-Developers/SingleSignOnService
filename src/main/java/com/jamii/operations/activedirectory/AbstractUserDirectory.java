package com.jamii.operations.activedirectory;

import com.jamii.Utils.JamiiCookieProcessor;
import com.jamii.Utils.JamiiDebug;
import com.jamii.responses.JamiiErrorsMessagesRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractUserDirectory {

    protected String JamiiError;
    protected JamiiErrorsMessagesRESP jamiiErrorsMessagesRESP = null;
    protected Boolean isSuccessful = true;
    protected final JamiiDebug jamiiDebug = new JamiiDebug( );

    protected static String DeviceKey = null;
    protected static String UserKey = null;
    protected static String SessionKey = null;

    @Autowired
    private JamiiCookieProcessor cookie;

    public ResponseEntity<?> run( Object requestPayload ) throws Exception{
        this.reset( );
        this.validateCookie( );
        this.processRequest( );
        jamiiDebug.info("Request completed");
        return this.getResponse( );
    }

    public void validateCookie( ) throws Exception{

        //Check if cookie information is available
        if(  DeviceKey==null || UserKey==null || SessionKey == null){
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        if(  DeviceKey.isEmpty( ) || UserKey.isEmpty( ) || SessionKey.isEmpty()){
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if user cookie is valid
        cookie.setUSER_KEY( UserKey );
        cookie.setDEVICE_KEY( DeviceKey );
        cookie.setUSER_COOKIE(SessionKey);

        if( !cookie.checkCookieIsValid( ) ){
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }
    }

    public abstract void processRequest( ) throws Exception;

    public ResponseEntity< ? > getResponse( ){

        StringBuilder response = new StringBuilder( ) ;
        if( !JamiiError.isEmpty( ) ){
            response.append ( this.JamiiError );
        }

        return new ResponseEntity<>( response.toString(), HttpStatus.OK );
    }

    public void reset( ){
        this.JamiiError = "";
        this.jamiiErrorsMessagesRESP = new JamiiErrorsMessagesRESP( );
        this.DeviceKey = null;
        this.UserKey = null;
        this.isSuccessful = true;
    }
}
