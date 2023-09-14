package com.jamii.jamiidb.model;

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
 *
 * Whenever we save directories we will use the "/" as the separator. With that being said all "/" user Directory should
 * be rejected at both front end and backend
 *
 * Additionally we will assume the punctuation mark "." as an indicator to the home directory, that means like above
 * we will reject any inputs of this mark at both the front end and the back end
 */

@Entity
@Table( name = "file_directory" , schema =  "jamiidb")
public class FileDirectoryTBL {

    public FileDirectoryTBL() {
    }

    public static final String  ID                      = "ID";
    public static final String  FILE_TABLE_OWNER_ID     = "FILE_TABLE_OWNER_ID";
    public static final String  USER_LOGIN_ID           = "USER_LOGIN_ID";
    public static final String  UI_DIRECTORY            = "UI_DIRECTORY";
    public static final String  LAST_UPDATED            = "LAST_UPDATED";

    @Id
    @Column( name = ID )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = UI_DIRECTORY , nullable = false, length = 500 )
    private String uidirectory;


    @Column( name = LAST_UPDATED , nullable = false )
    private LocalDateTime lastupdated;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = FILE_TABLE_OWNER_ID, nullable = false )
    private FileTableOwnerTBL filetableownderid;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL userloginid;

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

    public LocalDateTime getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(LocalDateTime lastupdated) {
        this.lastupdated = lastupdated;
    }

    public FileTableOwnerTBL getFiletableownderid() {
        return filetableownderid;
    }

    public void setFiletableownderid(FileTableOwnerTBL filetableownderid) {
        this.filetableownderid = filetableownderid;
    }

    public UserLoginTBL getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(UserLoginTBL userloginid) {
        this.userloginid = userloginid;
    }
}
