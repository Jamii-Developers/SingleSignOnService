package com.jamii.Utils;

import com.mysql.cj.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JamiiStringUtils extends StringUtils {

    public JamiiStringUtils() throws IllegalAccessException {
        throw new IllegalAccessException("This is a utility Class");
    }


    /**
     * Compare strings using the equals method to ensure the values are similar.
     *
     * @param s1
     * @param s2
     * @return true or false
     */
    public static boolean equals(String s1, String s2) {
        return getSafeString(s1).equals(getSafeString(s2));
    }

    /**
     * Compares strings using the equals method but ensures that the values are set to lowercase best cases are for username comparison.
     *
     * @param s1
     * @param s2
     * @return true or false
     */

    public static boolean equalsIgnoreCase(String s1, String s2) {
        return equals(s1.toLowerCase(), s2.toLowerCase());
    }

    /**
     * Ths method removes a delimiter from an array of letters.
     *
     * @param array
     * @param delimiter
     * @return String without a delimiter
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
     * This method intakes an empty object and confirms
     *
     * @param obj
     * @return Empty string if null or the original string available
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
     * Applies a default string if the word is not available
     *
     * @param word
     * @param defaultValue
     * @return Default value if word is empty
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
