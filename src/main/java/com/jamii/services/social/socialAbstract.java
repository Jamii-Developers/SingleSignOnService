package com.jamii.services.social;

import com.jamii.Utils.JamiiCookieProcessor;
import com.jamii.Utils.JamiiDebug;
import com.jamii.responses.JamiiErrorsMessagesRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class socialAbstract {

    protected String JamiiError;
    protected JamiiErrorsMessagesRESP jamiiErrorsMessagesRESP = null;
    protected Boolean isSuccessful = true;
    protected final JamiiDebug jamiiDebug = new JamiiDebug( );

    protected static String DeviceKey = null;
    protected static String UserKey = null;

    @Autowired
    private JamiiCookieProcessor cookie;

    public void processRequest( ) throws Exception{

        //Check if cookie information is available
        if(  this.DeviceKey==null || this.UserKey==null ){
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        if(  this.DeviceKey.isEmpty( ) || this.UserKey.isEmpty( ) ){
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Check if user cookie is valid
        cookie.setUSER_KEY( this.UserKey );
        cookie.setDEVICE_KEY( this.DeviceKey );

        if( !cookie.checkCookieIsValid( ) ){
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

    }

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
        this.isSuccessful = true;
    }
}
