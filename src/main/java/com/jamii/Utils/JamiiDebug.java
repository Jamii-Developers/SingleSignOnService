package com.jamii.Utils;

import java.util.logging.Logger;

public class JamiiDebug {

    public JamiiDebug( Class<?> LoggerClass ) {
        logger =  Logger.getLogger( LoggerClass.getName( ) ) ;
    }

    protected Logger logger ;

    public void warning( String text ){
        logger.warning( text );
    }

    public void info( String text ){
        logger.info( text );
    }

    public void error( String text ) {
        logger.info( text );
    }

    private void logError( ){

    }

}
