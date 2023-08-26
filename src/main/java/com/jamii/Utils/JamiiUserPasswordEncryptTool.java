package com.jamii.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JamiiUserPasswordEncryptTool {

    private String password;
    private String retyped_password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetyped_password() {
        return retyped_password;
    }

    public void setRetyped_password(String retyped_password) {
        this.retyped_password = retyped_password;
    }

    public JamiiUserPasswordEncryptTool(String password) {
        this.password = password;
    }

    public JamiiUserPasswordEncryptTool(String password, String retyped_password) {
        this.password = password;
        this.retyped_password = retyped_password;
    }

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

    public Boolean comparePasswords( ) {
        String password1 = doEncrypt( this.password );
        String password2 = doEncrypt( this.password );
        return JamiiStringUtils.equals( password1, password2 );
    }

    public String encryptPassword( ){
        return doEncrypt( this.password );
    }


}
