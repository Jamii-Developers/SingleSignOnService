package com.jamii.Utils;

import java.util.Random;

/**
 * Utility class for generating random keys with customizable character sets.
 * 
 * <p>This class generates random keys that can include numbers, letters,
 * and special characters. The generated keys are encrypted before being returned.</p>
 */
public class JamiiRandomKeyToolGen {

    private int len;
    private boolean include_numbers;
    private boolean include_letters;
    private boolean include_special_chars;

    /**
     * Gets the length of the key to generate.
     * @return the key length
     */
    public int getLen() {
        return len;
    }

    /**
     * Sets the length of the key to generate.
     * @param len the key length to set
     */
    public void setLen(int len) {
        this.len = len;
    }

    /**
     * Gets whether to include numbers in the key.
     * @return true if numbers should be included
     */
    public boolean isInclude_numbers() {
        return include_numbers;
    }

    /**
     * Sets whether to include numbers in the key.
     * @param include_numbers true to include numbers
     */
    public void setInclude_numbers(boolean include_numbers) {
        this.include_numbers = include_numbers;
    }

    /**
     * Gets whether to include letters in the key.
     * @return true if letters should be included
     */
    public boolean isInclude_letters() {
        return include_letters;
    }

    /**
     * Sets whether to include letters in the key.
     * @param include_letters true to include letters
     */
    public void setInclude_letters(boolean include_letters) {
        this.include_letters = include_letters;
    }

    /**
     * Gets whether to include special characters in the key.
     * @return true if special characters should be included
     */
    public boolean isInclude_special_chars() {
        return include_special_chars;
    }

    /**
     * Sets whether to include special characters in the key.
     * @param include_special_chars true to include special characters
     */
    public void setInclude_special_chars(boolean include_special_chars) {
        this.include_special_chars = include_special_chars;
    }

    /**
     * Generates a random key based on the configured options.
     * 
     * <p>The key is generated using the configured character sets (numbers, letters,
     * special characters) and then encrypted before being returned.</p>
     * @return the encrypted random key, or empty string if generation fails
     */
    public String generate( ){

        try{
            StringBuilder key = new StringBuilder( );

            if( len == 0){
                throw new RuntimeException("You need to provide a length to your key greater than 0");
            }

            if( !include_numbers & !include_letters & !include_special_chars){
                throw new RuntimeException("You need to include one type of options provided");
            }

            String includeNumbers = "1234567890";
            String includeAlphabet = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
            String includeSpecialChars = ",.:';[]{}-=_+!@#$%^&*()*`~";

            Random rand = new Random( );

            int count = 0;
            while( count < getLen( ) ){

               int val = rand.nextInt(3 );

               if( include_numbers && val == 0 ){
                   int charval = rand.nextInt( includeNumbers.length( ) );
                   key.append( includeNumbers.charAt(charval));
                   count++;
               }

                if( include_letters && val == 1 ){
                    int charval = rand.nextInt( includeAlphabet.length( ) );
                    key.append( includeAlphabet.charAt(charval));
                    count++;
                }

                if( include_special_chars && val == 2 ){
                    int charval = rand.nextInt( includeSpecialChars.length( ) );
                    key.append( includeSpecialChars.charAt( charval) );
                    count++;
                }

            }

            return JamiiUserPasswordEncryptTool.encryptPassword( key.toString( ) );
        }catch( Exception e ){
            e.printStackTrace( );
        }

        return "";
    }
}
