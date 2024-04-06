package com.jamii.services.social;

import com.jamii.requests.social.GetFollowerRequestListREQ;
import org.springframework.stereotype.Service;

@Service
public class GetFollowRequestListOPS extends socialAbstract{

    private GetFollowerRequestListREQ getFollowerRequestListREQ;

    public GetFollowerRequestListREQ getGetFollowerRequestListREQ() {
        return getFollowerRequestListREQ;
    }

    public void setGetFollowerRequestListREQ(GetFollowerRequestListREQ getFollowerRequestListREQ) {
        this.getFollowerRequestListREQ = getFollowerRequestListREQ;
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getGetFollowerRequestListREQ().getDevicekey();
        UserKey = getGetFollowerRequestListREQ().getUserkey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

    }
}
