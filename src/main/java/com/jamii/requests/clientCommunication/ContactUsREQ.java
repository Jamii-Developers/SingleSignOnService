package com.jamii.requests.clientCommunication;

public class ContactUsREQ {

    private String username;
    private String emailaddress;
    private String client_thoughts;

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

    public String getClient_thoughts() {
        return client_thoughts;
    }

    public void setClient_thoughts(String clientthoughts) {
        this.client_thoughts = clientthoughts;
    }
}
