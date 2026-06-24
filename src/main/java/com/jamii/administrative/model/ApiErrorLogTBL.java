package com.jamii.administrative.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * JPA Entity representing system error logs in the database.
 * 
 * <p>This class maps to the {@code system_error_logs} table and stores information about
 * errors that occur in the application, including the class where the error occurred,
 * error message, error type, and associated user.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code userloginid} - ID of the user who experienced the error</li>
 *     <li>{@code classname} - Class where the error occurred</li>
 *     <li>{@code errortype} - Type/category of error</li>
 *     <li>{@code ErrorMessage} - Detailed error message</li>
 *     <li>{@code auxdata} - Additional data (e.g., stack trace) stored as JSON</li>
 * </ul>
 */
@Entity
@Table(name = "system_error_logs")
public class ApiErrorLogTBL
{

    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "system_error_logs";
    
    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for user login ID.
     */
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    
    /**
     * Column name constant for class name.
     */
    public static final String CLASS_NAME = "CLASS_NAME";
    
    /**
     * Column name constant for error message.
     */
    public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
    
    /**
     * Column name constant for creation date.
     */
    public static final String CREATION_DATE = "CREATION_DATE";
    
    /**
     * Column name constant for error type.
     */
    public static final String ERROR_TYPE = "ERROR_TYPE";
    
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
     * ID of the user who experienced the error.
     */
    @Column(name = USER_LOGIN_ID, nullable = false) private Integer userloginid;
    
    /**
     * Type or category of the error.
     */
    @Column(name = ERROR_TYPE, nullable = false) private Integer errortype;
    
    /**
     * Name of the class where the error occurred.
     */
    @Column(name = CLASS_NAME, nullable = false) private String classname;
    
    /**
     * Detailed error message.
     */
    @Column(name = ERROR_MESSAGE, nullable = false) private String ErrorMessage;
    
    /**
     * Timestamp when the error was created.
     */
    @Column(name = CREATION_DATE, nullable = false) private LocalDateTime creationdate;
    
    /**
     * Status of the error log entry.
     */
    @Column(name = STATUS, nullable = false) private Integer status;
    
    /**
     * Timestamp when the error log was last updated.
     */
    @Column(name = LAST_UPDATED, nullable = false) private LocalDateTime lastupdated;
    
    /**
     * Auxiliary data stored as text (e.g., stack trace in JSON format).
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    /**
     * Default constructor.
     */
    public ApiErrorLogTBL() {}

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
     * Gets the user login ID.
     * @return the user login ID
     */
    public Integer getUserloginid()
    {
        return userloginid;
    }

    /**
     * Sets the user login ID.
     * @param userloginid the user login ID to set
     */
    public void setUserloginid(Integer userloginid)
    {
        this.userloginid = userloginid;
    }

    /**
     * Gets the class name where the error occurred.
     * @return the class name
     */
    public String getClassname()
    {
        return classname;
    }

    /**
     * Sets the class name where the error occurred.
     * @param classname the class name to set
     */
    public void setClassname(String classname)
    {
        this.classname = classname;
    }

    /**
     * Gets the error message.
     * @return the error message
     */
    public String getErrorMessage()
    {
        return ErrorMessage;
    }

    /**
     * Sets the error message.
     * @param errorMessage the error message to set
     */
    public void setErrorMessage(String errorMessage)
    {
        ErrorMessage = errorMessage;
    }

    /**
     * Gets the creation date timestamp.
     * @return the creation date
     */
    public LocalDateTime getCreationdate()
    {
        return creationdate;
    }

    /**
     * Sets the creation date timestamp.
     * @param creationdate the creation date to set
     */
    public void setCreationdate(LocalDateTime creationdate)
    {
        this.creationdate = creationdate;
    }

    /**
     * Gets the status of the error log.
     * @return the status
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * Sets the status of the error log.
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

    /**
     * Gets the error type.
     * @return the error type
     */
    public Integer getErrortype()
    {
        return errortype;
    }

    /**
     * Sets the error type.
     * @param errortype the error type to set
     */
    public void setErrortype(Integer errortype)
    {
        this.errortype = errortype;
    }
}
