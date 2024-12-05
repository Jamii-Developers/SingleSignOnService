package com.jamii.operations.activedirectory;

import com.jamii.Utils.JamiiDebug;
import com.jamii.responses.JamiiErrorsMessagesRESP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public abstract class AbstractPublicDirectory {

    protected String JamiiError;
    protected JamiiErrorsMessagesRESP jamiiErrorsMessagesRESP = null;

    protected final JamiiDebug jamiiDebug = new JamiiDebug( );


    public abstract void processRequest( ) throws Exception;


    public ResponseEntity<?> run( Object requestPayload ) throws Exception{
        this.reset( );
        this.processRequest( );
        jamiiDebug.info("Request completed");
        return this.getResponse( );
    }

    public  ResponseEntity< ? > getResponse( ){

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
