package com.jamii.jamiidb.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table( name = "user_groups" , schema =  "jamiidb")
public class UserGroupsTBL {

    public UserGroupsTBL( ) { }

    public static final String      TABLE_NAME      = "user_groups";

    public static final String      ID              = "ID";
    public static final String      USER_LOGIN_ID   = "USER_LOGIN_ID";


    @Id
    @Column( name = ID)
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    //Foreign Keys
    @OneToMany( mappedBy = "FK_USER_GROUPS_DATA" )
    private List<UserRolesTBL> userRolesTBL;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL FK_USER_LOGIN_DATA;


}
