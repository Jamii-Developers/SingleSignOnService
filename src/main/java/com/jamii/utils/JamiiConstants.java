package com.jamii.utils;

/**
 * Central constants class for the Jamii application.
 * 
 * <p>This class contains all application-wide constants, particularly response type codes
 * used for API communication. All constants are public static final and should be accessed
 * directly via the class name.</p>
 * 
 * <p>This is a utility class and cannot be instantiated.</p>
 */
public class JamiiConstants {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * @throws UnsupportedOperationException if instantiation is attempted
     */
    public JamiiConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * RESPONSE TYPE
     * 
     * <p>Constants defining response type codes for API responses.
     * Format: "TYPE|CODE" where TYPE is either ERR (error) or SUC (success).</p>
     */

    /**
     * Error message response type.
     */
    public static final String      RESPONSE_TYPE_ERROR_MESSAGE                 =   "ERR|001";
    
    /**
     * User login success response type.
     */
    public static final String      RESPONSE_TYPE_USERLOGIN                     =   "SUC|001";
    
    /**
     * Create new user success response type.
     */
    public static final String      RESPONSE_TYPE_CREATE_NEW_USER               =   "SUC|002";
    
    /**
     * Edit user data success response type.
     */
    public static final String      RESPONSE_TYPE_EDIT_USER_DATA                =   "SUC|003";
    
    /**
     * Change password success response type.
     */
    public static final String      RESPONSE_TYPE_CHANGE_PASSWORD               =   "SUC|004";
    
    /**
     * Reactivate account success response type.
     */
    public static final String      RESPONSE_TYPE_EDIT_REACTIVATE               =   "SUC|005";
    
    /**
     * Deactivate account success response type.
     */
    public static final String      RESPONSE_TYPE_EDIT_DEACTIVATE               =   "SUC|006";
    
    /**
     * Review user success response type.
     */
    public static final String      RESPONSE_TYPE_REVIEW_US                     =   "SUC|007";
    
    /**
     * Search results success response type.
     */
    public static final String      RESPONSE_SEARCH_RESULTS                     =   "SUC|008";
    
    /**
     * Send friend request success response type.
     */
    public static final String      RESPONSE_SEND_FRIEND_REQUEST                =   "SUC|009";
    
    /**
     * Send follow request success response type.
     */
    public static final String      RESPONSE_SEND_FOLLOW_REQUEST                =   "SUC|010";
    
    /**
     * Accept friend request success response type.
     */
    public static final String      RESPONSE_ACCEPT_FRIEND_REQUEST              =   "SUC|011";
    
    /**
     * Accept follow request success response type.
     */
    public static final String      RESPONSE_ACCEPT_FOLLOW_REQUEST              =   "SUC|012";
    
    /**
     * Reject friend request success response type.
     */
    public static final String      RESPONSE_REJECT_FRIEND_REQUEST              =   "SUC|013";
    
    /**
     * Reject follow request success response type.
     */
    public static final String      RESPONSE_REJECT_FOLLOW_REQUEST              =   "SUC|014";
    
    /**
     * Get friend list success response type.
     */
    public static final String      RESPONSE_GET_FRIEND_LIST_REQUEST            =   "SUC|015";
    
    /**
     * Get follow list success response type.
     */
    public static final String      RESPONSE_GET_FOLLOW_LIST_REQUEST            =   "SUC|016";
    
    /**
     * Get friend request list success response type.
     */
    public static final String      RESPONSE_GET_FRIEND_REQUEST_LIST_REQUEST    =   "SUC|017";
    
    /**
     * Get follow request list success response type.
     */
    public static final String      RESPONSE_GET_FOLLOW_REQUEST_LIST_REQUEST    =   "SUC|018";
    
    /**
     * Block user success response type.
     */
    public static final String      RESPONSE_BLOCK_USER_REQUEST                 =   "SUC|019";
    
    /**
     * Remove friend request success response type.
     * @deprecated Use {@link #RESPONSE_REJECT_FRIEND_REQUEST} instead
     */
    @Deprecated
    public static final String      RESPONSE_REMOVE_FRIEND_REQUEST              =   "SUC|020";
    
    /**
     * Remove follow request success response type.
     * @deprecated Use {@link #RESPONSE_REJECT_FOLLOW_REQUEST} instead
     */
    @Deprecated
    public static final String      RESPONSE_REMOVE_FOLLOW_REQUEST              =   "SUC|021";
    
    /**
     * Get blocked user list success response type.
     */
    public static final String      RESPONSE_GET_BLOCK_USER_LIST_REQUEST        =   "SUC|022";
    
    /**
     * Fetch user data success response type.
     */
    public static final String      RESPONSE_FETCH_USER_DATA                    =   "SUC|023";
    
    /**
     * Logoff success response type.
     */
    public static final String      RESPONSE_LOGOFF                             =   "SUC|024";
    
    /**
     * Contact support success response type.
     */
    public static final String      RESPONSE_CONTACT_SUPPORT                    =   "SUC|025";
    
    /**
     * Reject/remove follower success response type.
     */
    public static final String      RESPONSE_REJECT_REMOVE_FOLLOWER             =   "SUC|026";
    
    /**
     * Unfriend success response type.
     */
    public static final String      RESPONSE_UN_FRIEND                          =   "SUC|027";
    
    /**
     * Unfollow success response type.
     */
    public static final String      RESPONSE_UN_FOLLOW                          =   "SUC|028";
    
    /**
     * View user profile success response type.
     */
    public static final String      RESPONSE_VIEW_USER_PROFILE                  =   "SUC|029";
    
    /**
     * Unblock user success response type.
     */
    public static final String      RESPONSE_UNBLOCK_USER                       =   "SUC|030";



}
