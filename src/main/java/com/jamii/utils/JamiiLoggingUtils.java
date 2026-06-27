package com.jamii.utils;

import com.jamii.jAdmin.controllers.ApiErrorLog;
import com.jamii.jAdmin.model.AUXUtils.ApiErrorLog_AUX_DATA;
import com.jamii.jAdmin.model.ApiErrorLogTBL;
import com.jamii.jUser.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Logging utility class for recording exceptions to the database.
 * 
 * <p>This class provides methods to log exceptions with detailed information
 * including stack traces, class names, and associated user information.</p>
 */
@Component
public class JamiiLoggingUtils {

    @Autowired
    private ApiErrorLog errorLog;

    /**
     * Logs an exception to the database with associated user information.
     * @param className the class where the exception occurred
     * @param exception the exception to log
     * @param user the user associated with the exception (can be null)
     */
    public void ExceptionLogger( String className , Exception exception, UserLoginTBL user ){

        errorLog.data = new ApiErrorLogTBL( );

        if( user != null ){
            this.errorLog.data.setUserloginid( user.getId( ) );
        }else{
            this.errorLog.data.setUserloginid( 0 );
        }
        errorLog.data.setCreationdate( LocalDateTime.now( ) );
        errorLog.data.setLastupdated( LocalDateTime.now( ) );
        errorLog.data.setClassname( className );
        errorLog.data.setErrorMessage( exception.getMessage( ) );
        errorLog.data.setErrortype( ApiErrorLog.ERROR_TYPE_EXCEPTION );
        errorLog.data.setStatus( ApiErrorLog.ERROR_TYPE_EXCEPTION );

        ApiErrorLog_AUX_DATA auxData = new ApiErrorLog_AUX_DATA( );
        auxData.setStackTrace(Arrays.toString(exception.getStackTrace( ) ) );

        errorLog.data.setAuxdata( auxData.converToJSON( ) );

        errorLog.save( );

    }
}
