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
    public static final String              DATE_CREATED        = "DATE_CREATED";

    @Id
    @Column( name = ID )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL FK_USER_LOGIN_DATA;

    @Column( name = ORIGINAL_FILENAME , nullable = false, length = 50, unique = true)
    private String originalfilename;

    @Column( name = DATE_CREATED )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private LocalDateTime datecreated;

    @OneToMany( mappedBy = "FK_FILE_TABLE_OWNER" )
    private List<FileDirectoryTBL> fIleDirectoryTBL;


}
