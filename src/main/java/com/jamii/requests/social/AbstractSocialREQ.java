package com.jamii.requests.social;

public class AbstractSocialREQ {

    protected String userkey;
    protected String devicekey;

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getDevicekey() {
        return devicekey;
    }

    public void setDevicekey(String devicekey) {
        this.devicekey = devicekey;
    }

}
