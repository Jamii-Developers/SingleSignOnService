package com.jamii.requests.userServices.socialREQ;

import com.jamii.requests.userServices.AbstractUserServicesREQ;
import org.springframework.stereotype.Component;

@Component
public class AcceptFriendRequestServicesREQ extends AbstractUserServicesREQ {

    private String targetUserKey;

    public String getTargetUserKey() {
        return targetUserKey;
    }

    public void setTargetUserKey(String targetUserKey) {
        this.targetUserKey = targetUserKey;
    }
}
