package com.jamii.jamiidb.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
    private UserLoginTBL userloginid;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn( name = USER_GROUPS_ID, nullable = false )
    private UserGroupsTBL usergroupsid;
}
