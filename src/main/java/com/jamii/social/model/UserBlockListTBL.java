package com.jamii.social.model;

import com.jamii.users.model.UserLoginTBL;
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
 * JPA Entity representing user block list records in the database.
 * 
 * <p>This class maps to the {@code user_block_list} table and stores information about
 * users who have been blocked by other users, preventing interactions between them.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code userid} - User who blocked someone</li>
 *     <li>{@code blockedid} - User who was blocked</li>
 *     <li>{@code status} - Status of the block</li>
 * </ul>
 */
@Entity
@Table(name = "user_block_list")
public class UserBlockListTBL
{

    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "user_requests";
    
    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for user ID.
     */
    public static final String USER_ID = "USER_ID";
    
    /**
     * Column name constant for blocked user ID.
     */
    public static final String BLOCKED_ID = "BLOCKED_ID";
    
    /**
     * Column name constant for status.
     */
    public static final String STATUS = "STATUS";
    
    /**
     * Column name constant for date updated.
     */
    public static final String DATE_UPDATED = "DATE_UPDATED";
    
    /**
     * Column name constant for auxiliary data.
     */
    public static final String AUX_DATA = "AUX_DATA";

    /**
     * Primary key identifier.
     */
    @Id @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    
    /**
     * Status of the block.
     */
    @Column(name = STATUS, nullable = false) private Integer status;
    
    /**
     * Timestamp when the block was last updated.
     */
    @Column(name = DATE_UPDATED, nullable = false) private LocalDateTime dateupdated;
    
    /**
     * Auxiliary data stored as text.
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    /**
     * Foreign key reference to the user who blocked someone.
     */
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = USER_ID, nullable = false) private UserLoginTBL userid;
    
    /**
     * Foreign key reference to the user who was blocked.
     */
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = BLOCKED_ID, nullable = false) private UserLoginTBL blockedid;

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
     * Gets the block status.
     * @return the status
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * Sets the block status.
     * @param status the status to set
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    /**
     * Gets the last updated timestamp.
     * @return the last updated timestamp
     */
    public LocalDateTime getDateupdated()
    {
        return dateupdated;
    }

    /**
     * Sets the last updated timestamp.
     * @param dateupdated the last updated timestamp to set
     */
    public void setDateupdated(LocalDateTime dateupdated)
    {
        this.dateupdated = dateupdated;
    }

    /**
     * Gets the user who blocked someone.
     * @return the user login entity
     */
    public UserLoginTBL getUserid()
    {
        return userid;
    }

    /**
     * Sets the user who blocked someone.
     * @param userid the user login entity to set
     */
    public void setUserid(UserLoginTBL userid)
    {
        this.userid = userid;
    }

    /**
     * Gets the user who was blocked.
     * @return the user login entity
     */
    public UserLoginTBL getBlockedid()
    {
        return blockedid;
    }

    /**
     * Sets the user who was blocked.
     * @param blockedid the user login entity to set
     */
    public void setBlockedid(UserLoginTBL blockedid)
    {
        this.blockedid = blockedid;
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
