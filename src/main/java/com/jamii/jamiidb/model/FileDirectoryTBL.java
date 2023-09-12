package com.jamii.jamiidb.model;

import jakarta.persistence.*;

@Entity
@Table( name = "file_directory" , schema =  "jamiidb")
public class FileDirectoryTBL {

    public FileDirectoryTBL() {
    }

    public static final String  ID                      = "ID";
    public static final String  FILE_TABLE_OWNER_ID     = "FILE_TABLE_OWNER_ID";
    public static final String  UI_DIRECTORY            = "UI_DIRECTORY";
    public static final String  STATUS                  = "STATUS";
    public static final String  LAST_UPDATED            = "LAST_UPDATED";

    @Id
    @Column( name = ID )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = UI_DIRECTORY , nullable = false, length = 500 )
    private String uidirectory;

    @Column( name = STATUS , nullable = false )
    private Integer status;

    @Column( name = LAST_UPDATED , nullable = false )
    private Integer lastupdated;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = FILE_TABLE_OWNER_ID, nullable = false )
    private FileTableOwnerTBL FK_FILE_TABLE_OWNER;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUidirectory() {
        return uidirectory;
    }

    public void setUidirectory(String uidirectory) {
        this.uidirectory = uidirectory;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(Integer lastupdated) {
        this.lastupdated = lastupdated;
    }

    public FileTableOwnerTBL getFK_FILE_TABLE_OWNER() {
        return FK_FILE_TABLE_OWNER;
    }

    public void setFK_FILE_TABLE_OWNER(FileTableOwnerTBL FK_FILE_TABLE_OWNER) {
        this.FK_FILE_TABLE_OWNER = FK_FILE_TABLE_OWNER;
    }
}
