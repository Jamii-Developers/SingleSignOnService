package com.jamii.social.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;

public class RemoveFollowerServicesREQ
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
