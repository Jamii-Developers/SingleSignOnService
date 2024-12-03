package com.jamii.jamiidb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table( name = "user_cookies" , schema =  "jamiidb")
public class UserCookiesTBL {
    public UserCookiesTBL() { }

    public static final String      TABLE_NAME              = "user_cookies";

    public static final String      ID                      = "ID";
    public static final String      USER_LOGIN_ID           = "USER_LOGIN_ID";
    public static final String      DEVICE_INFORMATION_ID   = "DEVICE_INFORMATION_ID";
    public static final String      DATE_CREATED            = "DATE_CREATED";
    public static final String      SESSION_KEY             = "SESSION_KEY";
    public static final String      EXPIRE_DATE             = "EXPIRE_DATE";
    public static final String      ACTIVE                  = "ACTIVE";


    @Id
    @Column( name = ID)
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = DATE_CREATED)
    private LocalDateTime datecreated;

    @Column( name = SESSION_KEY)
    private String sessionkey;

    @Column( name = EXPIRE_DATE)
    private LocalDateTime expiredate;

    @Column( name = ACTIVE)
    private boolean active;

    //Foreign Keys
    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL userloginid;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = DEVICE_INFORMATION_ID, nullable = false )
    private DeviceInformationTBL deviceinformationid;

    //ACTIVE STATUS
    public static boolean ACTIVE_STATUS_ENABLED = true;
    public static boolean ACTIVE_STATUS_DISABLED = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(LocalDateTime datecreated) {
        this.datecreated = datecreated;
    }

    public UserLoginTBL getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(UserLoginTBL userloginid) {
        this.userloginid = userloginid;
    }

    public DeviceInformationTBL getDeviceinformationid() {
        return deviceinformationid;
    }

    public void setDeviceinformationid(DeviceInformationTBL deviceinformationid) {
        this.deviceinformationid = deviceinformationid;
    }

    public String getSessionkey() {
        return sessionkey;
    }

    public void setSessionkey(String sessionkey) {
        this.sessionkey = sessionkey;
    }

    public LocalDateTime getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(LocalDateTime expiredate) {
        this.expiredate = expiredate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
