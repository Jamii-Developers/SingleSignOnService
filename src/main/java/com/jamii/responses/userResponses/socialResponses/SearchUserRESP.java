package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

import java.util.ArrayList;
import java.util.List;

public class SearchUserRESP extends AbstractResponses {

    public SearchUserRESP( ) {
        this.MSG_TYPE = JamiiConstants.RESPONSE_SEARCH_RESULTS;
    }

    List< Results > results = new ArrayList<>( );

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public static class Results{

        private String userKey;
        private String username;
        private String firstname;
        private String lastname;
        private boolean isFriend;
        private boolean isFollowing;
        private boolean hasPendingFriendRequest;
        private boolean hasPendingFollowingRequest;

        public String getUserKey() {
            return userKey;
        }

        public void setUserKey(String userKey) {
            this.userKey = userKey;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
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


}
