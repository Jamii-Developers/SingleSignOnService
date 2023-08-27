package com.jamii.Utils;

import com.mysql.cj.util.StringUtils;

import java.time.LocalDate;

public class JamiiStringUtils extends StringUtils {

    public JamiiStringUtils( ) throws IllegalAccessException { throw new IllegalAccessException( "This is a utility Class"); }

    public static boolean equals( String s1, String s2){
        return s1.equals( s2 );
    }
}
