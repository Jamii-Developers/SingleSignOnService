package com.jamii.utils;

import java.time.format.DateTimeFormatter;

/**
 * Utility class for date formatting and manipulation.
 * 
 * <p>This class provides date formatters used throughout the application,
 * particularly for cookie expiration dates.</p>
 * 
 * <p>This is a utility class and cannot be instantiated.</p>
 */
public class JamiiDateUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * @throws UnsupportedOperationException if instantiation is attempted
     */
    public JamiiDateUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Date Formatters
     * 
     * <p>Pre-configured DateTimeFormatter instances for common date formats.</p>
     */

    /**
     * RFC 1123 date time formatter for cookie dates.
     */
    public static final DateTimeFormatter COOKIE_DATE = DateTimeFormatter.RFC_1123_DATE_TIME;
}
