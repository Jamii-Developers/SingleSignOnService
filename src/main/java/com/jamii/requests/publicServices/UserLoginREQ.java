package com.jamii.requests.publicServices;

public class UserLoginREQ extends AbstractPublicServicesREQ{

    private String loginCredential;
    private String loginPassword;
    private String loginDeviceName;
    private String location;
    private Boolean rememberLogin;

    public UserLoginREQ() {
    }

    public UserLoginREQ(String loginCredential, String loginPassword, String loginDeviceName, String location, Boolean rememberLogin) {
        this.loginCredential = loginCredential;
        this.loginPassword = loginPassword;
        this.loginDeviceName = loginDeviceName;
        this.location = location;
        this.rememberLogin = rememberLogin;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getRememberLogin() {
        return rememberLogin;
    }

    public void setRememberLogin(Boolean rememberLogin) {
        this.rememberLogin = rememberLogin;
    }
}
