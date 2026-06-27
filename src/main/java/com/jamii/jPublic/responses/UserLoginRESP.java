package com.jamii.jPublic.responses;

import com.jamii.utils.JamiiConstants;
import com.jamii.abstractClasses.AbstractResponses;

public class UserLoginRESP
        extends AbstractResponses
{

    private String userKey;
    private String deviceKey;
    private String sessionKey;
    private String username;
    private String emailAddress;
    private String dateCreated;
    private String expiryDate;

    public UserLoginRESP()
    {
        this.UI_SUBJECT = "Login Successful!";
        this.UI_MESSAGE = "Your login was successful!";
        this.MSG_TYPE = JamiiConstants.RESPONSE_TYPE_USERLOGIN;
    }

    public String getUserKey()
    {
        return userKey;
    }

    public void setUserKey(String userKey)
    {
        this.userKey = userKey;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getDeviceKey()
    {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey)
    {
        this.deviceKey = deviceKey;
    }

    public String getSessionKey()
    {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey)
    {
        this.sessionKey = sessionKey;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public String getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate)
    {
        this.expiryDate = expiryDate;
    }
}
