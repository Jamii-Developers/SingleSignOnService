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

import java.time.LocalDateTime;

@Entity
@Table(name = "user_cookies", schema = "jamiidb")
public class UserCookiesTBL
{
    public static final String TABLE_NAME = "user_cookies";
    public static final String ID = "ID";
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    public static final String DEVICE_INFORMATION_ID = "DEVICE_INFORMATION_ID";
    public static final String DATE_CREATED = "DATE_CREATED";
    public static final String SESSION_KEY = "SESSION_KEY";
    public static final String EXPIRE_DATE = "EXPIRE_DATE";
    public static final String ACTIVE = "ACTIVE";
    public static final String AUX_DATA = "AUX_DATA";

    @Id @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(name = DATE_CREATED) private LocalDateTime datecreated;
    @Column(name = SESSION_KEY) private String sessionkey;
    @Column(name = EXPIRE_DATE) private LocalDateTime expiredate;
    @Column(name = ACTIVE) private boolean active;
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    //Foreign Keys
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = USER_LOGIN_ID, nullable = false) private UserLoginTBL userloginid;
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = DEVICE_INFORMATION_ID, nullable = false) private DeviceInformationTBL deviceinformationid;

    public UserCookiesTBL() {}

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public LocalDateTime getDatecreated()
    {
        return datecreated;
    }

    public void setDatecreated(LocalDateTime datecreated)
    {
        this.datecreated = datecreated;
    }

    public UserLoginTBL getUserloginid()
    {
        return userloginid;
    }

    public void setUserloginid(UserLoginTBL userloginid)
    {
        this.userloginid = userloginid;
    }

    public DeviceInformationTBL getDeviceinformationid()
    {
        return deviceinformationid;
    }

    public void setDeviceinformationid(DeviceInformationTBL deviceinformationid)
    {
        this.deviceinformationid = deviceinformationid;
    }

    public String getSessionkey()
    {
        return sessionkey;
    }

    public void setSessionkey(String sessionkey)
    {
        this.sessionkey = sessionkey;
    }

    public LocalDateTime getExpiredate()
    {
        return expiredate;
    }

    public void setExpiredate(LocalDateTime expiredate)
    {
        this.expiredate = expiredate;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getAuxdata()
    {
        return auxdata;
    }

    public void setAuxdata(String auxdata)
    {
        this.auxdata = auxdata;
    }
}
