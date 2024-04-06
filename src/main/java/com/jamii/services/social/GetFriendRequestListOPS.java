package com.jamii.services.social;

import com.jamii.requests.social.GetFriendRequestListREQ;
import org.springframework.stereotype.Service;

@Service
public class GetFriendRequestListOPS extends socialAbstract{

    private GetFriendRequestListREQ getFriendRequestListREQ;

    public GetFriendRequestListREQ getGetFriendRequestListREQ() {
        return getFriendRequestListREQ;
    }

    public void setGetFriendRequestListREQ(GetFriendRequestListREQ getFriendRequestListREQ) {
        this.getFriendRequestListREQ = getFriendRequestListREQ;
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getGetFriendRequestListREQ().getDevicekey();
        UserKey = getGetFriendRequestListREQ().getUserkey( );
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

    }
}
