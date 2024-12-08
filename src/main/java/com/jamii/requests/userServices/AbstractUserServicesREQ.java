package com.jamii.requests.userServices;

public class AbstractUserServicesREQ {

    protected String userKey;
    protected String deviceKey;
    protected String sessionKey;

    public String getUserKey() {return userKey;}

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getDeviceKey() {return deviceKey;}

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
