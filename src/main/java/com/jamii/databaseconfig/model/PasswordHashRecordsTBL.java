package com.jamii.databaseconfig.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/**
 * JPA Entity representing password hash records in the database.
 * 
 * <p>This class maps to the {@code Password_Hash_Records} table and stores password hashes
 * for users to track password history and prevent password reuse.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code userloginid} - User this password belongs to</li>
 *     <li>{@code passwordsalt} - Hashed password value</li>
 *     <li>{@code dateadded} - When this password was added</li>
 * </ul>
 */
@Entity
@Table(name = "Password_Hash_Records")
public class PasswordHashRecordsTBL
{

    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "PasswordHashRecords";
    
    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for user login ID.
     */
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    
    /**
     * Column name constant for password salt.
     */
    public static final String PASSWORD_SALT = "PASSWORD_SALT";
    
    /**
     * Column name constant for date added.
     */
    public static final String DATE_ADDED = "DATE_ADDED";
    
    /**
     * Column name constant for auxiliary data.
     */
    public static final String AUX_DATA = "AUX_DATA";

    /**
     * Primary key identifier.
     */
    @Id @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    
    /**
     * Hashed password value.
     */
    @Column(name = PASSWORD_SALT, nullable = false, length = 1000) private String passwordsalt;
    
    /**
     * Timestamp when this password was added.
     */
    @Column(name = DATE_ADDED, nullable = false, length = 100) private LocalDateTime dateadded;
    
    /**
     * Auxiliary data stored as text.
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    /**
     * Foreign key reference to the user this password belongs to.
     */
    @ManyToOne @JoinColumn(name = USER_LOGIN_ID, nullable = false) private UserLoginTBL userloginid;
    
    /**
     * One-to-one relationship to password history records.
     */
    @OneToOne( mappedBy = "passwordhashrecordsid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE) private PasswordHashRecordsHistoryTBL passwordHashRecordsHistory;


    /**
     * Default constructor.
     */
    public PasswordHashRecordsTBL()
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
     * Gets the hashed password.
     * @return the password salt
     */
    public String getPasswordsalt()
    {
        return passwordsalt;
    }

    /**
     * Sets the hashed password.
     * @param passwordsalt the password salt to set
     */
    public void setPasswordsalt(String passwordsalt)
    {
        this.passwordsalt = passwordsalt;
    }

    /**
     * Gets the date added timestamp.
     * @return the date added
     */
    public LocalDateTime getDateadded()
    {
        return dateadded;
    }

    /**
     * Sets the date added timestamp.
     * @param dateadded the date added to set
     */
    public void setDateadded(LocalDateTime dateadded)
    {
        this.dateadded = dateadded;
    }

    /**
     * Gets the user this password belongs to.
     * @return the user login entity
     */
    public UserLoginTBL getUserloginid()
    {
        return userloginid;
    }

    /**
     * Sets the user this password belongs to.
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
}
