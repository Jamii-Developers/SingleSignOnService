package com.jamii.users.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA Entity representing device information in the database.
 * 
 * <p>This class maps to the {@code device_information} table and stores information
 * about devices used by users, including device name, unique key, location, and last connection time.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code userloginid} - User who owns this device</li>
 *     <li>{@code devicename} - Name of the device</li>
 *     <li>{@code devicekey} - Unique key identifying the device</li>
 *     <li>{@code location} - Geographic location of the device</li>
 *     <li>{@code active} - Active status of the device</li>
 * </ul>
 */
@Entity
@Table(name = "device_information")
public class DeviceInformationTBL
{

    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "device_information";
    
    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for user login ID.
     */
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    
    /**
     * Column name constant for device name.
     */
    public static final String DEVICE_NAME = "DEVICE_NAME";
    
    /**
     * Column name constant for device key.
     */
    public static final String DEVICE_KEY = "DEVICE_KEY";
    
    /**
     * Column name constant for location.
     */
    public static final String LOCATION = "LOCATION";
    
    /**
     * Column name constant for last connected timestamp.
     */
    public static final String LAST_CONNECTED = "LAST_CONNECTED";
    
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
     * Name of the device.
     */
    @Column(name = DEVICE_NAME, length = 200) private String devicename;
    
    /**
     * Unique key identifying the device.
     */
    @Column(name = DEVICE_KEY, length = 200) private String devicekey;
    
    /**
     * Geographic location of the device.
     */
    @Column(name = LOCATION, length = 50) private String location;
    
    /**
     * Timestamp of the last connection from this device.
     */
    @Column(name = LAST_CONNECTED) private LocalDateTime lastconnected;
    
    /**
     * Active status of the device.
     */
    @Column(name = ACTIVE) private Integer active;
    
    /**
     * Auxiliary data stored as text.
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    /**
     * One-to-many relationship to user cookies associated with this device.
     */
    @OneToMany(mappedBy = "deviceinformationid") private List<UserCookiesTBL> userCookiesTBL;
    
    /**
     * Foreign key reference to the user who owns this device.
     */
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = USER_LOGIN_ID, nullable = false) private UserLoginTBL userloginid;

    /**
     * Default constructor.
     */
    public DeviceInformationTBL() {}

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
     * Gets the device name.
     * @return the device name
     */
    public String getDevicename()
    {
        return devicename;
    }

    /**
     * Sets the device name.
     * @param devicename the device name to set
     */
    public void setDevicename(String devicename)
    {
        this.devicename = devicename;
    }

    /**
     * Gets the last connected timestamp.
     * @return the last connected timestamp
     */
    public LocalDateTime getLastconnected()
    {
        return lastconnected;
    }

    /**
     * Sets the last connected timestamp.
     * @param lastconnected the last connected timestamp to set
     */
    public void setLastconnected(LocalDateTime lastconnected)
    {
        this.lastconnected = lastconnected;
    }

    /**
     * Gets the location of the device.
     * @return the location
     */
    public String getLocation()
    {
        return location;
    }

    /**
     * Sets the location of the device.
     * @param location the location to set
     */
    public void setLocation(String location)
    {
        this.location = location;
    }

    /**
     * Gets the active status.
     * @return the active status
     */
    public Integer getActive()
    {
        return active;
    }

    /**
     * Sets the active status.
     * @param active the active status to set
     */
    public void setActive(Integer active)
    {
        this.active = active;
    }

    /**
     * Gets the device key.
     * @return the device key
     */
    public String getDevicekey()
    {
        return devicekey;
    }

    /**
     * Sets the device key.
     * @param devicekey the device key to set
     */
    public void setDevicekey(String devicekey)
    {
        this.devicekey = devicekey;
    }

    /**
     * Gets the user who owns this device.
     * @return the user login entity
     */
    public UserLoginTBL getUserloginid()
    {
        return userloginid;
    }

    /**
     * Sets the user who owns this device.
     * @param userloginid the user login entity to set
     */
    public void setUserloginid(UserLoginTBL userloginid)
    {
        this.userloginid = userloginid;
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

    /**
     * Gets the list of user cookies associated with this device.
     * @return the list of user cookies
     */
    public List<UserCookiesTBL> getUserCookiesTBL()
    {
        return userCookiesTBL;
    }

    /**
     * Sets the list of user cookies associated with this device.
     * @param userCookiesTBL the list of user cookies to set
     */
    public void setUserCookiesTBL(List<UserCookiesTBL> userCookiesTBL)
    {
        this.userCookiesTBL = userCookiesTBL;
    }
}
