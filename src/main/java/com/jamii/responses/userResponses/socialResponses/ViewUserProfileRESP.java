package com.jamii.responses.userResponses.socialResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.databaseconfig.model.UserDataTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.responses.AbstractResponses;

public class ViewUserProfileRESP extends AbstractResponses {

    public ViewUserProfileRESP(UserLoginTBL user, UserDataTBL userData) {
        this.MSG_TYPE = JamiiConstants.RESPONSE_VIEW_USER_PROFILE;
        setUsername( user.getUsername( ) );
        setEmailaddress( user.getEmailaddress( ) );

        if( userData != null ) {
            setFirstname( userData.getFirstname( ) );
            setMiddlename( userData.getMiddlename( ) );
            setLastname( userData.getLastname( ) );
            setAddress1( userData.getAddress1( ) );
            setAddress2( userData.getAddress2( ) );
            setCity( userData.getCity( ) );
            setState( userData.getState( ) );
            setCountry( userData.getCountry( ) );
            setZipcode(userData.getZipcode( ) );
        }
    }

    private String username;
    private String emailaddress;
    private String firstname;
    private String middlename;
    private String lastname;
    private String address1;
    private String address2;
    private String city;
    private String province;
    private String state;
    private String country;
    private String zipcode;

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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
