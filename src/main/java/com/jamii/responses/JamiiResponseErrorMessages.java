package com.jamii.responses;

import com.jamii.Utils.JamiiConstants;

import java.util.HashMap;

public class JamiiResponseErrorMessages {

    public static final String ERROR_FIELD_SUBJECT          = "ERROR_SUBJECT";
    public static final String ERROR_FIELD_MESSAGE          = "ERROR_MESSAGE";
    public static final String ERROR_FIELD_CODE             = "ERROR_CODE";
    public static final String MESSAGE_TYPE                 = "MSGTYPE";
    public static final String RESPONSE_TYPE                = JamiiConstants.RESPONSE_TYPE_ERROR_MESSAGE;


    public static final String ERROR_CODE_0001 = "0001";
    public static final String ERROR_CODE_0002 = "0002";
    public static HashMap< String, String > loginError( ){

        HashMap< String, String > message= new HashMap< >( );
        message.put( MESSAGE_TYPE, RESPONSE_TYPE );
        message.put( ERROR_FIELD_SUBJECT , "Login Error" );
        message.put( ERROR_FIELD_MESSAGE, "The credentials are not valid" );
        message.put( ERROR_FIELD_CODE, ERROR_CODE_0001 );
        return message;
    }

    public static HashMap< String, String > createNewUserError( ){

        HashMap< String, String > message= new HashMap< >( );
        message.put( MESSAGE_TYPE, RESPONSE_TYPE );
        message.put( ERROR_FIELD_SUBJECT , "Creating new user Error" );
        message.put( ERROR_FIELD_MESSAGE, "The username or email address provided are already in the system" );
        message.put( ERROR_FIELD_CODE, ERROR_CODE_0002 );
        return message;
    }
}
