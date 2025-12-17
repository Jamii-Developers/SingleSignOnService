package com.jamii.Utils;

import com.jamii.databaseconfig.controllers.ApiErrorLog;
import com.jamii.databaseconfig.model.AUXUtils.ApiErrorLog_AUX_DATA;
import com.jamii.databaseconfig.model.ApiErrorLogTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class JamiiLoggingUtils {

    @Autowired
    private ApiErrorLog errorLog;

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
