package com.jamii.webapi.jamiidb.model;

import jakarta.persistence.*;

@Entity
@Table( name = "user_roles" , schema =  "jamiidb")
public class UserRolesTBL {

    public static final String      TABLE_NAME      = "user_roles";

    public static final String      ID              = "ID";
    public static final String      USER_LOGIN_ID   = "USER_LOGIN_ID";
    public static final String      USER_GROUPS_ID  = "USER_GROUPS_ID";

    @Id
    @Column( name = ID)
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    //Foreign Keys
    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL FK_USER_LOGIN_DATA;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn( name = USER_GROUPS_ID, nullable = false )
    private UserGroupsTBL FK_USER_GROUPS_DATA;
}
