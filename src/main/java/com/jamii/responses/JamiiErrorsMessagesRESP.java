package com.jamii.responses;

import com.jamii.Utils.JamiiErrorUtils;

public class JamiiErrorsMessagesRESP extends AbstractResponses {


    private String ERROR_MSG_TYPE = JamiiErrorUtils.RESPONSE_TYPE;

    private String ERROR_FIELD_SUBJECT;
    private String ERROR_FIELD_MESSAGE;
    private String ERROR_FIELD_CODE;

    public String getERROR_FIELD_SUBJECT() {
        return ERROR_FIELD_SUBJECT;
    }

    public void setERROR_FIELD_SUBJECT(String ERROR_FIELD_SUBJECT) {
        this.ERROR_FIELD_SUBJECT = ERROR_FIELD_SUBJECT;
    }

    public String getERROR_FIELD_MESSAGE() {
        return ERROR_FIELD_MESSAGE;
    }

    public void setERROR_FIELD_MESSAGE(String ERROR_FIELD_MESSAGE) {
        this.ERROR_FIELD_MESSAGE = ERROR_FIELD_MESSAGE;
    }

    public String getERROR_FIELD_CODE() {
        return ERROR_FIELD_CODE;
    }

    public void setERROR_FIELD_CODE(String ERROR_FIELD_CODE) {
        this.ERROR_FIELD_CODE = ERROR_FIELD_CODE;
    }

