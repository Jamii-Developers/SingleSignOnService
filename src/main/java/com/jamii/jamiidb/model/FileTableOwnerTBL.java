package com.jamii.jamiidb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table( name = "file_table_owner" , schema =  "jamiidb")
public class FileTableOwnerTBL {

    public FileTableOwnerTBL( ) { }

    public static final String              ID                  = "ID";
    public static final String              USER_LOGIN_ID       = "USER_LOGIN_ID";
    public static final String              FILE_TYPE           = "FILE_TYPE";
    public static final String              SYSTEM_FILENAME     = "SYSTEM_FILENAME";
    public static final String              FILE_LOCATION       = "FILE_LOCATION";
    public static final String              FILE_SIZE           = "FILE_SIZE";
    public static final String              DATE_CREATED        = "DATE_CREATED";
    public static final String              STATUS              = "STATUS";

    @Id
    @Column( name = ID )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = SYSTEM_FILENAME , nullable = false, length = 200 )
    private String SystemFilename;

    @Column( name = FILE_LOCATION , nullable = false, length = 200 )
    private String filelocation;

    @Column( name = FILE_TYPE , nullable = false, length = 200 )
    private String filetype;

    @Column( name = FILE_SIZE , nullable = false)
    private Long filesize;

    @Column( name = DATE_CREATED )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private LocalDateTime datecreated;

    @Column( name = STATUS , nullable = false)
    private Integer status;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL userloginid;

    @OneToMany( mappedBy = "FK_FILE_TABLE_OWNER" )
    private List<FileDirectoryTBL> fIleDirectoryTBL;

    /**
     * Setting active statuses
     */

    public final static Integer ACTIVE_STATUS_STORE         = 1;
    public final static Integer ACTIVE_STATUS_DELETED       = 2;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getSystemFilename() {
        return SystemFilename;
    }

    public void setSystemFilename(String systemFilename) {
        SystemFilename = systemFilename;
    }

    public String getFilelocation() {
        return filelocation;
    }

    public void setFilelocation(String filelocation) {
        this.filelocation = filelocation;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize( Long filesize) {
        this.filesize = filesize;
    }

    public LocalDateTime getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(LocalDateTime datecreated) {
        this.datecreated = datecreated;
    }

    public UserLoginTBL getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(UserLoginTBL userloginid) {
        this.userloginid = userloginid;
    }

    public List<FileDirectoryTBL> getfIleDirectoryTBL() {
        return fIleDirectoryTBL;
    }

    public void setfIleDirectoryTBL(List<FileDirectoryTBL> fIleDirectoryTBL) {
        this.fIleDirectoryTBL = fIleDirectoryTBL;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
