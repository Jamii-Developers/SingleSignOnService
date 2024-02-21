package com.jamii.jamiidb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    @Column( name = TYPE , nullable = false, length = 1000 )
    private Integer type;

    @Column( name = STATUS , nullable = false, length = 1000 )
    private Integer status;

    @Column( name = DATE_UPDATED , nullable = false, length = 1000 )
    private LocalDateTime dateupdated;

    //Foreign Keys
    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = SENDER_ID, nullable = false )
    private UserLoginTBL senderid;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = RECEIVER_ID, nullable = false )
    private UserLoginTBL receiverid;

    // TYPE
    public static final Integer TYPE_FRIEND    = 1;
    public static final Integer TYPE_FOLLOW    = 2;

    // STATUS
    public static final Integer STATUS_PENDING  = 1;
    public static final Integer STATUS_ACCEPTED = 2;
    public static final Integer STATUS_REJECTED = 3;
    public static final Integer STATUS_BLOCKED  = 4;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getDateupdated() {
        return dateupdated;
    }

    public void setDateupdated(LocalDateTime dateupdated) {
        this.dateupdated = dateupdated;
    }

    public UserLoginTBL getSenderid() {
        return senderid;
    }

    public void setSenderid(UserLoginTBL senderid) {
        this.senderid = senderid;
    }

    public UserLoginTBL getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(UserLoginTBL receiverid) {
        this.receiverid = receiverid;
    }
}
