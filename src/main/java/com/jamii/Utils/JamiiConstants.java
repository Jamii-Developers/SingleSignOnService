package com.jamii.Utils;

public class JamiiConstants {

    public JamiiConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * RESPONSE TYPE
     */

    public static final String      RESPONSE_TYPE_ERROR_MESSAGE                 =   "ERR|001";
    public static final String      RESPONSE_TYPE_USERLOGIN                     =   "SUC|001";
    public static final String      RESPONSE_TYPE_CREATE_NEW_USER               =   "SUC|002";
    public static final String      RESPONSE_TYPE_EDIT_USER_DATA                =   "SUC|003";
    public static final String      RESPONSE_TYPE_CHANGE_PASSWORD               =   "SUC|004";
    public static final String      RESPONSE_TYPE_EDIT_REACTIVATE               =   "SUC|005";
    public static final String      RESPONSE_TYPE_EDIT_DEACTIVATE               =   "SUC|006";
    public static final String      RESPONSE_TYPE_REVIEW_US                     =   "SUC|007";
    public static final String      RESPONSE_SEARCH_RESULTS                     =   "SUC|008";
    public static final String      RESPONSE_SEND_FRIEND_REQUEST                =   "SUC|009";
    public static final String      RESPONSE_SEND_FOLLOW_REQUEST                =   "SUC|010";
    public static final String      RESPONSE_ACCEPT_FRIEND_REQUEST              =   "SUC|011";
    public static final String      RESPONSE_ACCEPT_FOLLOW_REQUEST              =   "SUC|012";
    public static final String      RESPONSE_REJECT_FRIEND_REQUEST              =   "SUC|013";
    public static final String      RESPONSE_REJECT_FOLLOW_REQUEST              =   "SUC|014";
    public static final String      RESPONSE_GET_FRIEND_LIST_REQUEST            =   "SUC|015";
    public static final String      RESPONSE_GET_FOLLOW_LIST_REQUEST            =   "SUC|016";
    public static final String      RESPONSE_GET_FRIEND_REQUEST_LIST_REQUEST    =   "SUC|017";
    public static final String      RESPONSE_GET_FOLLOW_REQUEST_LIST_REQUEST    =   "SUC|018";
    public static final String      RESPONSE_BLOCK_USER_REQUEST                 =   "SUC|019";
    @Deprecated /** Use RESPONSE_REJECT_FRIEND_REQUEST instead **/
    public static final String      RESPONSE_REMOVE_FRIEND_REQUEST              =   "SUC|020";
    @Deprecated /** Use RESPONSE_REJECT_FOLLOW_REQUEST instead **/
    public static final String      RESPONSE_REMOVE_FOLLOW_REQUEST              =   "SUC|021";
    public static final String      RESPONSE_GET_BLOCK_USER_LIST_REQUEST        =   "SUC|022";
    public static final String      RESPONSE_FETCH_USER_DATA                    =   "SUC|023";
    public static final String      RESPONSE_LOGOFF                             =   "SUC|024";
    public static final String      RESPONSE_CONTACT_SUPPORT                    =   "SUC|025";
    public static final String      RESPONSE_REJECT_REMOVE_FOLLOWER             =   "SUC|026";
    public static final String      RESPONSE_UN_FRIEND                          =   "SUC|027";
    public static final String      RESPONSE_UN_FOLLOW                          =   "SUC|028";
    public static final String      RESPONSE_VIEW_USER_PROFILE                  =   "SUC|029";
    public static final String      RESPONSE_UNBLOCK_USER                       =   "SUC|030";



}
