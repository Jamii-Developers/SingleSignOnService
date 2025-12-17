package com.jamii.databaseconfig.model;

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

@Entity
@Table(name = "user_relationship", schema = "jamiidb")
public class UserRelationshipTBL
{

    public static final String TABLE_NAME = "user_relationship";
    public static final String ID = "ID";
    public static final String SENDER_ID = "SENDER_ID";
    public static final String RECEIVER_ID = "RECEIVER_ID";
    public static final String TYPE = "TYPE";
    public static final String STATUS = "STATUS";
    public static final String DATE_UPDATED = "DATE_UPDATED";
    public static final String AUX_DATA = "AUX_DATA";

    @Id @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(name = TYPE, nullable = false) private Integer type;
    @Column(name = STATUS, nullable = false) private Integer status;
    @Column(name = DATE_UPDATED, nullable = false) private LocalDateTime dateupdated;
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;
    //Foreign Keys
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = SENDER_ID, nullable = false) private UserLoginTBL senderid;
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = RECEIVER_ID, nullable = false) private UserLoginTBL receiverid;

    public UserRelationshipTBL()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public LocalDateTime getDateupdated()
    {
        return dateupdated;
    }

    public void setDateupdated(LocalDateTime dateupdated)
    {
        this.dateupdated = dateupdated;
    }

    public UserLoginTBL getSenderid()
    {
        return senderid;
    }

    public void setSenderid(UserLoginTBL senderid)
    {
        this.senderid = senderid;
    }

    public UserLoginTBL getReceiverid()
    {
        return receiverid;
    }

    public void setReceiverid(UserLoginTBL receiverid)
    {
        this.receiverid = receiverid;
    }

    public String getAuxdata()
    {
        return auxdata;
    }

    public void setAuxdata(String auxdata)
    {
        this.auxdata = auxdata;
    }
}
