package com.jamii.Utils;

import java.util.logging.Logger;

public class JamiiDebug {

    public JamiiDebug( Class<?> LoggerClass ) {
        logger =  Logger.getLogger( LoggerClass.getName( ) ) ;
    }

    protected static Logger logger ;

    public static void warning( String text ){
        logger.warning( text );
    }

    public static void info( String text ){
        logger.info( text );
    }

    public static void error( String text ) {
        logger.info( text );
    }

    private void logError( ){

    }

}