    public void setLoginError( ){
        setERROR_FIELD_SUBJECT( "Login Error!"  );
        setERROR_FIELD_MESSAGE("The credentials are not valid" );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0001 ) ;
    }

    public void createNewUserError( ){
        setERROR_FIELD_SUBJECT( "User Sign Up Error!"  );
        setERROR_FIELD_MESSAGE( "The username or email address provided are already in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0002 ) ;
    }

    public void setPasswordChange_UsernameOrEmailAddressDoesNotExist( ){
        setERROR_FIELD_SUBJECT( "Password Change Error!"  );
        setERROR_FIELD_MESSAGE( "The username or email address provided does not exist in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0003 ) ;
    }

    public void setPasswordChange_PasswordsNotMatching( ){
        setERROR_FIELD_SUBJECT( "Password Change Error!"  );
        setERROR_FIELD_MESSAGE( "The passwords does not match what we currently have in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0004) ;

    }
    public void setPasswordChange_PasswordMatchesLastTen( ){
        setERROR_FIELD_SUBJECT( "Password Change Error!"  );
        setERROR_FIELD_MESSAGE( "The password matches the last ten"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0005 ) ;
    }

    public void setDeactivateUser_UsernameOrEmailAddressDoesNotExist( ){
        setERROR_FIELD_SUBJECT( "Account Deactivation Error!"  );
        setERROR_FIELD_MESSAGE( "The username or email address provided does not exist in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0006 ) ;
    }


    public void setDeactivateUser_PasswordsNotMatching( ){
        setERROR_FIELD_SUBJECT( "Account Deactivation Error!"  );
        setERROR_FIELD_MESSAGE( "The passwords does not match what we currently have in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0007 ) ;
    }

    public void setReactivateUser_UsernameOrEmailAddressDoesNotExist( ){
        setERROR_FIELD_SUBJECT( "Account Reactivation Error!"  );
        setERROR_FIELD_MESSAGE( "No deactivated username and email address matches the information provided"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0008 ) ;
    }

    public void setReactivateUser_PasswordsNotMatching( ){
        setERROR_FIELD_SUBJECT( "Account Reactivation Error!"  );
        setERROR_FIELD_MESSAGE( "The passwords does not match what we currently have in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0009 ) ;
    }

    public void setEditUserData_UserKeyDoesNotExist( ){
        setERROR_FIELD_SUBJECT( "Profile Update Error!"  );
        setERROR_FIELD_MESSAGE( "The system is not familiar with User Key"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0010 ) ;
    }

    public void setEditUserData_PasswordMatching( ){
        setERROR_FIELD_SUBJECT( "Profile Update Error!"  );
        setERROR_FIELD_MESSAGE( "The passwords does not match what we currently have in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0011 ) ;
    }

    public void setUploadFileOPS_NoMatchingUserKey( ){
        setERROR_FIELD_SUBJECT( "Upload File Failure!"  );
        setERROR_FIELD_MESSAGE( "We don't recognize this user. Please attempt Logging in again and uploading your file"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0012 ) ;
    }

    public void setUploadFileOPS_NoMatchingDeviceKey( ){
        setERROR_FIELD_SUBJECT( "Upload File Failure!"  );
        setERROR_FIELD_MESSAGE( "We don't recognize this device. Please attempt Logging in again and uploading your file"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0013 ) ;
    }

    public void setDownloadFileOPS_NoMatchingUserKey( ){
        setERROR_FIELD_SUBJECT( "Download File Failure!"  );
        setERROR_FIELD_MESSAGE( "We don't recognize this user. Please attempt Logging in again and downloading your file"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0014 ) ;
    }

    public void setDownloadFileOPS_NoMatchingDeviceKey( ){
        setERROR_FIELD_SUBJECT( "Download File Failure!"  );
        setERROR_FIELD_MESSAGE( "We don't recognize this device. Please attempt Logging in again and uploading your file"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0015 ) ;
    }

    public void setDownloadFileOPS_NoActiveFileFound( ){
        setERROR_FIELD_SUBJECT( "Download File Failure!"  );
        setERROR_FIELD_MESSAGE( "The file has been deleted or no longer exists in our system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0016 ) ;
    }

    public void setDownloadFileOPS_OopsWeCannotFindThisFile( ){
        setERROR_FIELD_SUBJECT( "Download File Failure!"  );
        setERROR_FIELD_MESSAGE( "Oops! Our system run into an issue please try finding downloading this file again"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0017 ) ;
    }

    public void setUserFileDirectoryUpdateOPS_FileIsAlreadyInThisDirectory( ){
        setERROR_FIELD_SUBJECT( "File Directory Update!"  );
        setERROR_FIELD_MESSAGE( "This file is already in current directory"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0018 ) ;
    }

    public void setUserFileDirectoryOPS_FileIsInTrash() {
        setERROR_FIELD_SUBJECT( "File Directory Update!"  );
        setERROR_FIELD_MESSAGE( "The file you selected is in your trash bin"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0019 ) ;
    }

    public void setUserFileDirectoryOPS_NoMatchingUserKey( ){
        setERROR_FIELD_SUBJECT( "File Directory Update!"  );
        setERROR_FIELD_MESSAGE( "We don't recognize this user. Please attempt logging in again and downloading your file"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0020 ) ;
    }

    public void setUserFileDirectory_NoMatchingDeviceKey( ){
        setERROR_FIELD_SUBJECT( "File Directory Update!"  );
        setERROR_FIELD_MESSAGE( "We don't recognize this device. Please attempt Logging in again and uploading your file"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0021 ) ;
    }

    public void setUserFileDeleteOPS_NoMatchingUserKey() {
        setERROR_FIELD_SUBJECT( "File Deletion!"  );
        setERROR_FIELD_MESSAGE( "We don't recognize this device. Please attempt Logging in again and uploading your file"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0022 ) ;
    }

    public void setUserFileDeleteOPS_NoMatchingDeviceKey() {
        setERROR_FIELD_SUBJECT( "File Deletion!"  );
        setERROR_FIELD_MESSAGE( "We don't recognize this device. Please attempt Logging in again and uploading your file"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0023 ) ;
    }
    public void setUserFileDeleteOPS_FileIsInTrash() {
        setERROR_FIELD_SUBJECT( "File Deletion!"  );
        setERROR_FIELD_MESSAGE( "The file you selected is in your trash bin or has been deleted"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0024 ) ;
    }

    public void setContactUsOPS_UserNotFound() {
        setERROR_FIELD_SUBJECT( "Contact Us Error!"  );
        setERROR_FIELD_MESSAGE( "The username or email address is not in our system."  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0025 ) ;
    }

    public void setSearchUserOPS_DeviceNotFound() {
        setERROR_FIELD_SUBJECT( "Device Error!"  );
        setERROR_FIELD_MESSAGE( "This device is not registered or your session has expired. Please try and login again"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0026 ) ;
    }

    public void setSendFriendRequestOPS_FriendRequestIsAlreadyAvailable() {
        setERROR_FIELD_SUBJECT( "Error!"  );
        setERROR_FIELD_MESSAGE( "A friend request has already been sent this user"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0027 ) ;
    }

    public void setSendFriendRequestOPS_FriendRequestHasBeenSentByTheReceiver() {
        setERROR_FIELD_SUBJECT( "Error!"  );
        setERROR_FIELD_MESSAGE( "A friend request has already been sent to you by this user"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0028 ) ;
    }

    public void setSendFriendRequestOPS_BlockedUserVagueResponse( ) {
        setERROR_FIELD_SUBJECT( "Error!"  );
        setERROR_FIELD_MESSAGE( "You cannot add this user"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0029 ) ;
    }

    public void setSendFriendRequestOPS_YouHaveBlockedThisUser( ) {
        setERROR_FIELD_SUBJECT( "Error!"  );
        setERROR_FIELD_MESSAGE( "You have blocked this user"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0030 ) ;
    }

    public void setSendFollowRequestOPS_BlockedUserVagueResponse( ) {
        setERROR_FIELD_SUBJECT( "Error!"  );
        setERROR_FIELD_MESSAGE( "You cannot follow this user"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0031 ) ;
    }

    public void setSendFollowRequestOPS_YouHaveBlockedThisUser( ) {
        setERROR_FIELD_SUBJECT( "Error!"  );
        setERROR_FIELD_MESSAGE( "You have blocked this user"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0032 ) ;
    }

    public void setSendFollowRequestOPS_AlreadyFollowingTheUser() {
        setERROR_FIELD_SUBJECT( "Error!"  );
        setERROR_FIELD_MESSAGE( "You are already following this user" );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0033 ) ;
    }

    public void setSendFriendRequestOPS_AreAlreadyFriends() {
        setERROR_FIELD_SUBJECT( "Error!"  );
        setERROR_FIELD_MESSAGE( "You are already friends with this user"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0034 ) ;
    }
    public void setAcceptFriendRequest_GenericError() {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "Something has gone wrong"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0035 ) ;
    }

    public void setSendFriendRequestOPS_GenerateGenericError( ) {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "Something has gone wrong"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0036 ) ;
    }

    public void setSendFollowRequestOPS_PendingFollowRequest() {
        setERROR_FIELD_SUBJECT( "Error!"  );
        setERROR_FIELD_MESSAGE( "You already have a pending follow request" );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0037 ) ;
    }

    public void setRejectFriendRequestOPS_GenerateGenericError( ) {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "Something has gone wrong"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0038 ) ;
    }

    public void setRejectFollowRequestOPS_GenerateGenericError( ) {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "Something has gone wrong"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0039 ) ;
    }


    public void setBlockUserRequestOPS_GenerateGenericError( ) {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "Something has gone wrong"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0040 ) ;
    }

    public void setAcceptFollowRequest_GenerateGenericError() {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "Something has gone wrong"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0041 ) ;
    }

    public void setGetFriendList_NoFriends() {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "Seems you have do not friends on your list"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0042 ) ;
    }

    public void setGetFollowList_NoFollowers() {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "Seems you have do not followers on your list"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0043 ) ;
    }

    public void setGetFollowRequestList_NoNewFollowRequests() {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "Seems you have do not any new follow requests"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0044 ) ;
    }

    public void setGetFriendRequestList_NoNewFriendRequests() {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "Seems you have do not any new follow requests"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0045 ) ;
    }

    public void setGetBlockUserList_NoBlockedUsers() {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "Seems you have not blocked anyone"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0046 ) ;
    }

    public void setFetchUserData_GenericError() {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "Seems we are running into a little problem."  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0047 ) ;
    }

    public void setFetchUserData_NoData() {
        setERROR_FIELD_SUBJECT( "Oops!"  );
        setERROR_FIELD_MESSAGE( "We don't have any information stored in our database about your profile"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0048 ) ;
    }
}
