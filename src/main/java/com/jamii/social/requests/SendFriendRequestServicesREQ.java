package com.jamii.social.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;
import org.springframework.stereotype.Component;

@Component
public class SendFriendRequestServicesREQ
        extends AbstractUserServicesREQ
{

    private String friendKey;

    public String getFriendKey()
    {
        return friendKey;
    }

    public void setFriendKey(String friendKey)
    {
        this.friendKey = friendKey;
    }
}
