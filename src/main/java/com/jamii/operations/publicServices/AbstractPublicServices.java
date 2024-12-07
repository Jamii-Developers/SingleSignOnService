package com.jamii.operations.publicServices;

import com.jamii.Utils.JamiiDebug;
import com.jamii.responses.JamiiErrorsMessagesRESP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public abstract class AbstractPublicServices {

    protected String JamiiError;
    protected JamiiErrorsMessagesRESP jamiiErrorsMessagesRESP = null;
    protected JamiiDebug jamiiDebug = new JamiiDebug( this.getClass( ) );

    protected Boolean isSuccessful = false;
    protected Boolean getIsSuccessful( ) {
        return isSuccessful;
    }
    protected void setIsSuccessful( Boolean isSuccessful ) {
        this.isSuccessful = isSuccessful;
    }

    protected Object request;
    public Object getRequest( ) {
        return request;
    }
    public void setRequest(Object request) {this.request = request;}

    public void reset( ){
        this.JamiiError = "";
        this.jamiiErrorsMessagesRESP = new JamiiErrorsMessagesRESP( );
        setRequest( null );
        setIsSuccessful( false );
    }

    public abstract void processRequest( ) throws Exception;

    public ResponseEntity<?> run( Object requestPayload ) throws Exception{
        jamiiDebug.info("Received request" );
        setRequest( requestPayload );
        processRequest( );
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


}
