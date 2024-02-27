package com.jamii.requests.social;

import org.springframework.stereotype.Component;

@Component
public class RemoveFollowRequestREQ extends AbstractSocialREQ{

    private String receiveruserkey;

    public String getReceiveruserkey() {
        return receiveruserkey;
    }

    public void setReceiveruserkey(String receiveruserkey) {
        this.receiveruserkey = receiveruserkey;
    }

}
