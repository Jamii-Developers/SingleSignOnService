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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA Entity representing file ownership records in the database.
 * 
 * <p>This class maps to the {@code file_table_owner} table and stores information about
 * files owned by users, including file metadata, location, size, and lifecycle status.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code userloginid} - User who owns this file</li>
 *     <li>{@code systemfilename} - System-generated filename</li>
 *     <li>{@code originalfilename} - Original filename uploaded by user</li>
 *     <li>{@code filetype} - Type/mime type of the file</li>
 *     <li>{@code filesize} - Size of the file in bytes</li>
 *     <li>{@code status} - Lifecycle status (store, in trash, deleted)</li>
 * </ul>
 */
@Entity
@Table(name = "file_table_owner")
public class FileTableOwnerTBL
{

    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for user login ID.
     */
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    
    /**
     * Column name constant for file type.
     */
    public static final String FILE_TYPE = "FILE_TYPE";
    
    /**
     * Column name constant for system filename.
     */
    public static final String SYSTEM_FILENAME = "SYSTEM_FILENAME";
    
    /**
     * Column name constant for file location.
     */
    public static final String FILE_LOCATION = "FILE_LOCATION";
    
    /**
     * Column name constant for original filename.
     */
    public static final String ORIGINAL_FILENAME = "ORIGINAL_FILENAME";
    
    /**
     * Column name constant for file size.
     */
    public static final String FILE_SIZE = "FILE_SIZE";
    
    /**
     * Column name constant for date created.
     */
    public static final String DATE_CREATED = "DATE_CREATED";
    
    /**
     * Column name constant for status.
     */
    public static final String STATUS = "STATUS";
    
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
     * System-generated filename for storage.
     */
    @Column(name = SYSTEM_FILENAME, nullable = false, length = 200) private String systemfilename;
    
    /**
     * Physical location where the file is stored.
     */
    @Column(name = FILE_LOCATION, nullable = false, length = 200) private String filelocation;
    
    /**
     * Type or mime type of the file.
     */
    @Column(name = FILE_TYPE, nullable = false, length = 200) private String filetype;
    
    /**
     * Original filename as uploaded by the user.
     */
    @Column(name = ORIGINAL_FILENAME, nullable = false, length = 200) private String originalfilename;
    
    /**
     * Size of the file in bytes.
     */
    @Column(name = FILE_SIZE, nullable = false) private Long filesize;
    
    /**
     * Timestamp when the file was created.
     */
    @Column(name = DATE_CREATED) private LocalDateTime datecreated;
    
    /**
     * Lifecycle status of the file (e.g., store, in trash, deleted).
     */
    @Column(name = STATUS, nullable = false) private Integer status;
    
    /**
     * Timestamp when the file record was last updated.
     */
    @Column(name = LAST_UPDATED, nullable = false) private LocalDateTime lastupdated;
    
    /**
     * Auxiliary data stored as text.
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    /**
     * Foreign key reference to the user who owns this file.
     */
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = USER_LOGIN_ID, nullable = false) private UserLoginTBL userloginid;
    
    /**
     * One-to-many relationship to file directories associated with this file.
     */
    @OneToMany(mappedBy = "filetableownerid") private List<FileDirectoryTBL> fIleDirectoryTBL;

    /**
     * Default constructor.
     */
    public FileTableOwnerTBL() {}

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
     * Gets the system filename.
     * @return the system filename
     */
    public String getSystemfilename()
    {
        return systemfilename;
    }

    /**
     * Sets the system filename.
     * @param systemfilename the system filename to set
     */
    public void setSystemfilename(String systemfilename)
    {
        this.systemfilename = systemfilename;
    }

    /**
     * Gets the file location.
     * @return the file location
     */
    public String getFilelocation()
    {
        return filelocation;
    }

    /**
     * Sets the file location.
     * @param filelocation the file location to set
     */
    public void setFilelocation(String filelocation)
    {
        this.filelocation = filelocation;
    }

    /**
     * Gets the file type.
     * @return the file type
     */
    public String getFiletype()
    {
        return filetype;
    }

    /**
     * Sets the file type.
     * @param filetype the file type to set
     */
    public void setFiletype(String filetype)
    {
        this.filetype = filetype;
    }

    /**
     * Gets the original filename.
     * @return the original filename
     */
    public String getOriginalfilename()
    {
        return originalfilename;
    }

    /**
     * Sets the original filename.
     * @param originalfilename the original filename to set
     */
    public void setOriginalfilename(String originalfilename)
    {
        this.originalfilename = originalfilename;
    }

    /**
     * Gets the file size.
     * @return the file size in bytes
     */
    public Long getFilesize()
    {
        return filesize;
    }

    /**
     * Sets the file size.
     * @param filesize the file size to set
     */
    public void setFilesize(Long filesize)
    {
        this.filesize = filesize;
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
     * Gets the user who owns this file.
     * @return the user login entity
     */
    public UserLoginTBL getUserloginid()
    {
        return userloginid;
    }

    /**
     * Sets the user who owns this file.
     * @param userloginid the user login entity to set
     */
    public void setUserloginid(UserLoginTBL userloginid)
    {
        this.userloginid = userloginid;
    }

    /**
     * Gets the list of file directories associated with this file.
     * @return the list of file directories
     */
    public List<FileDirectoryTBL> getfIleDirectoryTBL()
    {
        return fIleDirectoryTBL;
    }

    /**
     * Sets the list of file directories associated with this file.
     * @param fIleDirectoryTBL the list of file directories to set
     */
    public void setfIleDirectoryTBL(List<FileDirectoryTBL> fIleDirectoryTBL)
    {
        this.fIleDirectoryTBL = fIleDirectoryTBL;
    }

    /**
     * Gets the lifecycle status.
     * @return the status
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * Sets the lifecycle status.
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
