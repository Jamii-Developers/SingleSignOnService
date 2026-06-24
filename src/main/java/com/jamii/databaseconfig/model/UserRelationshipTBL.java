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
 * JPA Entity representing user relationships in the database.
 * 
 * <p>This class maps to the {@code user_relationship} table and stores information about
 * relationships between users, such as friendships or connections.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code senderid} - User who initiated the relationship</li>
 *     <li>{@code receiverid} - User who received the relationship request</li>
 *     <li>{@code type} - Type of relationship</li>
 *     <li>{@code status} - Status of the relationship</li>
 * </ul>
 */
@Entity
@Table(name = "user_relationship")
public class UserRelationshipTBL
{

    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "user_relationship";
    
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
     * Column name constant for type.
     */
    public static final String TYPE = "TYPE";
    
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
     * Type of relationship.
     */
    @Column(name = TYPE, nullable = false) private Integer type;
    
    /**
     * Status of the relationship.
     */
    @Column(name = STATUS, nullable = false) private Integer status;
    
    /**
     * Timestamp when the relationship was last updated.
     */
    @Column(name = DATE_UPDATED, nullable = false) private LocalDateTime dateupdated;
    
    /**
     * Auxiliary data stored as text.
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;
    
    /**
     * Foreign key reference to the user who initiated the relationship.
     */
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = SENDER_ID, nullable = false) private UserLoginTBL senderid;
    
    /**
     * Foreign key reference to the user who received the relationship request.
     */
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = RECEIVER_ID, nullable = false) private UserLoginTBL receiverid;

    /**
     * Default constructor.
     */
    public UserRelationshipTBL()
    {
    }

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
     * Gets the relationship type.
     * @return the type
     */
    public Integer getType()
    {
        return type;
    }

    /**
     * Sets the relationship type.
     * @param type the type to set
     */
    public void setType(Integer type)
    {
        this.type = type;
    }

    /**
     * Gets the relationship status.
     * @return the status
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * Sets the relationship status.
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
     * Gets the user who initiated the relationship.
     * @return the user login entity
     */
    public UserLoginTBL getSenderid()
    {
        return senderid;
    }

    /**
     * Sets the user who initiated the relationship.
     * @param senderid the user login entity to set
     */
    public void setSenderid(UserLoginTBL senderid)
    {
        this.senderid = senderid;
    }

    /**
     * Gets the user who received the relationship request.
     * @return the user login entity
     */
    public UserLoginTBL getReceiverid()
    {
        return receiverid;
    }

    /**
     * Sets the user who received the relationship request.
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
