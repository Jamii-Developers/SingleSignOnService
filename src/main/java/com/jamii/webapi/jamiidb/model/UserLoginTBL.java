package com.jamii.webapi.jamiidb.model;

import jakarta.persistence.*;

@Entity
@Table( name = "User_Login" , schema =  "jamiidb")
public class UserLoginTBL {

    public UserLoginTBL( ) { }

    @Id
    @Column( name = "id")
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = false)
    private Integer id;
    public static final String ID = "ID";

    @Column( name = "username" , nullable = false, length = 50, unique = true)
    private String username;
    public static final String USERNAME = "USERNAME";

    @Column( name = "emailaddress", nullable = false, length = 200, unique = true )

    private String emailaddress;
    public static final String EMAILADDRESS = "EMAILADDRESS";

    @Column( name = "firstname" , nullable = false, length = 200 )

    private String firstname;
    public static final String FIRSTNAME = "FIRSTNAME";

    @Column( name = "lastname", nullable = false, length = 200 )

    private String lastname;
    public static final String LASTNAME = "LASTNAME";

    @Column( name = "passwordsalt" , nullable = false, length = 200 )

    private String passwordsalt;
    public static final String PASSWORDSALT = "PASSWORDSALT";


    @Column( name = "active", nullable = false )
    private Integer active;
    public static final String ACTIVE = "ACTIVE";


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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname( ) {
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
}
