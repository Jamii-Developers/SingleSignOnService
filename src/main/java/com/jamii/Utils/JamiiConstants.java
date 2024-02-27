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
    public static final String      RESPONSE_ACCEPT_FRIEND_REQUEST      =   "SUC|011";
    public static final String      RESPONSE_ACCEPT_FOLLOW_REQUEST      =   "SUC|012";
    public static final String      RESPONSE_REJECT_FRIEND_REQUEST      =   "SUC|013";
    public static final String      RESPONSE_REJECT_FOLLOW_REQUEST      =   "SUC|014";
    public static final String      RESPONSE_REMOVE_FRIEND_REQUEST      =   "SUC|015";
    public static final String      RESPONSE_REMOVE_FOLLOW_REQUEST      =   "SUC|016";
    public static final String      RESPONSE_BLOCK_FRIEND_REQUEST       =   "SUC|017";
    public static final String      RESPONSE_BLOCK_FOLLOW_REQUEST       =   "SUC|018";
}
