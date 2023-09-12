package com.jamii.requests.activeDirectory;

import com.jamii.jamiidb.model.UserLoginTBL;

public class DeactivateUserREQ {

    private String userkey;
    private String username;
    private String emailaddress;
    private String password;
    private final Integer active = UserLoginTBL.ACTIVE_ON;

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getActive() {
        return active;
    }
}
