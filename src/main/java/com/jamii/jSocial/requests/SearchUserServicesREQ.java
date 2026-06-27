package com.jamii.jSocial.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;

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
