package com.jamii.requests.publicServices;

public class CreateNewUserREQ extends AbstractPublicServicesREQ{

    public CreateNewUserREQ() {
    }

    private String emailaddress;
    private String username;
    private String password ;

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
