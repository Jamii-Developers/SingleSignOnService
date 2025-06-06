package com.jamii.responses.userResponses.profileResponses;

import com.jamii.Utils.JamiiConstants;
import com.jamii.responses.AbstractResponses;

public class FetchUserProfileRESP extends AbstractResponses {

    public FetchUserProfileRESP() {
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "Latest data has been retrieved!" ;
        this.MSG_TYPE = JamiiConstants.RESPONSE_FETCH_USER_DATA;
    }

    private String firstname;
    private String lastname;
    private String middlename;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String province;
    private String country;
    private String zipcode;
    private Integer privacy;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }
}
