package com.jamii.jamiidb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table( name = "device_information" , schema =  "jamiidb")
public class DeviceInformationTBL {

    public DeviceInformationTBL( ) { }

    public static final String      TABLE_NAME      = "device_information";

    public static final String      ID              = "ID";
    public static final String      USER_LOGIN_ID   = "USER_LOGIN_ID";
    public static final String      DEVICE_NAME     = "DEVICE_NAME";
    public static final String      DEVICE_KEY      = "DEVICE_KEY";
    public static final String      LAST_CONNECTED  = "LAST_CONNECTED";
    public static final String      ACTIVE          = "ACTIVE";


    @Id
    @Column( name = ID )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = DEVICE_NAME ,length = 200)
    private String devicename;

    @Column( name = DEVICE_KEY , length = 50)
    private String devicekey;

    @Column( name = LAST_CONNECTED )
    private LocalDateTime lastconnected;

    @Column( name = ACTIVE )
    private Integer active;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL userloginid;

    /**
     * Active Statuses
     */

    public static final Integer     ACTIVE_STATUS_DISABLED      = 0;
    public static final Integer     ACTIVE_STATUS_ENABLED       = 1;
    public static final Integer     ACTIVE_STATUS_BLOCKED       = 2;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public LocalDateTime getLastconnected() {
        return lastconnected;
    }

    public void setLastconnected(LocalDateTime lastconnected) {
        this.lastconnected = lastconnected;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getDevicekey() {
        return devicekey;
    }

    public void setDevicekey(String devicekey) {
        this.devicekey = devicekey;
    }

    public UserLoginTBL getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(UserLoginTBL userloginid) {
        this.userloginid = userloginid;
    }
}
