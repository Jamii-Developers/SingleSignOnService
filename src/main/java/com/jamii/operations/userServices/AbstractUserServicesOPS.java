package com.jamii.operations.userServices;

import com.jamii.Utils.JamiiCookieProcessor;
import com.jamii.Utils.JamiiDebug;
import com.jamii.responses.JamiiErrorsMessagesRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractUserServicesOPS {

    @Autowired
    private JamiiCookieProcessor cookie;

    protected String JamiiError;
    protected JamiiErrorsMessagesRESP jamiiErrorsMessagesRESP = null;
    protected final JamiiDebug jamiiDebug = new JamiiDebug( this.getClass( ) );

    protected static String DeviceKey = null;
    public static String getDeviceKey() {return DeviceKey;}
    public static void setDeviceKey(String deviceKey) {DeviceKey = deviceKey;}

    protected static String UserKey = null;
    public static String getUserKey() {return UserKey;}
    public static void setUserKey(String userKey) {UserKey = userKey;}

    protected static String SessionKey = null;
    public static String getSessionKey() {return SessionKey;}
    public static void setSessionKey(String sessionKey) {SessionKey = sessionKey;}

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

    public void reset( ){
        this.JamiiError = "";
        this.jamiiErrorsMessagesRESP = new JamiiErrorsMessagesRESP( );
        setDeviceKey( null );
        setUserKey( null );
        setSessionKey( null );
        setIsSuccessful( true );
    }

    public abstract void processRequest( ) throws Exception;

    public ResponseEntity<?> run( Object requestPayload ) throws Exception{
        jamiiDebug.info("Received request" );
        setRequest( requestPayload );
        validateCookie( );
        processRequest( );
        jamiiDebug.info("Request completed");
        return this.getResponse( );
    }

    public void validateCookie( ) throws Exception{

        //Check if cookie information is available
        if(  DeviceKey==null || UserKey==null || SessionKey == null){
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }

        if(  DeviceKey.isEmpty( ) || UserKey.isEmpty( ) || SessionKey.isEmpty()){
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return;
        }

        //Check if user cookie is valid
        cookie.setUSER_KEY( getUserKey( ) );
        cookie.setDEVICE_KEY( getDeviceKey( ) );
        cookie.setUSER_COOKIE( getSessionKey( ) );

        if( !cookie.checkCookieIsValid( ) ){
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
        }
    }

    public ResponseEntity< ? > getResponse( ){

        StringBuilder response = new StringBuilder( ) ;
        if( !JamiiError.isEmpty( ) ){
            response.append ( this.JamiiError );
        }

        return new ResponseEntity<>( response.toString(), HttpStatus.OK );
    }


}
