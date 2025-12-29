package com.jamii.databaseconfig.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Password_Hash_Records_History", schema = "jamiidb")
public class PasswordHashRecordsHistoryTBL {

    public static final String TABLE_NAME = "PasswordHashRecordsHistory";
    public static final String ID = "ID";
    public static final String PASSWORD_HASH_RECORD_ID = "PASSWORD_HASH_RECORD_ID";
    public static final String PASSWORD_SALT = "PASSWORD_SALT";
    public static final String DATE_ADDED = "DATE_ADDED";
    public static final String AUX_DATA = "AUX_DATA";

    @Id
    @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(name = PASSWORD_SALT, nullable = false, length = 1000) private String passwordsalt;
    @Column(name = DATE_ADDED, nullable = false, length = 100) private LocalDateTime dateadded;
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    //Foreign Keys
    @ManyToOne @JoinColumn(name = PASSWORD_HASH_RECORD_ID, nullable = false) private PasswordHashRecordsTBL passwordhashrecordsid;
}
