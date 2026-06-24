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
 * JPA Entity representing client communication records in the database.
 * 
 * <p>This class maps to the {@code client_communication} table and stores communication
 * from clients, such as support requests, reviews, or other feedback.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code userloginid} - User who sent the communication</li>
 *     <li>{@code clientthoughts} - The content/message from the client</li>
 *     <li>{@code typeofthought} - Type of communication (e.g., support, review)</li>
 *     <li>{@code dateofthought} - When the communication was sent</li>
 * </ul>
 */
@Entity
@Table(name = "client_communication")
public class ClientCommunicationTBL
{

    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "contact_us";
    
    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for user login ID.
     */
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    
    /**
     * Column name constant for client thoughts/message.
     */
    public static final String CLIENT_THOUGHTS = "CLIENT_THOUGHTS";
    
    /**
     * Column name constant for type of thought.
     */
    public static final String TYPE_OF_THOUGHT = "TYPE_OF_THOUGHT";
    
    /**
     * Column name constant for date of thought.
     */
    public static final String DATE_OF_THOUGHT = "DATE_OF_THOUGHT";
    
    /**
     * Column name constant for auxiliary data.
     */
    public static final String AUX_DATA = "AUX_DATA";

    /**
     * Primary key identifier.
     */
    @Id @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    
    /**
     * The content or message from the client.
     */
    @Column(name = CLIENT_THOUGHTS, columnDefinition = "text") private String clientthoughts;
    
    /**
     * Type of communication (e.g., support request, review, feedback).
     */
    @Column(name = TYPE_OF_THOUGHT) private int typeofthought;
    
    /**
     * Timestamp when the communication was sent.
     */
    @Column(name = DATE_OF_THOUGHT) private LocalDateTime dateofthought;
    
    /**
     * Auxiliary data stored as text.
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    /**
     * Foreign key reference to the user who sent the communication.
     */
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = USER_LOGIN_ID, nullable = false) private UserLoginTBL userloginid;


    /**
     * Default constructor.
     */
    public ClientCommunicationTBL()
    {
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
     * Gets the client's message/thoughts.
     * @return the client thoughts
     */
    public String getClientthoughts()
    {
        return clientthoughts;
    }

    /**
     * Sets the client's message/thoughts.
     * @param clientthoughts the client thoughts to set
     */
    public void setClientthoughts(String clientthoughts)
    {
        this.clientthoughts = clientthoughts;
    }

    /**
     * Gets the type of communication.
     * @return the type of thought
     */
    public int getTypeofthought()
    {
        return typeofthought;
    }

    /**
     * Sets the type of communication.
     * @param typeofthought the type of thought to set
     */
    public void setTypeofthought(int typeofthought)
    {
        this.typeofthought = typeofthought;
    }

    /**
     * Gets the date when the communication was sent.
     * @return the date of thought
     */
    public LocalDateTime getDateofthought()
    {
        return dateofthought;
    }

    /**
     * Sets the date when the communication was sent.
     * @param dateofthought the date of thought to set
     */
    public void setDateofthought(LocalDateTime dateofthought)
    {
        this.dateofthought = dateofthought;
    }

    /**
     * Gets the user who sent the communication.
     * @return the user login entity
     */
    public UserLoginTBL getUserloginid()
    {
        return userloginid;
    }

    /**
     * Sets the user who sent the communication.
     * @param userloginid the user login entity to set
     */
    public void setUserloginid(UserLoginTBL userloginid)
    {
        this.userloginid = userloginid;
    }
}
