package com.jamii.jamiidb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table( name = "system_error_logs" , schema =  "jamiidb")
public class ApiErrorLogTBL {

    public ApiErrorLogTBL( ) {}

    public static final String              TABLE_NAME      = "system_error_logs";
    public static final String              ID              = "ID";
    public static final String              USER_LOGIN_ID   = "USER_LOGIN_ID";
    public static final String              CLASS_NAME      = "CLASS_NAME";
    public static final String              ERROR_MESSAGE   = "ERROR_MESSAGE";
    public static final String              CREATION_DATE   = "CREATION_DATE";
    public static final String              ERROR_TYPE      = "ERROR_TYPE";
    public static final String              STATUS          = "STATUS";
    public static final String              LAST_UPDATED    = "LAST_UPDATED";
    public static final String              AUX_DATA        = "AUX_DATA";

    @Id
    @Column( name = ID )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = USER_LOGIN_ID, nullable = false )
    private Integer userloginid;

    @Column( name = ERROR_TYPE, nullable = false )
    private Integer errortype;

    @Column( name = CLASS_NAME, nullable = false )
    private String classname;

    @Column( name = ERROR_MESSAGE, nullable = false )
    private String ErrorMessage;

    @Column( name = CREATION_DATE , nullable = false)
    private LocalDateTime creationdate;

    @Column( name = STATUS , nullable = false)
    private Integer status;

    @Column( name = LAST_UPDATED , nullable = false)
    private LocalDateTime lastupdated;

    @Column( name = AUX_DATA, columnDefinition = "text" )
    private String auxdata;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(Integer userloginid) {
        this.userloginid = userloginid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public LocalDateTime getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(LocalDateTime creationdate) {
        this.creationdate = creationdate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(LocalDateTime lastupdated) {
        this.lastupdated = lastupdated;
    }

    public String getAuxdata() {
        return auxdata;
    }

    public void setAuxdata( String auxdata) {
        this.auxdata = auxdata;
    }

    public Integer getErrortype() {
        return errortype;
    }

    public void setErrortype(Integer errortype) {
        this.errortype = errortype;
    }
}
