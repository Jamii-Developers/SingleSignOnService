package com.jamii.requests.clientCommunication;

public class ContactUsREQ {

    private String username;
    private String emailAddress;
    private String clientThoughts;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getClientThoughts() {
        return clientThoughts;
    }

    public void setClientThoughts(String clientThoughts) {
        this.clientThoughts = clientThoughts;
    }
}
