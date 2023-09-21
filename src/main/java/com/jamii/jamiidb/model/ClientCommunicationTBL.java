package com.jamii.jamiidb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table( name = "client_communication" , schema =  "jamiidb")
public class ClientCommunicationTBL {

    public static final String      TABLE_NAME      = "contact_us";

    public static final String      ID              = "ID";
    public static final String      USER_LOGIN_ID   = "USER_LOGIN_ID";
    public static final String      CLIENT_THOUGHTS = "CLIENT_THOUGHTS";
    public static final String      TYPE_OF_THOUGHT = "TYPE_OF_THOUGHT";
    public static final String      DATE_OF_THOUGHT = "DATE_OF_THOUGHT";

    @Id
    @Column( name = ID )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = CLIENT_THOUGHTS, columnDefinition = "text" )
    private String clientthoughts;

    @Column( name = TYPE_OF_THOUGHT )
    private int typeofthought;

    @Column( name = DATE_OF_THOUGHT )
    private LocalDateTime dateofthought;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL userloginid;

    public final static Integer TYPE_OF_THOUGHT_CONTACT_US = 1 ; //

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientthoughts() {
        return clientthoughts;
    }

    public void setClientthoughts(String clientthoughts) {
        this.clientthoughts = clientthoughts;
    }

    public int getTypeofthought() {
        return typeofthought;
    }

    public void setTypeofthought(int typeofthought) {
        this.typeofthought = typeofthought;
    }

    public LocalDateTime getDateofthought() {
        return dateofthought;
    }

    public void setDateofthought(LocalDateTime dateofthought) {
        this.dateofthought = dateofthought;
    }

    public UserLoginTBL getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(UserLoginTBL userloginid) {
        this.userloginid = userloginid;
    }
}
