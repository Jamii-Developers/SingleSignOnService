package com.jamii.utils;

import java.util.List;

/**
 * Utility class for string manipulation and comparison operations.
 * 
 * <p>This class provides methods for safe string comparison, delimiter handling,
 * and null-safe string operations.</p>
 * 
 * <p>This is a utility class and cannot be instantiated.</p>
 */
public class JamiiStringUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * @throws IllegalAccessException if instantiation is attempted
     */
    public JamiiStringUtils() throws IllegalAccessException {
        throw new IllegalAccessException("This is a utility Class");
    }


    /**
     * Compares strings using the equals method to ensure the values are similar.
     * @param s1 the first string to compare
     * @param s2 the second string to compare
     * @return true if the strings are equal, false otherwise
     */
    public static boolean equals(String s1, String s2) {
        return getSafeString(s1).equals(getSafeString(s2));
    }

    /**
     * Compares strings using the equals method but ensures that the values are set to lowercase.
     * Best used for username comparison.
     * @param s1 the first string to compare
     * @param s2 the second string to compare
     * @return true if the strings are equal ignoring case, false otherwise
     */
    public static boolean equalsIgnoreCase(String s1, String s2) {
        return equals(s1.toLowerCase(), s2.toLowerCase());
    }

    /**
     * Joins a list of strings with a specified delimiter.
     * @param array the list of strings to join
     * @param delimiter the delimiter to insert between strings
     * @return the joined string with delimiters, or empty string if list is empty
     */
    public static String separateWithDelimiter(List<String> array, String delimiter) {

        if (array.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < array.size(); x++) {

            sb.append(array.get(x));
            if (x < array.size()) {
                sb.append(delimiter);
            }
        }

        return sb.toString();
    }

    /**
     * Converts an object to a safe string representation.
     * @param obj the object to convert
     * @return empty string if null, otherwise the string representation of the object
     */
    public static String getSafeString(Object obj) {
        String str = "";
        if (obj == null) {
            return str;
        }

        if (obj instanceof String) {
            str = (String) obj;
        }

        return str;
    }

    /**
     * Applies a default string if the word is not available or empty.
     * @param word the word to check
     * @param defaultValue the default value to return if word is empty
     * @return the default value if word is empty, otherwise the original word
     */
    public static String getDefaultSafeString(String word, String defaultValue) {
        word = getSafeString(word);
        defaultValue = getSafeString(defaultValue);
        if (word.isEmpty()) {
            return defaultValue;
        }

        return word;
    }
}
