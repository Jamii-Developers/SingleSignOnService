package com.jamii.databaseconfig.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * JPA Entity representing password hash history records in the database.
 * 
 * <p>This class maps to the {@code Password_Hash_Records_History} table and stores
 * historical snapshots of password hashes for archival purposes.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code passwordhashrecordsid} - Reference to the password hash record</li>
 *     <li>{@code passwordsalt} - Hashed password value from history</li>
 *     <li>{@code dateadded} - When this password was added</li>
 * </ul>
 */
@Entity
@Table(name = "Password_Hash_Records_History")
public class PasswordHashRecordsHistoryTBL {

    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "PasswordHashRecordsHistory";
    
    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for password hash record ID.
     */
    public static final String PASSWORD_HASH_RECORD_ID = "PASSWORD_HASH_RECORD_ID";
    
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
    @Id
    @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    
    /**
     * Hashed password value from history.
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
     * Foreign key reference to the password hash record.
     */
    @ManyToOne @JoinColumn(name = PASSWORD_HASH_RECORD_ID, nullable = false) private PasswordHashRecordsTBL passwordhashrecordsid;
}
