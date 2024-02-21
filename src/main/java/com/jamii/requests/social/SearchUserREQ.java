package com.jamii.requests.social;

public class SearchUserREQ extends AbstractSocialREQ{

    private String searchstring;

    public String getSearchstring() {
        return searchstring;
    }

    public void setSearchstring(String searchstring) {
        this.searchstring = searchstring;
    }
}
