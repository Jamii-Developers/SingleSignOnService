package com.jamii.jamiidb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table( name = "user_login" , schema =  "jamiidb")
public class UserLoginTBL {

    public UserLoginTBL( ) { }

    public static final String              TABLE_NAME      = "user_login";

    public static final String              ID              = "ID";
    public static final String              USERNAME        = "USERNAME";
    public static final String              EMAIL_ADDRESS   = "EMAIL_ADDRESS";
    public static final String              PASSWORD_SALT   = "PASSWORD_SALT";
    public static final String              USER_KEY        = "USER_KEY";
    public static final String              ACTIVE          = "ACTIVE";
    public static final String              DATE_CREATED    = "DATE_CREATED";

    @Id
    @Column( name = ID )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = USERNAME , nullable = false, length = 50, unique = true)
    private String username;

    @Column( name = EMAIL_ADDRESS, nullable = false, length = 200, unique = true )
    private String emailaddress;

    @Column(   name = PASSWORD_SALT , nullable = false, length = 200 )
    private String passwordsalt;

    @Column( name = USER_KEY , nullable = false, length = 1000 )
    private String userkey;

    @Column( name = ACTIVE, nullable = false )
    private Integer active;

    @Column( name = DATE_CREATED, nullable = false )
    private LocalDateTime datecreated;

    @OneToMany( mappedBy = "userloginid" )
    private List<DeviceInformationTBL> deviceInformationTBL;

    @OneToMany( mappedBy = "FK_USER_LOGIN_DATA" )
    private List<UserDataTBL> userDataTBL;

    @OneToMany( mappedBy = "FK_USER_LOGIN_DATA" )
    private List<PasswordHashRecordsTBL> passwordHashRecordsTBL;

    @OneToMany( mappedBy = "FK_USER_LOGIN_DATA" )
    private List<UserGroupsTBL> userGroupsTBL;

    @OneToMany( mappedBy = "FK_USER_LOGIN_DATA" )
    private List<UserRolesTBL> userRolesTBL;

    @OneToMany( mappedBy = "FK_USER_LOGIN_DATA" )
    private List<FileTableOwnerTBL> fileTableOwnerTBL;

    /**
     * ACTIVE STATUS
     */

    public static final Integer ACTIVE_OFF              = 0;
    public static final Integer ACTIVE_ON               = 1;
    public static final Integer ACTIVE_TERMINATED       = 2;

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

    public void setEmailAddress(String emailaddress) {
        this.emailaddress = emailaddress;
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

    public String getUserKey() {
        return userkey;
    }

    public void setUserKey(String userkey) {
        this.userkey = userkey;
    }

    public LocalDateTime getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(LocalDateTime datecreated) {
        this.datecreated = datecreated;
    }
}
