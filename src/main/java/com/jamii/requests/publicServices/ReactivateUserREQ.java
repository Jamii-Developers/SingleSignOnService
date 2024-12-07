package com.jamii.requests.publicServices;

import com.jamii.jamiidb.model.UserLoginTBL;

public class ReactivateUserREQ extends AbstractPublicServicesREQ {

    private String username;
    private String emailaddress;
    private String password;
    private final Integer active = UserLoginTBL.ACTIVE_OFF;

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
