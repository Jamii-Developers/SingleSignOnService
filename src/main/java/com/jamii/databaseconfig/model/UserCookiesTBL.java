package com.jamii.databaseconfig.model;

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

/**
 * JPA Entity representing user cookie/session records in the database.
 * 
 * <p>This class maps to the {@code user_cookies} table and stores session information
 * for users, including session keys, expiration dates, and device associations for "remember me" functionality.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code userloginid} - User this session belongs to</li>
 *     <li>{@code deviceinformationid} - Device associated with this session</li>
 *     <li>{@code sessionkey} - Session key for authentication</li>
 *     <li>{@code expiredate} - When the session expires</li>
 *     <li>{@code active} - Whether the session is active</li>
 * </ul>
 */
@Entity
@Table(name = "user_cookies")
public class UserCookiesTBL
{
    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "user_cookies";
    
    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for user login ID.
     */
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    
    /**
     * Column name constant for device information ID.
     */
    public static final String DEVICE_INFORMATION_ID = "DEVICE_INFORMATION_ID";
    
    /**
     * Column name constant for date created.
     */
    public static final String DATE_CREATED = "DATE_CREATED";
    
    /**
     * Column name constant for session key.
     */
    public static final String SESSION_KEY = "SESSION_KEY";
    
    /**
     * Column name constant for expire date.
     */
    public static final String EXPIRE_DATE = "EXPIRE_DATE";
    
    /**
     * Column name constant for active status.
     */
    public static final String ACTIVE = "ACTIVE";
    
    /**
     * Column name constant for auxiliary data.
     */
    public static final String AUX_DATA = "AUX_DATA";

    /**
     * Primary key identifier.
     */
    @Id @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    
    /**
     * Timestamp when the session was created.
     */
    @Column(name = DATE_CREATED) private LocalDateTime datecreated;
    
    /**
     * Session key for authentication.
     */
    @Column(name = SESSION_KEY) private String sessionkey;
    
    /**
     * Timestamp when the session expires.
     */
    @Column(name = EXPIRE_DATE) private LocalDateTime expiredate;
    
    /**
     * Whether the session is active.
     */
    @Column(name = ACTIVE) private boolean active;
    
    /**
     * Auxiliary data stored as text.
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    /**
     * Foreign key reference to the user this session belongs to.
     */
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = USER_LOGIN_ID, nullable = false) private UserLoginTBL userloginid;
    
    /**
     * Foreign key reference to the device associated with this session.
     */
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = DEVICE_INFORMATION_ID, nullable = false) private DeviceInformationTBL deviceinformationid;

    /**
     * Default constructor.
     */
    public UserCookiesTBL() {}

    /**
     * Gets the primary key ID.
     * @return the ID
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * Sets the primary key ID.
     * @param id the ID to set
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * Gets the date created timestamp.
     * @return the date created
     */
    public LocalDateTime getDatecreated()
    {
        return datecreated;
    }

    /**
     * Sets the date created timestamp.
     * @param datecreated the date created to set
     */
    public void setDatecreated(LocalDateTime datecreated)
    {
        this.datecreated = datecreated;
    }

    /**
     * Gets the user this session belongs to.
     * @return the user login entity
     */
    public UserLoginTBL getUserloginid()
    {
        return userloginid;
    }

    /**
     * Sets the user this session belongs to.
     * @param userloginid the user login entity to set
     */
    public void setUserloginid(UserLoginTBL userloginid)
    {
        this.userloginid = userloginid;
    }

    /**
     * Gets the device associated with this session.
     * @return the device information entity
     */
    public DeviceInformationTBL getDeviceinformationid()
    {
        return deviceinformationid;
    }

    /**
     * Sets the device associated with this session.
     * @param deviceinformationid the device information entity to set
     */
    public void setDeviceinformationid(DeviceInformationTBL deviceinformationid)
    {
        this.deviceinformationid = deviceinformationid;
    }

    /**
     * Gets the session key.
     * @return the session key
     */
    public String getSessionkey()
    {
        return sessionkey;
    }

    /**
     * Sets the session key.
     * @param sessionkey the session key to set
     */
    public void setSessionkey(String sessionkey)
    {
        this.sessionkey = sessionkey;
    }

    /**
     * Gets the expire date.
     * @return the expire date
     */
    public LocalDateTime getExpiredate()
    {
        return expiredate;
    }

    /**
     * Sets the expire date.
     * @param expiredate the expire date to set
     */
    public void setExpiredate(LocalDateTime expiredate)
    {
        this.expiredate = expiredate;
    }

    /**
     * Gets the active status.
     * @return {@code true} if active, {@code false} otherwise
     */
    public boolean isActive()
    {
        return active;
    }

    /**
     * Sets the active status.
     * @param active the active status to set
     */
    public void setActive(boolean active)
    {
        this.active = active;
    }

    /**
     * Gets the auxiliary data.
     * @return the auxiliary data
     */
    public String getAuxdata()
    {
        return auxdata;
    }

    /**
     * Sets the auxiliary data.
     * @param auxdata the auxiliary data to set
     */
    public void setAuxdata(String auxdata)
    {
        this.auxdata = auxdata;
    }
}
