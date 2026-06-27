package com.jamii.jSocial.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;
import org.springframework.stereotype.Component;

@Component
public class RejectFriendRequestServicesREQ
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
