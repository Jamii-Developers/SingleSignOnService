package com.jamii.jSocial.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;
import org.springframework.stereotype.Component;

@Component
public class SendFollowRequestServicesREQ
        extends AbstractUserServicesREQ
{

    private String followKey;

    public String getFollowKey()
    {
        return followKey;
    }

    public void setFollowKey(String followKey)
    {
        this.followKey = followKey;
    }
}
