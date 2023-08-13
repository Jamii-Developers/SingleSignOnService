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
    @Column( name = "email_address")

    private String email_address;
    @Column( name = "first_name")

    private String first_name;
    @Column( name = "last_name")

    private String last_name;
    @Column( name = "password_salt")

    private String password_salt;
    @Column( name = "active")

    private Integer active;

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

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword_salt() {
        return password_salt;
    }

    public void setPassword_salt(String password_salt) {
        this.password_salt = password_salt;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
