package com.jamii.jamiidb.model;

import jakarta.persistence.*;

@Entity
@Table( name = "user_roles" , schema =  "jamiidb")
public class UserRolesTBL {
    public UserRolesTBL() {
    }

    public static final String      TABLE_NAME      = "user_roles";

    public static final String      ID              = "ID";
    public static final String      USER_LOGIN_ID   = "USER_LOGIN_ID";
    public static final String      USER_GROUPS_ID  = "USER_GROUPS_ID";
    public static final String      AUX_DATA        = "AUX_DATA";

    @Id
    @Column( name = ID)
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = AUX_DATA, columnDefinition = "text" )
    private String auxdata;

    //Foreign Keys
    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL userloginid;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn( name = USER_GROUPS_ID, nullable = false )
    private UserGroupsTBL usergroupsid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuxdata() {
        return auxdata;
    }

    public void setAuxdata(String auxdata) {
        this.auxdata = auxdata;
    }

    public UserLoginTBL getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(UserLoginTBL userloginid) {
        this.userloginid = userloginid;
    }

    public UserGroupsTBL getUsergroupsid() {
        return usergroupsid;
    }

    public void setUsergroupsid(UserGroupsTBL usergroupsid) {
        this.usergroupsid = usergroupsid;
    }
}
