package com.jamii.webapi.jamiidb.model;

import jakarta.persistence.*;

@Entity
@Table( name = "UserLoginInformation")
public class UserLoginInformationTBL {

    public UserLoginInformationTBL( ) { }

    @Id
    @Column( name = "id")
    @GeneratedValue( strategy= GenerationType.AUTO )
    private Integer id;
    @Column( name = "username")
    private String username;
    @Column( name = "emailaddress")

    private String emailaddress;
    @Column( name = "first_name")

    private String firstname;
    @Column( name = "last_name")

    private String lastname;
    @Column( name = "password_salt")

    private String passwordsalt;
    @Column( name = "active")

    private Integer active;

    /**
     * ACTIVE STATUS
     */

    public static final Integer ACTIVE_OFF              = 0;
    public static final Integer ACTIVE_ON               = 1;
    public static final Integer ACTIVE_TERMINATED       = 2;

    public Integer getId() {
        return id;
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

    public void setEmailAddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname( ) {
        return lastname;
    }

    public void setLastname(String last_name) {
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
}
