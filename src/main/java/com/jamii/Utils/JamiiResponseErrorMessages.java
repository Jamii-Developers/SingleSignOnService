package com.jamii.Utils;

import java.util.HashMap;

public class JamiiResponseErrorMessages {

    public static final String ERROR_FIELD_SUBJECT          = "ERROR_SUBJECT";
    public static final String ERROR_FIELD_MESSAGE          = "ERROR_MESSAGE";
    public static final String ERROR_FIELD_CODE             = "ERROR_CODE";


    public static final String ERROR_CODE_0001 = "0001";
    public static HashMap< String, String > loginError( ){

        HashMap< String, String > message= new HashMap< >( );
        message.put( ERROR_FIELD_SUBJECT , "Login Error" );
        message.put( ERROR_FIELD_MESSAGE, "The credentials are not valid" );
        message.put( ERROR_FIELD_CODE, ERROR_CODE_0001 );
        return message;
    }
}
