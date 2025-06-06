package com.jamii.jamiidb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table( name = "user_block_list" , schema =  "jamiidb")
public class UserBlockListTBL {

    public static final String              TABLE_NAME      = "user_requests";
    public static final String              ID              = "ID";
    public static final String              USER_ID         = "USER_ID";
    public static final String              BLOCKED_ID      = "BLOCKED_ID";
    public static final String              STATUS          = "STATUS";
    public static final String              DATE_UPDATED    = "DATE_UPDATED";
    public static final String              AUX_DATA        = "AUX_DATA";

    @Id
    @Column( name = ID)
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = STATUS , nullable = false )
    private Integer status;

    @Column( name = DATE_UPDATED , nullable = false )
    private LocalDateTime dateupdated;

    @Column( name = AUX_DATA, columnDefinition = "text" )
    private String auxdata;

    //Foreign Keys
    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = USER_ID, nullable = false )
    private UserLoginTBL userid;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = BLOCKED_ID, nullable = false )
    private UserLoginTBL blockedid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getDateupdated() {
        return dateupdated;
    }

    public void setDateupdated(LocalDateTime dateupdated) {
        this.dateupdated = dateupdated;
    }

    public UserLoginTBL getUserid() {
        return userid;
    }

    public void setUserid(UserLoginTBL userid) {
        this.userid = userid;
    }

    public UserLoginTBL getBlockedid() {
        return blockedid;
    }

    public void setBlockedid(UserLoginTBL blockedid) {
        this.blockedid = blockedid;
    }

    public String getAuxdata() {
        return auxdata;
    }

    public void setAuxdata(String auxdata) {
        this.auxdata = auxdata;
    }
}
