package com.jamii.jamiidb.model;

import jakarta.persistence.*;

@Entity
@Table( name = "user_relationship" , schema =  "jamiidb")
public class UserRelationshipTBL {

    public UserRelationshipTBL() {
    }

    public static final String              TABLE_NAME      = "user_relationship";
    public static final String              ID              = "ID";
    public static final String              SENDER_ID       = "SENDER_ID";
    public static final String              RECEIVER_ID     = "RECEIVER_ID";
    public static final String              TYPE            = "TYPE";
    public static final String              STATUS          = "STATUS";
    public static final String              DATE_UPDATED    = "DATE_UPDATED";

    @Id
    @Column( name = ID)
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;
}
