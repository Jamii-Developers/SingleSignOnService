package com.jamii.webapi.jamiidb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table( name = "device_information" , schema =  "jamiidb")
public class DeviceInformationTBL {

    public DeviceInformationTBL( ) {
    }

    public static final Integer ACTIVE_STATUS_DISABLED = 0;
    public static final Integer ACTIVE_STATUS_ENABLED = 1;

    @Id
    @Column( name = "id")
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;
    public static final String ID = "ID";

    @Column( name = "user_login_id")
    private Integer userloginid;
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";

    @Column( name = "device_name")
    private String devicename;
    public static final String DEVICE_NAME = "DEVICE_NAME";

    @Column( name = "last_connected")
    private LocalDateTime lastconnected;
    public static final String LAST_CONNECTED = "LAST_CONNECTED";

    @Column( name = "active")
    private Integer active;
    public static final String ACTIVE = "ACTIVE";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(Integer userloginid) {
        this.userloginid = userloginid;
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
}
