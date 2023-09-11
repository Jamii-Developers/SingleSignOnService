package com.jamii.requests.activeDirectory;

import com.jamii.webapi.jamiidb.model.UserLoginTBL;

public class UserLoginREQ {

    private String loginCredential;
    private String loginPassword;
    private String loginDeviceId;
    private final Integer activeStatus = UserLoginTBL.ACTIVE_ON;

    public String getLoginCredential() {
        return loginCredential;
    }

    public void setLoginCredential(String loginCredential) {
        this.loginCredential = loginCredential;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getLoginDeviceId() {
        return loginDeviceId;
    }

    public void setLoginDeviceId(String loginDeviceId) {
        this.loginDeviceId = loginDeviceId;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }
}
