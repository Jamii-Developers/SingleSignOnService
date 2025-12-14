package com.jamii.requests.userServices.socialREQ;

import com.jamii.requests.userServices.AbstractUserServicesREQ;
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
