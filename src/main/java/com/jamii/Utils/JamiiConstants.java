package com.jamii.Utils;

public class JamiiConstants {

    /**
     * This section will contains statuses used across the database
     */

    //Process status
    public static final Boolean PROCESS_STATUS_FAILED = false ;
    public static final Boolean PROCESS_STATUS_SUCCESSFUL = true ;


    /**
     * RESPONSE TYPE
     */

    public static final String      RESPONSE_TYPE_ERROR_MESSAGE         =   "ERR|001";
    public static final String      RESPONSE_TYPE_USERLOGIN             =   "SUC|001";
    public static final String      RESPONSE_TYPE_CREATE_NEW_USER       =   "SUC|002";
    public static final String      RESPONSE_TYPE_EDIT_USER_DATA        =   "SUC|003";
    public static final String      RESPONSE_TYPE_CHANGE_PASSWORD       =   "SUC|004";
    public static final String      RESPONSE_TYPE_EDIT_REACTIVATE       =   "SUC|005";
    public static final String      RESPONSE_TYPE_EDIT_DEACTIVATE       =   "SUC|006";
    public static final String      RESPONSE_TYPE_CONTACT_US            =   "SUC|007";
    public static final String      RESPONSE_SEARCH_RESULTS             =   "SUC|008";
    public static final String      RESPONSE_SEND_FRIEND_REQUEST        =   "SUC|009";
    public static final String      RESPONSE_SEND_FOLLOW_REQUEST        =   "SUC|010";
}
