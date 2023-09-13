package com.jamii.Utils;

import java.util.Random;

public class JamiiRandomKeyToolGen {

    private int len;
    private boolean include_numbers;
    private boolean include_letters;
    private boolean include_special_chars;


    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public boolean isInclude_numbers() {
        return include_numbers;
    }

    public void setInclude_numbers(boolean include_numbers) {
        this.include_numbers = include_numbers;
    }

    public boolean isInclude_letters() {
        return include_letters;
    }

    public void setInclude_letters(boolean include_letters) {
        this.include_letters = include_letters;
    }

    public boolean isInclude_special_chars() {
        return include_special_chars;
    }

    public void setInclude_special_chars(boolean include_special_chars) {
        this.include_special_chars = include_special_chars;
    }

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

            for( int x = key.toString( ).length( )  ; x < getLen( ); x++ ){

               int val = rand.nextInt(3 );

               if( include_numbers && val == 0 ){
                   int charval = rand.nextInt( includeNumbers.length( ) );
                   key.append( includeNumbers.charAt(charval));
               }

                if( include_letters && val == 1 ){
                    int charval = rand.nextInt( includeAlphabet.length( ) );
                    key.append( includeAlphabet.charAt(charval));
                }

                if( include_special_chars && val == 2 ){
                    int charval = rand.nextInt( includeSpecialChars.length( ) );
                    key.append( includeSpecialChars.charAt( charval) );
                }

            }

            return key.toString( );
        }catch( Exception e ){
            e.printStackTrace( );
        }

        return "";
    }
}
