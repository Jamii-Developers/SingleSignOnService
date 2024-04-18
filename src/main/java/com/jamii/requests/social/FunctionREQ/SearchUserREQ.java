package com.jamii.requests.social.FunctionREQ;

import com.jamii.requests.social.AbstractSocialREQ;

public class SearchUserREQ extends AbstractSocialREQ {

    private String searchstring;

    public String getSearchstring() {
        return searchstring;
    }

    public void setSearchstring(String searchstring) {
        this.searchstring = searchstring;
    }
}
