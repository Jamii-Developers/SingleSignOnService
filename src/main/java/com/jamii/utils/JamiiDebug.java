package com.jamii.utils;

import java.util.logging.Logger;

/**
 * Debug logging utility class for the Jamii application.
 * 
 * <p>This class provides convenient methods for logging debug messages at different levels.
 * It wraps Java's standard Logger functionality.</p>
 */
public class JamiiDebug {

    /**
     * Constructor that initializes the logger for a specific class.
     * @param LoggerClass the class to create a logger for
     */
    public JamiiDebug( Class<?> LoggerClass ) {
        logger =  Logger.getLogger( LoggerClass.getName( ) ) ;
    }

    /**
     * The underlying Java Logger instance.
     */
    protected Logger logger ;

    /**
     * Logs a warning message.
     * @param text the warning message to log
     */
    public void warning( String text ){
        logger.warning( text );
    }

    /**
     * Logs an info message.
     * @param text the info message to log
     */
    public void info( String text ){
        logger.info( text );
    }

    /**
     * Logs an error message.
     * @param text the error message to log
     */
    public void error( String text ) {
        logger.severe( text );
    }

    /**
     * Logs a warning message (alternative method name).
     * @param s the warning message to log
     */
    public void warn( String s ) {
        logger.warning( s );
    }
}
