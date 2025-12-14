package com.jamii.requests.userServices.socialREQ;

import com.jamii.requests.userServices.AbstractUserServicesREQ;

public class SearchUserServicesREQ
        extends AbstractUserServicesREQ
{

    private String searchstring;

    public String getSearchstring()
    {
        return searchstring;
    }

    public void setSearchstring(String searchstring)
    {
        this.searchstring = searchstring;
    }
}
