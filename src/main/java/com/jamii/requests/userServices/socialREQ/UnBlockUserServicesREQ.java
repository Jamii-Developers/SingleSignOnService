package com.jamii.requests.userServices.socialREQ;

import com.jamii.requests.userServices.AbstractUserServicesREQ;

public class UnBlockUserServicesREQ
        extends AbstractUserServicesREQ
{

    private String targetUserKey;

    public String getTargetUserKey()
    {
        return targetUserKey;
    }

    public void setTargetUserKey(String targetUserKey)
    {
        this.targetUserKey = targetUserKey;
    }
}
