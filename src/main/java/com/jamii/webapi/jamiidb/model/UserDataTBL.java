package com.jamii.webapi.jamiidb.model;

import jakarta.persistence.*;

@Entity
@Table( name = "user_data" , schema =  "jamiidb")
public class UserDataTBL {
    public UserDataTBL( ) { }

    public static final String      TABLE_NAME      =       "user_data";

    public static final String      ID              =       "ID";
    public static final String      USER_LOGIN_ID   =       "USER_LOGIN_ID";
    public static final String      FIRST_NAME      =       "FIRST_NAME";
    public static final String      LAST_NAME       =       "LAST_NAME";

    @Id
    @Column( name = ID)
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = FIRST_NAME , nullable = false, length = 200 )
    private String firstname;

    @Column( name = LAST_NAME, nullable = false, length = 200 )
    private String lastname;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL FK_USER_LOGIN_DATA;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
