package com.jamii.Utils;

import java.util.logging.Logger;

public class JamiiDebug {

    public JamiiDebug(  ) {
        logger =  Logger.getLogger( this.getClass( ).getName( ) ) ;
    }

    protected static Logger logger ;

    public static void warning( String text ){
        logger.warning( text );
    }

    public static void info( String text ){
        logger.info( text );
    }
}
