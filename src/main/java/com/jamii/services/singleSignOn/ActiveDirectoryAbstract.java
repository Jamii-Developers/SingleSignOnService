package com.jamii.services.singleSignOn;

import com.jamii.Utils.JamiiDebug;
import com.jamii.responses.JamiiErrorsMessagesRESP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public abstract class ActiveDirectoryAbstract {

    protected String JamiiError;
    protected JamiiErrorsMessagesRESP jamiiErrorsMessagesRESP = null;

    protected final JamiiDebug jamiiDebug = new JamiiDebug( );

    public abstract void processRequest( ) throws Exception;

    public  ResponseEntity<  String > getResponse( ){

        StringBuilder response = new StringBuilder( ) ;
        if( !JamiiError.isEmpty( ) ){
            response.append ( this.JamiiError );
        }

        return new ResponseEntity<>( response.toString(), HttpStatus.OK );
    }

    public void reset( ){
        this.JamiiError = "";
        this.jamiiErrorsMessagesRESP = new JamiiErrorsMessagesRESP( );
    }
}
