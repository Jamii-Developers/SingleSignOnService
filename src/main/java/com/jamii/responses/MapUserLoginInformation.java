package com.jamii.responses;

import com.jamii.webapi.jamiidb.model.UserLoginInformationTBL;

import java.util.HashMap;

public class MapUserLoginInformation {

    public MapUserLoginInformation(UserLoginInformationTBL userInformation ) {
        setId( userInformation.getId( ) );
        setEmailaddress( userInformation.getEmailaddress( ) );
        setUsername( userInformation.getUsername( ) );
        setFirstname( userInformation.getFirstname( ) );
        setLastname( userInformation.getLastname( ) );
    }

    private Integer id;
    private String username;
    private String emailaddress;
    private String firstname;
    private String lastname;
    private String passwordsalt;
    private Integer active;

    public Integer getId() {
        return id;
    }

    public String getIdAsString( ){
        return String.valueOf( id );
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPasswordsalt() {
        return passwordsalt;
    }

    public void setPasswordsalt(String passwordsalt) {
        this.passwordsalt = passwordsalt;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public HashMap< String, String > getResponseMap( ){
        HashMap < String , String > response = new HashMap< >( );
        response.put( UserLoginInformationTBL.ID , getIdAsString( ) );
        response.put( UserLoginInformationTBL.EMAILADDRESS , getEmailaddress( ) );
        response.put( UserLoginInformationTBL.USERNAME , getUsername( ) );
        response.put( UserLoginInformationTBL.FIRSTNAME, getFirstname( ) );
        response.put( UserLoginInformationTBL.LASTNAME, getLastname( ) );
        return response;
    }
}
