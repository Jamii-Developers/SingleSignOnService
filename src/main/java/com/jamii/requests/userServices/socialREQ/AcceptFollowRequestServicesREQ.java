package com.jamii.requests.userServices.socialREQ;

import com.jamii.requests.userServices.AbstractUserServicesREQ;
import org.springframework.stereotype.Component;

@Component
public class AcceptFollowRequestServicesREQ extends AbstractUserServicesREQ {

    private String receiveruserkey;

    public String getReceiveruserkey() {
        return receiveruserkey;
    }

    public void setReceiveruserkey(String receiveruserkey) {
        this.receiveruserkey = receiveruserkey;
    }
}
