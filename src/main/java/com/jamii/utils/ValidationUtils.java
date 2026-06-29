package com.jamii.utils;

import java.util.regex.Pattern;

/**
 * Utility class for common validation operations across the application.
 * 
 * <p>This class provides static methods for validating various types of input
 * including email addresses, usernames, passwords, and other common data formats.
 * All validation methods return boolean values and are safe to use in any context.</p>
 * 
 * <p>Usage examples:</p>
 * <pre>{@code
 * if (ValidationUtils.isValidEmail(email)) {
 *     // Process valid email
 * }
 * 
 * if (ValidationUtils.isValidUsername(username)) {
 *     // Process valid username
 * }
 * }</pre>
 */
public class ValidationUtils {

    // Email validation pattern (RFC 5322 compliant)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    // Username validation pattern (alphanumeric, underscore, 3-50 characters)
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,50}$"
    );

    // Device key validation pattern (alphanumeric, hyphen, 10-200 characters)
    private static final Pattern DEVICE_KEY_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9-]{10,200}$"
    );

    // Session key validation pattern (alphanumeric, 20-255 characters)
    private static final Pattern SESSION_KEY_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9]{20,255}$"
    );

    /**
     * Validates an email address format.
     * 
     * @param email the email address to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates a username format.
     * 
     * @param username the username to validate
     * @return true if the username is valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Validates a password strength.
     * 
     * <p>Password must be at least 8 characters long and contain at least:
     * one uppercase letter, one lowercase letter, one digit, and one special character.</p>
     * 
     * @param password the password to validate
     * @return true if the password meets strength requirements, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    /**
     * Validates a device key format.
     * 
     * @param deviceKey the device key to validate
     * @return true if the device key is valid, false otherwise
     */
    public static boolean isValidDeviceKey(String deviceKey) {
        if (deviceKey == null || deviceKey.trim().isEmpty()) {
            return false;
        }
        return DEVICE_KEY_PATTERN.matcher(deviceKey.trim()).matches();
    }

    /**
     * Validates a session key format.
     * 
     * @param sessionKey the session key to validate
     * @return true if the session key is valid, false otherwise
     */
    public static boolean isValidSessionKey(String sessionKey) {
        if (sessionKey == null || sessionKey.trim().isEmpty()) {
            return false;
        }
        return SESSION_KEY_PATTERN.matcher(sessionKey.trim()).matches();
    }

    /**
     * Validates that a string is not null or empty after trimming.
     * 
     * @param value the string to validate
     * @return true if the string is not null or empty, false otherwise
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates that a string length is within the specified bounds.
     * 
     * @param value the string to validate
     * @param minLength the minimum allowed length (inclusive)
     * @param maxLength the maximum allowed length (inclusive)
     * @return true if the string length is within bounds, false otherwise
     */
    public static boolean isValidLength(String value, int minLength, int maxLength) {
        if (value == null) {
            return false;
        }
        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Validates post content length and format.
     * 
     * @param content the post content to validate
     * @return true if the content is valid, false otherwise
     */
    public static boolean isValidPostContent(String content) {
        return content != null && 
               !content.trim().isEmpty() && 
               content.trim().length() <= 2000;
    }

    /**
     * Validates a user key format.
     * 
     * @param userKey the user key to validate
     * @return true if the user key is valid, false otherwise
     */
    public static boolean isValidUserKey(String userKey) {
        return isNotEmpty(userKey) && 
               isValidLength(userKey, 10, 1000);
    }

    /**
     * Validates that all authentication keys are present and valid.
     * 
     * @param userKey the user key
     * @param deviceKey the device key
     * @param sessionKey the session key
     * @return true if all keys are valid, false otherwise
     */
    public static boolean areAuthenticationKeysValid(String userKey, String deviceKey, String sessionKey) {
        return isValidUserKey(userKey) && 
               isValidDeviceKey(deviceKey) && 
               isValidSessionKey(sessionKey);
    }

    /**
     * Sanitizes a string by trimming whitespace and null-checking.
     * 
     * @param value the string to sanitize
     * @return the sanitized string or null if input was null
     */
    public static String sanitize(String value) {
        return value != null ? value.trim() : null;
    }

    /**
     * Validates that a string contains only alphanumeric characters.
     * 
     * @param value the string to validate
     * @return true if the string contains only alphanumeric characters, false otherwise
     */
    public static boolean isAlphanumeric(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return value.trim().matches("^[a-zA-Z0-9]+$");
    }
}
