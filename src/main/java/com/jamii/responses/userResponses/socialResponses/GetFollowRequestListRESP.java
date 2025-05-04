package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

import java.util.ArrayList;
import java.util.List;

public class GetFollowRequestListRESP extends AbstractResponses {

    public GetFollowRequestListRESP( ) {
        this.MSG_TYPE = JamiiConstants.RESPONSE_GET_FOLLOW_REQUEST_LIST_REQUEST;
    }

    List<GetFollowRequestListRESP.Results> results = new ArrayList<>( );

    public List<GetFollowRequestListRESP.Results> getResults() {
        return results;
    }

    public void setResults(List<GetFollowRequestListRESP.Results> results) {
        this.results = results;
    }

    public static class Results{

        private String userKey;
        private String username;
        private String firstname;
        private String lastname;

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
    }
}
