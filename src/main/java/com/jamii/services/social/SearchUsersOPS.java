package com.jamii.services.social;

import com.jamii.requests.social.SearchUserREQ;

public class SearchUsersOPS extends socialAbstract{

    private SearchUserREQ searchUserREQ;

    public SearchUserREQ getSearchUserREQ() {
        return searchUserREQ;
    }

    public void setSearchUserREQ(SearchUserREQ searchUserREQ) {
        this.searchUserREQ = searchUserREQ;
    }

    /**
     * @throws Exception
     */
    @Override
    public void processRequest() throws Exception {

    }
}
