package com.jamii.requests.social;

import com.jamii.jamiidb.model.UserRelationshipTBL;
import org.springframework.stereotype.Component;

@Component
public class SendFollowRequestREQ extends AbstractSocialREQ {

    private String senderKey;
    private String recieverKey;
    private final static Integer relationshipType = UserRelationshipTBL.TYPE_FOLLOW;
    private final static Integer relationshipStatus = UserRelationshipTBL.STATUS_PENDING;

    public String getSenderKey() {
        return senderKey;
    }

    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    public String getRecieverKey() {
        return recieverKey;
    }

    public void setRecieverKey(String recieverKey) {
        this.recieverKey = recieverKey;
    }
}
