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

@Entity
@Table(name = "client_communication", schema = "jamiidb")
public class ClientCommunicationTBL
{

    public static final String TABLE_NAME = "contact_us";
    public static final String ID = "ID";
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    public static final String CLIENT_THOUGHTS = "CLIENT_THOUGHTS";
    public static final String TYPE_OF_THOUGHT = "TYPE_OF_THOUGHT";
    public static final String DATE_OF_THOUGHT = "DATE_OF_THOUGHT";
    public static final String AUX_DATA = "AUX_DATA";

    @Id @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(name = CLIENT_THOUGHTS, columnDefinition = "text") private String clientthoughts;
    @Column(name = TYPE_OF_THOUGHT) private int typeofthought;
    @Column(name = DATE_OF_THOUGHT) private LocalDateTime dateofthought;
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    //Foreign Keys
    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = USER_LOGIN_ID, nullable = false) private UserLoginTBL userloginid;


    public ClientCommunicationTBL()
    {
    }

    public String getAuxdata()
    {
        return auxdata;
    }

    public void setAuxdata(String auxdata)
    {
        this.auxdata = auxdata;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getClientthoughts()
    {
        return clientthoughts;
    }

    public void setClientthoughts(String clientthoughts)
    {
        this.clientthoughts = clientthoughts;
    }

    public int getTypeofthought()
    {
        return typeofthought;
    }

    public void setTypeofthought(int typeofthought)
    {
        this.typeofthought = typeofthought;
    }

    public LocalDateTime getDateofthought()
    {
        return dateofthought;
    }

    public void setDateofthought(LocalDateTime dateofthought)
    {
        this.dateofthought = dateofthought;
    }

    public UserLoginTBL getUserloginid()
    {
        return userloginid;
    }

    public void setUserloginid(UserLoginTBL userloginid)
    {
        this.userloginid = userloginid;
    }
}
