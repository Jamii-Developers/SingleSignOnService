package com.jamii.utils;

import com.jamii.abstractClasses.AbstractResponses;

/**
 * Error response utility class for the Jamii application.
 * 
 * <p>This class provides standardized error message formatting and response generation
 * for all error scenarios throughout the application. Each error method sets the
 * appropriate error subject and message for specific error conditions.</p>
 * 
 * <p>Usage pattern:</p>
 * <pre>
 * JamiiErrorsMessagesRESP errorResponse = new JamiiErrorsMessagesRESP();
 * errorResponse.setLoginError();
 * String jsonResponse = errorResponse.getJSONRESP();
 * </pre>
 * 
 * <p>This class extends {@link AbstractResponses} to inherit JSON response generation
 * capabilities and uses constants from {@link JamiiErrorUtils} for consistent error codes.</p>
 * 
 * <p>Performance considerations:</p>
 * <ul>
 *   <li>This class maintains no state that requires database calls</li>
 *   <li>All error messages are static strings for optimal performance</li>
 *   <li>JSON generation is handled efficiently by the parent class</li>
 * </ul>
 */
public class JamiiErrorsMessagesRESP
        extends AbstractResponses
{

    /** The error message type constant from JamiiErrorUtils */
    private final String ERROR_MSG_TYPE = JamiiErrorUtils.RESPONSE_TYPE;

    private String ERROR_FIELD_SUBJECT;
    private String ERROR_FIELD_MESSAGE;

    public String getERROR_FIELD_SUBJECT()
    {
        return ERROR_FIELD_SUBJECT;
    }

    public void setERROR_FIELD_SUBJECT(String ERROR_FIELD_SUBJECT)
    {
        this.ERROR_FIELD_SUBJECT = ERROR_FIELD_SUBJECT;
    }

    public String getERROR_FIELD_MESSAGE()
    {
        return ERROR_FIELD_MESSAGE;
    }

    public void setERROR_FIELD_MESSAGE(String ERROR_FIELD_MESSAGE)
    {
        this.ERROR_FIELD_MESSAGE = ERROR_FIELD_MESSAGE;
    }

    public void setLoginError()
    {
        setERROR_FIELD_SUBJECT("Login Error!");
        setERROR_FIELD_MESSAGE("The credentials are not valid");
    }

    public void createNewUserError()
    {
        setERROR_FIELD_SUBJECT("User Sign Up Error!");
        setERROR_FIELD_MESSAGE("The username or email address provided are already in the system");
    }

    public void setPasswordChange_UsernameOrEmailAddressDoesNotExist()
    {
        setERROR_FIELD_SUBJECT("Password Change Error!");
        setERROR_FIELD_MESSAGE("The username or email address provided does not exist in the system");
    }

    public void setPasswordChange_PasswordsNotMatching()
    {
        setERROR_FIELD_SUBJECT("Password Change Error!");
        setERROR_FIELD_MESSAGE("The passwords does not match what we currently have in the system");
    }

    public void setPasswordChange_PasswordMatchesLastTen()
    {
        setERROR_FIELD_SUBJECT("Password Change Error!");
        setERROR_FIELD_MESSAGE("The password matches the last ten");
    }

    public void setDeactivateUser_UsernameOrEmailAddressDoesNotExist()
    {
        setERROR_FIELD_SUBJECT("Account Deactivation Error!");
        setERROR_FIELD_MESSAGE("The username or email address provided does not exist in the system");
    }

    public void setDeactivateUser_PasswordsNotMatching()
    {
        setERROR_FIELD_SUBJECT("Account Deactivation Error!");
        setERROR_FIELD_MESSAGE("The passwords does not match what we currently have in the system");
    }

    public void setReactivateUser_UsernameOrEmailAddressDoesNotExist()
    {
        setERROR_FIELD_SUBJECT("Account Reactivation Error!");
        setERROR_FIELD_MESSAGE("No deactivated username and email address matches the information provided");
    }

    public void setReactivateUser_PasswordsNotMatching()
    {
        setERROR_FIELD_SUBJECT("Account Reactivation Error!");
        setERROR_FIELD_MESSAGE("The passwords does not match what we currently have in the system");
    }

    public void setEditUserData_UserKeyDoesNotExist()
    {
        setERROR_FIELD_SUBJECT("Profile Update Error!");
        setERROR_FIELD_MESSAGE("The system is not familiar with User Key");
    }

    public void setEditUserData_PasswordMatching()
    {
        setERROR_FIELD_SUBJECT("Profile Update Error!");
        setERROR_FIELD_MESSAGE("The passwords does not match what we currently have in the system");
    }

    public void setUploadFileOPS_NoMatchingUserKey()
    {
        setERROR_FIELD_SUBJECT("Upload File Failure!");
        setERROR_FIELD_MESSAGE("We don't recognize this user. Please attempt Logging in again and uploading your file");
    }

    public void setUploadFileOPS_NoMatchingDeviceKey()
    {
        setERROR_FIELD_SUBJECT("Upload File Failure!");
        setERROR_FIELD_MESSAGE("We don't recognize this device. Please attempt Logging in again and uploading your file");
    }

    public void setDownloadFileOPS_NoMatchingUserKey()
    {
        setERROR_FIELD_SUBJECT("Download File Failure!");
        setERROR_FIELD_MESSAGE("We don't recognize this user. Please attempt Logging in again and downloading your file");
    }

    public void setDownloadFileOPS_NoMatchingDeviceKey()
    {
        setERROR_FIELD_SUBJECT("Download File Failure!");
        setERROR_FIELD_MESSAGE("We don't recognize this device. Please attempt Logging in again and uploading your file");
    }

    public void setDownloadFileOPS_NoActiveFileFound()
    {
        setERROR_FIELD_SUBJECT("Download File Failure!");
        setERROR_FIELD_MESSAGE("The file has been deleted or no longer exists in our system");
    }

    public void setDownloadFileOPS_OopsWeCannotFindThisFile()
    {
        setERROR_FIELD_SUBJECT("Download File Failure!");
        setERROR_FIELD_MESSAGE("Oops! Our system run into an issue please try finding downloading this file again");
    }

    public void setUserFileDirectoryUpdateOPS_FileIsAlreadyInThisDirectory()
    {
        setERROR_FIELD_SUBJECT("File Directory Update!");
        setERROR_FIELD_MESSAGE("This file is already in current directory");
    }

    public void setUserFileDirectoryOPS_FileIsInTrash()
    {
        setERROR_FIELD_SUBJECT("File Directory Update!");
        setERROR_FIELD_MESSAGE("The file you selected is in your trash bin");
    }

    public void setUserFileDirectoryOPS_NoMatchingUserKey()
    {
        setERROR_FIELD_SUBJECT("File Directory Update!");
        setERROR_FIELD_MESSAGE("We don't recognize this user. Please attempt logging in again and downloading your file");
    }

    public void setUserFileDirectory_NoMatchingDeviceKey()
    {
        setERROR_FIELD_SUBJECT("File Directory Update!");
        setERROR_FIELD_MESSAGE("We don't recognize this device. Please attempt Logging in again and uploading your file");
    }

    public void setUserFileDeleteOPS_NoMatchingUserKey()
    {
        setERROR_FIELD_SUBJECT("File Deletion!");
        setERROR_FIELD_MESSAGE("We don't recognize this device. Please attempt Logging in again and uploading your file");
    }

    public void setUserFileDeleteOPS_NoMatchingDeviceKey()
    {
        setERROR_FIELD_SUBJECT("File Deletion!");
        setERROR_FIELD_MESSAGE("We don't recognize this device. Please attempt Logging in again and uploading your file");
    }

    public void setUserFileDeleteOPS_FileIsInTrash()
    {
        setERROR_FIELD_SUBJECT("File Deletion!");
        setERROR_FIELD_MESSAGE("The file you selected is in your trash bin or has been deleted");
    }

    public void setContactUsOPS_UserNotFound()
    {
        setERROR_FIELD_SUBJECT("Contact Us Error!");
        setERROR_FIELD_MESSAGE("The username or email address is not in our system.");
    }

    public void setSearchUserOPS_DeviceNotFound()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("This device is not registered or your session has expired. Please try and login again");
    }

    public void setSendFriendRequestOPS_FriendRequestIsAlreadyAvailable()
    {
        setERROR_FIELD_SUBJECT("Error!");
        setERROR_FIELD_MESSAGE("A friend request has already been sent this user");
    }

    public void setSendFriendRequestOPS_FriendRequestHasBeenSentByTheReceiver()
    {
        setERROR_FIELD_SUBJECT("Error!");
        setERROR_FIELD_MESSAGE("A friend request has already been sent to you by this user");
    }

    public void setSendFriendRequestOPS_BlockedUserVagueResponse()
    {
        setERROR_FIELD_SUBJECT("Error!");
        setERROR_FIELD_MESSAGE("You cannot add this user");
    }

    public void setSendFriendRequestOPS_YouHaveBlockedThisUser()
    {
        setERROR_FIELD_SUBJECT("Error!");
        setERROR_FIELD_MESSAGE("You have blocked this user");
    }

    public void setSendFollowRequestOPS_BlockedUserVagueResponse()
    {
        setERROR_FIELD_SUBJECT("Error!");
        setERROR_FIELD_MESSAGE("You cannot follow this user");
    }

    public void setSendFollowRequestOPS_YouHaveBlockedThisUser()
    {
        setERROR_FIELD_SUBJECT("Error!");
        setERROR_FIELD_MESSAGE("You have blocked this user");
    }

    public void setSendFollowRequestOPS_AlreadyFollowingTheUser()
    {
        setERROR_FIELD_SUBJECT("Error!");
        setERROR_FIELD_MESSAGE("You are already following this user");
    }

    public void setSendFriendRequestOPS_AreAlreadyFriends()
    {
        setERROR_FIELD_SUBJECT("Error!");
        setERROR_FIELD_MESSAGE("You are already friends with this user");
    }

    public void setSendFollowRequestOPS_PendingFollowRequest()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("You already have a pending follow request");
    }

    public void setGetFriendList_NoFriends()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("Seems you have do not friends on your list");
    }

    public void setGetFollowList_NoFollowers()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("Seems you have do not follows on your list");
    }

    public void setGetFollowRequestList_NoNewFollowRequests()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("Seems you have do not any new follow requests");
    }

    public void setGetFriendRequestList_NoNewFriendRequests()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("Seems you have do not any new friend requests");
    }

    public void setGetBlockUserList_NoBlockedUsers()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("Seems you have not blocked anyone");
    }

    public void setFetchUserData_NoData()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("We don't have any information stored in our database about your profile");
    }

    public void setUnBlockUserOPS()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("This user is not blocked currently");
    }

    public void set_RemoveFollowerOPS_NotFriends()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("Seems you are the user is currently not following you");
    }

    public void set_Unfollow_NotFollowing()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("Seems you are not following this user");
    }

    public void set_UnFriend_NotFriends()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("Seems you are not friends with this user");
    }

    public void setGenericErrorMessage()
    {
        setERROR_FIELD_SUBJECT("Oops!");
        setERROR_FIELD_MESSAGE("Something went wrong on ourside! Please try again");
    }

    public void setSearchUserOPS_UserNotFound() {
        setERROR_FIELD_SUBJECT("User not found!");
        setERROR_FIELD_MESSAGE("We cannot find this user in our system");
    }

    public void setCreateNewPost_InvalidContent() {
        setERROR_FIELD_SUBJECT("CREATE_NEW_POST_INVALID_CONTENT");
        setERROR_FIELD_MESSAGE("Post content cannot be empty or exceed maximum length");
    }

    public void setCreateNewPost_PostCreationFailed() {
        setERROR_FIELD_SUBJECT("CREATE_NEW_POST_FAILED");
        setERROR_FIELD_MESSAGE("Failed to create post. Please try again.");
    }

    public void setDeletePost_InvalidPostId() {
        setERROR_FIELD_SUBJECT("DELETE_POST_INVALID_ID");
        setERROR_FIELD_MESSAGE("Invalid post ID provided");
    }

    public void setDeletePost_PostNotFound() {
        setERROR_FIELD_SUBJECT("DELETE_POST_NOT_FOUND");
        setERROR_FIELD_MESSAGE("Post not found or already deleted");
    }

    public void setDeletePost_Unauthorized() {
        setERROR_FIELD_SUBJECT("DELETE_POST_UNAUTHORIZED");
        setERROR_FIELD_MESSAGE("You are not authorized to delete this post");
    }

    public void setEditPost_InvalidPostId() {
        setERROR_FIELD_SUBJECT("EDIT_POST_INVALID_ID");
        setERROR_FIELD_MESSAGE("Invalid post ID provided");
    }

    public void setEditPost_PostNotFound() {
        setERROR_FIELD_SUBJECT("EDIT_POST_NOT_FOUND");
        setERROR_FIELD_MESSAGE("Post not found or has been deleted");
    }

    public void setEditPost_Unauthorized() {
        setERROR_FIELD_SUBJECT("EDIT_POST_UNAUTHORIZED");
        setERROR_FIELD_MESSAGE("You are not authorized to edit this post");
    }

    public void setEditPost_InvalidContent() {
        setERROR_FIELD_SUBJECT("EDIT_POST_INVALID_CONTENT");
        setERROR_FIELD_MESSAGE("Post content cannot be empty or exceed maximum length");
    }

    public void setEditPost_UpdateFailed() {
        setERROR_FIELD_SUBJECT("EDIT_POST_UPDATE_FAILED");
        setERROR_FIELD_MESSAGE("Failed to update post. Please try again.");
    }
}
