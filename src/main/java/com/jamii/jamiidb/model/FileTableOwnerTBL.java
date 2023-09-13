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
    public static final String              ORIGINAL_FILENAME   = "ORIGINAL_FILENAME";
    public static final String              SYSTEM_FILENAME     = "SYSTEM_FILENAME";
    public static final String              FILE_LOCATION       = "FILE_LOCATION";
    public static final String              FILE_SIZE           = "FILE_SIZE";
    public static final String              DATE_CREATED        = "DATE_CREATED";

    @Id
    @Column( name = ID )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = ORIGINAL_FILENAME , nullable = false, length = 200 )
    private String originalfilename;

    @Column( name = SYSTEM_FILENAME , nullable = false, length = 200 )
    private String getSystemFilename;

    @Column( name = FILE_LOCATION , nullable = false, length = 200 )
    private String filelocation;

    @Column( name = FILE_SIZE , nullable = false, length = 200 )
    private String filesize;

    @Column( name = DATE_CREATED )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private LocalDateTime datecreated;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL FK_USER_LOGIN_DATA;

    @OneToMany( mappedBy = "FK_FILE_TABLE_OWNER" )
    private List<FileDirectoryTBL> fIleDirectoryTBL;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalfilename() {
        return originalfilename;
    }

    public void setOriginalfilename(String originalfilename) {
        this.originalfilename = originalfilename;
    }

    public String getGetSystemFilename() {
        return getSystemFilename;
    }

    public void setGetSystemFilename(String getSystemFilename) {
        this.getSystemFilename = getSystemFilename;
    }

    public String getFilelocation() {
        return filelocation;
    }

    public void setFilelocation(String filelocation) {
        this.filelocation = filelocation;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public LocalDateTime getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(LocalDateTime datecreated) {
        this.datecreated = datecreated;
    }

    public UserLoginTBL getFK_USER_LOGIN_DATA() {
        return FK_USER_LOGIN_DATA;
    }

    public void setFK_USER_LOGIN_DATA(UserLoginTBL FK_USER_LOGIN_DATA) {
        this.FK_USER_LOGIN_DATA = FK_USER_LOGIN_DATA;
    }

    public List<FileDirectoryTBL> getfIleDirectoryTBL() {
        return fIleDirectoryTBL;
    }

    public void setfIleDirectoryTBL(List<FileDirectoryTBL> fIleDirectoryTBL) {
        this.fIleDirectoryTBL = fIleDirectoryTBL;
    }
}
