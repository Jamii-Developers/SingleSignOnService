package com.jamii.responses;

import com.jamii.Utils.JamiiErrorUtils;

public class JamiiErrorsMessagesRESP
        extends AbstractResponses
{

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
        setERROR_FIELD_SUBJECT("Device Error!");
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
}
