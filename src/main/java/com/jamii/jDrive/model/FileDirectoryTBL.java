package com.jamii.jDrive.model;

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
 * JPA Entity representing file directories in the database.
 * 
 * <p>This class maps to the {@code file_directory} table and stores directory information
 * for organizing user files. Directories use "/" as a separator and reject inputs containing
 * "/" or "." (home directory indicator) at both frontend and backend.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code filetableownerid} - File owner this directory belongs to</li>
 *     <li>{@code userloginid} - User who owns this directory</li>
 *     <li>{@code uidirectory} - UI representation of the directory path</li>
 * </ul>
 */
@Entity
@Table(name = "file_directory")
public class FileDirectoryTBL
{

    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for file table owner ID.
     */
    public static final String FILE_TABLE_OWNER_ID = "FILE_TABLE_OWNER_ID";
    
    /**
     * Column name constant for user login ID.
     */
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    
    /**
     * Column name constant for UI directory.
     */
    public static final String UI_DIRECTORY = "UI_DIRECTORY";
    
    /**
     * Column name constant for last updated timestamp.
     */
    public static final String LAST_UPDATED = "LAST_UPDATED";
    
    /**
     * Column name constant for auxiliary data.
     */
    public static final String AUX_DATA = "AUX_DATA";

    /**
     * Primary key identifier.
     */
    @Id @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    
    /**
     * UI representation of the directory path.
     * 
     * <p>Note: "/" is used as the separator. Inputs containing "/" or "." are rejected
     * at both frontend and backend.</p>
     */
    @Column(name = UI_DIRECTORY, nullable = false, length = 500) private String uidirectory;
    
    /**
     * Timestamp when the directory was last updated.
     */
    @Column(name = LAST_UPDATED, nullable = false) private LocalDateTime lastupdated;
    
    /**
     * Auxiliary data stored as text.
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    /**
     * Foreign key reference to the file table owner.
     */
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = FILE_TABLE_OWNER_ID, nullable = false) private FileTableOwnerTBL filetableownerid;
    
    /**
     * Foreign key reference to the user who owns this directory.
     */
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = USER_LOGIN_ID, nullable = false) private UserLoginTBL userloginid;


    /**
     * Default constructor.
     */
    public FileDirectoryTBL()
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
     * Gets the UI directory path.
     * @return the UI directory
     */
    public String getUidirectory()
    {
        return uidirectory;
    }

    /**
     * Sets the UI directory path.
     * @param uidirectory the UI directory to set
     */
    public void setUidirectory(String uidirectory)
    {
        this.uidirectory = uidirectory;
    }

    /**
     * Gets the last updated timestamp.
     * @return the last updated timestamp
     */
    public LocalDateTime getLastupdated()
    {
        return lastupdated;
    }

    /**
     * Sets the last updated timestamp.
     * @param lastupdated the last updated timestamp to set
     */
    public void setLastupdated(LocalDateTime lastupdated)
    {
        this.lastupdated = lastupdated;
    }

    /**
     * Gets the file table owner.
     * @return the file table owner entity
     */
    public FileTableOwnerTBL getFiletableownerid()
    {
        return filetableownerid;
    }

    /**
     * Sets the file table owner.
     * @param filetableownerid the file table owner entity to set
     */
    public void setFiletableownerid(FileTableOwnerTBL filetableownerid)
    {
        this.filetableownerid = filetableownerid;
    }

    /**
     * Gets the user who owns this directory.
     * @return the user login entity
     */
    public UserLoginTBL getUserloginid()
    {
        return userloginid;
    }

    /**
     * Sets the user who owns this directory.
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
