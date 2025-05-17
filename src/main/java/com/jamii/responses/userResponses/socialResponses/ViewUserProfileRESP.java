package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class ViewUserProfileRESP extends AbstractResponses {

    public ViewUserProfileRESP(UserLoginTBL user, UserDataTBL userData) {
        this.MSG_TYPE = JamiiConstants.RESPONSE_VIEW_USER_PROFILE;
        setUsername( user.getUsername( ) );
        setEmailaddress( user.getEmailaddress( ) );

        if( userData != null ) {
            setFirstname(userData.getFirstname());
            setMiddlename(userData.getMiddlename());
            setLastname(userData.getLastname());
        }
    }

    private String username;
    private String emailaddress;
    private String firstname;
    private String middlename;
    private String lastname;

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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
