package com.jamii.operations.publicServices;

import com.jamii.Utils.JamiiDebug;
import com.jamii.responses.JamiiErrorsMessagesRESP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public abstract class AbstractPublicServices {

    protected String JamiiError;
    protected JamiiErrorsMessagesRESP jamiiErrorsMessagesRESP = null;
    protected JamiiDebug jamiiDebug = new JamiiDebug( this.getClass( ) );

    protected Boolean isSuccessful = true;
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

    /**
     * This method is used to reset:
     * * The error message responses
     * * Requests submitted requests
     * * Successfull check for requests
     */
    public void reset( ){
        this.JamiiError = "";
        this.jamiiErrorsMessagesRESP = new JamiiErrorsMessagesRESP( );
        setRequest( null );
        setIsSuccessful( true );
    }

    /**
     * This is the meat of the code so will always need to be configured
     * @throws Exception
     */
    public abstract void processRequest( ) throws Exception;

    /**
     * This handles the running process of the request and is always going to be called when attempting to process any request
     * @param requestPayload
     * @return
     * @throws Exception
     */
    public ResponseEntity<?> run( Object requestPayload ) throws Exception{
        jamiiDebug.info("Received request" );
        setRequest( requestPayload );
        processRequest( );
        jamiiDebug.info("Request completed");
        return this.getResponse( );
    }

    /**
     * Failed requests can be handled at the abstract level but successfull will need to be customized per request
     * @return
     */
    public  ResponseEntity< ? > getResponse( ){

        StringBuilder response = new StringBuilder( ) ;
        if( !JamiiError.isEmpty( ) ){
            response.append ( this.JamiiError );
        }

        return new ResponseEntity<>( response.toString(), HttpStatus.OK );
    }


}
