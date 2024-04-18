package com.jamii.Utils;

import com.mysql.cj.util.StringUtils;

import java.util.List;

public class JamiiStringUtils extends StringUtils {

    public JamiiStringUtils( ) throws IllegalAccessException { throw new IllegalAccessException( "This is a utility Class"); }

    public static boolean equals( String s1, String s2){
        return s1.equals( s2 );
    }

    public static String separateWithDelimiter(List< String > array, String delimiter ){

        if( array.size( ) == 0 ){
            return "";
        }

        StringBuilder sb = new StringBuilder( );
        for( int x = 0; x < array.size( ); x++ ){

            sb.append( array.get( x ) );
            if( x < array.size( ) ){
                sb.append( delimiter );
            }
        }

        return sb.toString( );
    }

    public static String getSafeString( Object obj ){
        String str = "";
        if( obj == null ){
            return str;
        }

        if( obj instanceof String ){
            str = (String)obj;
        }

        return str;
    }
}
