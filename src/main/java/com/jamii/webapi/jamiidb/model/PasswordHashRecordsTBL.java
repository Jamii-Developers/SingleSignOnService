package com.jamii.webapi.jamiidb.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table( name = "Password_Hash_Records" , schema =  "jamiidb")
public class PasswordHashRecordsTBL {

    public PasswordHashRecordsTBL( ){

    }

    public static final String      TABLE_NAME      = "PasswordHashRecords";

    public static final String      ID              = "ID";
    public static final String      USER_LOGIN_ID   = "USER_LOGIN_ID";
    public static final String      PASSWORD_SALT   = "PASSWORD_SALT";
    public static final String      DATE_ADDED      = "DATE_ADDED";

    @Id
    @Column( name = ID)
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = PASSWORD_SALT , nullable = false, length = 1000 )
    private String passwordsalt;

    @Column( name = DATE_ADDED , nullable = false, length = 100 )
    private LocalDateTime dateadded;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL FK_USER_LOGIN_DATA;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPasswordsalt() {
        return passwordsalt;
    }

    public void setPasswordsalt(String passwordsalt) {
        this.passwordsalt = passwordsalt;
    }

    public LocalDateTime getDateadded( ) {
        return dateadded;
    }

    public void setDateadded( LocalDateTime dateadded ) {
        this.dateadded = dateadded;
    }

    public UserLoginTBL getFK_USER_LOGIN_DATA() {
        return FK_USER_LOGIN_DATA;
    }

    public void setFK_USER_LOGIN_DATA(UserLoginTBL FK_USER_LOGIN_DATA) {
        this.FK_USER_LOGIN_DATA = FK_USER_LOGIN_DATA;
    }
}
