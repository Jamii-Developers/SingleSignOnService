package com.jamii.databaseconfig.model;

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
 * JPA Entity representing user requests in the database.
 * 
 * <p>This class maps to the {@code user_requests} table and stores information about
 * requests sent between users, such as friend requests or connection requests.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code senderid} - User who sent the request</li>
 *     <li>{@code receiverid} - User who received the request</li>
 *     <li>{@code type} - Type of request</li>
 *     <li>{@code status} - Status of the request</li>
 * </ul>
 */
@Entity
@Table(name = "user_requests")
public class UserRequestsTBL
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
     * Column name constant for sender ID.
     */
    public static final String SENDER_ID = "SENDER_ID";
    
    /**
     * Column name constant for receiver ID.
     */
    public static final String RECEIVER_ID = "RECEIVER_ID";
    
    /**
     * Column name constant for request type.
     */
    public static final String REQUEST_TYPE = "REQUEST_TYPE";
    
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
     * Status of the request.
     */
    @Column(name = STATUS, nullable = false, length = 1000) private Integer status;
    
    /**
     * Type of request.
     */
    @Column(name = REQUEST_TYPE, nullable = false, length = 1000) private Integer type;
    
    /**
     * Timestamp when the request was last updated.
     */
    @Column(name = DATE_UPDATED, nullable = false, length = 1000) private LocalDateTime dateupdated;
    
    /**
     * Auxiliary data stored as text.
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    /**
     * Foreign key reference to the user who sent the request.
     */
    @ManyToOne @JoinColumn(name = SENDER_ID, nullable = false) private UserLoginTBL senderid;
    
    /**
     * Foreign key reference to the user who received the request.
     */
    @ManyToOne @JoinColumn(name = RECEIVER_ID, nullable = false) private UserLoginTBL receiverid;

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
     * Gets the request status.
     * @return the status
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * Sets the request status.
     * @param status the status to set
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    /**
     * Gets the request type.
     * @return the request type
     */
    public Integer getType()
    {
        return type;
    }

    /**
     * Sets the request type.
     * @param type the request type to set
     */
    public void setType(Integer type)
    {
        this.type = type;
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
     * Gets the user who sent the request.
     * @return the user login entity
     */
    public UserLoginTBL getSenderid()
    {
        return senderid;
    }

    /**
     * Sets the user who sent the request.
     * @param senderid the user login entity to set
     */
    public void setSenderid(UserLoginTBL senderid)
    {
        this.senderid = senderid;
    }

    /**
     * Gets the user who received the request.
     * @return the user login entity
     */
    public UserLoginTBL getReceiverid()
    {
        return receiverid;
    }

    /**
     * Sets the user who received the request.
     * @param receiverid the user login entity to set
     */
    public void setReceiverid(UserLoginTBL receiverid)
    {
        this.receiverid = receiverid;
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
