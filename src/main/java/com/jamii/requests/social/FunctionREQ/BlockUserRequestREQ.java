package com.jamii.requests.social.FunctionREQ;

import com.jamii.requests.social.AbstractSocialREQ;
import org.springframework.stereotype.Component;

@Component
public class BlockUserRequestREQ extends AbstractSocialREQ {

    private String receiveruserkey;

    public String getReceiveruserkey() {
        return receiveruserkey;
    }

    public void setReceiveruserkey(String receiveruserkey) {
        this.receiveruserkey = receiveruserkey;
    }
}
