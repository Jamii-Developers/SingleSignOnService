package com.jamii.requests.activeDirectory;

import com.jamii.jamiidb.model.UserLoginTBL;

public class UserLoginREQ {

    private String loginCredential;
    private String loginPassword;
    private String loginDeviceName;
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

    public String getLoginDeviceName() {
        return loginDeviceName;
    }

    public void setLoginDeviceName(String loginDeviceName) {
        this.loginDeviceName = loginDeviceName;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }
}
