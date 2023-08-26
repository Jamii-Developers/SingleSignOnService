package com.jamii.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JamiiUserPasswordEncryptTool {

    /**
     *
     * @param text - Test to be encrpted
     * @return - Returns the encrypted text as string
     * @throws NoSuchAlgorithmException
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

    public static Boolean comparePasswords( String password1, String password2  ) {
        String encryptedPassword1 = doEncrypt( password1 );
        String encryptedPassword2 = doEncrypt( password2 );
        return JamiiStringUtils.equals( encryptedPassword1, encryptedPassword2 );
    }

    public static String encryptPassword( String password1 ){
        return doEncrypt( password1 );
    }


}
