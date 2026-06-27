package com.jamii.jUser.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;

public class DeactivateUserREQ
        extends AbstractUserServicesREQ
{

    private String username;
    private String emailaddress;
    private String password;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmailaddress()
    {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress)
    {
        this.emailaddress = emailaddress;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
