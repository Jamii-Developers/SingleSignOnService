package com.jamii.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for password encryption and user key generation.
 * 
 * <p>This class provides methods for encrypting passwords using MD5 hashing,
 * comparing passwords, and generating unique user keys.</p>
 */
public class JamiiUserPasswordEncryptTool {

    /**
     * Encrypts text using MD5 hashing algorithm.
     * Reference: https://www.geeksforgeeks.org/java-program-to-encrypt-password-in-configuration-files/
     * @param text the text to be encrypted
     * @return the encrypted text as a hexadecimal string
     */
    public static String doEncrypt( String text ) {

        String encryptedPassword = "";

        try{
            /* MessageDigest instance for MD5. */
            MessageDigest m = MessageDigest.getInstance("MD5");
            /* Add plain-text password bytes to digest using MD5 update() method. */
            m.update( text.getBytes( ) );
            /* Convert the hash value into bytes */
            byte[] bytes = m.digest();
            /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            encryptedPassword = s.toString( );
        }catch ( NoSuchAlgorithmException e ){
            e.printStackTrace( );
        }

        return encryptedPassword;
    }

    /**
     * Compares two passwords by encrypting them and comparing the results.
     * @param password1 the first password to compare
     * @param password2 the second password to compare
     * @return true if the encrypted passwords match, false otherwise
     */
    public static Boolean comparePasswords( String password1, String password2  ) {
        String encryptedPassword1 = doEncrypt( password1 );
        String encryptedPassword2 = doEncrypt( password2 );
        return JamiiStringUtils.equals( encryptedPassword1, encryptedPassword2 );
    }

    /**
     * Encrypts a password using MD5 hashing.
     * @param password1 the password to encrypt
     * @return the encrypted password
     */
    public static String encryptPassword( String password1 ){
        return doEncrypt( password1 );
    }

    /**
     * Generates a unique user key based on username, email address, and creation date.
     * @param username the username
     * @param emailAddress the email address
     * @param date_Created the creation date
     * @return the generated user key
     */
    public static String generateUserKey( String username, String emailAddress, String date_Created ){
        StringBuilder key = new StringBuilder( );
        key.append( doEncrypt( username ) );
        key.append( doEncrypt( emailAddress ) );
        key.append( doEncrypt( date_Created ) );
        return key.toString( );
    }


}
