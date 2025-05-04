package com.jamii.operations.userServices.social.Utils;

public class SearchResultsHelper {

     public static class SearchResults{
        private String USER_KEY;
        private String USERNAME;
        private String FIRSTNAME;
        private String LASTNAME;
        private boolean isFriend;
        private boolean isFollowing;
        private boolean hasPendingFriendRequest;
        private boolean hasPendingFollowingRequest;

        public String getUSER_KEY() {
            return USER_KEY;
        }

        public void setUSER_KEY(String USER_KEY) {
            this.USER_KEY = USER_KEY;
        }

        public String getUSERNAME() {
            return USERNAME;
        }

        public void setUSERNAME(String USERNAME) {
            this.USERNAME = USERNAME;
        }

        public String getFIRSTNAME() {
            return FIRSTNAME;
        }

        public void setFIRSTNAME(String FIRSTNAME) {
            this.FIRSTNAME = FIRSTNAME;
        }

        public String getLASTNAME() {
            return LASTNAME;
        }

        public void setLASTNAME(String LASTNAME) {
            this.LASTNAME = LASTNAME;
        }

         public boolean isFriend() {
             return isFriend;
         }

         public void setFriend(boolean friend) {
             isFriend = friend;
         }

         public boolean isFollowing() {
             return isFollowing;
         }

         public void setFollowing(boolean following) {
             isFollowing = following;
         }

         public boolean isHasPendingFriendRequest() {
             return hasPendingFriendRequest;
         }

         public void setHasPendingFriendRequest(boolean hasPendingFriendRequest) {
             this.hasPendingFriendRequest = hasPendingFriendRequest;
         }

         public boolean isHasPendingFollowingRequest() {
             return hasPendingFollowingRequest;
         }

         public void setHasPendingFollowingRequest(boolean hasPendingFollowingRequest) {
             this.hasPendingFollowingRequest = hasPendingFollowingRequest;
         }
     }

    public static class RelationShipResults{
        private String USER_KEY;
        private String USERNAME;
        private String FIRSTNAME;
        private String LASTNAME;

        public String getUSER_KEY() {
            return USER_KEY;
        }

        public void setUSER_KEY(String USER_KEY) {
            this.USER_KEY = USER_KEY;
        }

        public String getUSERNAME() {
            return USERNAME;
        }

        public void setUSERNAME(String USERNAME) {
            this.USERNAME = USERNAME;
        }

        public String getFIRSTNAME() {
            return FIRSTNAME;
        }

        public void setFIRSTNAME(String FIRSTNAME) {
            this.FIRSTNAME = FIRSTNAME;
        }

        public String getLASTNAME() {
            return LASTNAME;
        }

        public void setLASTNAME(String LASTNAME) {
            this.LASTNAME = LASTNAME;
        }
    }
}
