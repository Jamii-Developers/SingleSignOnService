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
    public static final String      AUX_DATA        = "AUX_DATA";


    @Id
    @Column( name = ID)
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    //Foreign Keys
    @OneToMany( mappedBy = "usergroupsid" )
    private List<UserRolesTBL> userRolesTBL;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL userloginid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<UserRolesTBL> getUserRolesTBL() {
        return userRolesTBL;
    }

    public void setUserRolesTBL(List<UserRolesTBL> userRolesTBL) {
        this.userRolesTBL = userRolesTBL;
    }

    public UserLoginTBL getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(UserLoginTBL userloginid) {
        this.userloginid = userloginid;
    }
}
