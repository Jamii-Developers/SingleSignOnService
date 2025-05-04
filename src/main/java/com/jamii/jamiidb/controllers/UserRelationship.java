package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.repo.UserRelationshipREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserRelationship {

    @Autowired
    private UserRelationshipREPO userRelationshipREPO;

    //Creating a table object to reference when creating data for that table
    public UserRelationshipTBL data = new UserRelationshipTBL( );
    public ArrayList< UserRelationshipTBL > dataList = new ArrayList< >( );

    // TYPE
    public static final Integer TYPE_FRIEND = 1;
    public static final Integer TYPE_FOLLOW = 2;
    // STATUS
    public static final Integer STATUS_INACTIVE = 0;
    public static final Integer STATUS_ACTIVE = 1;


    public void add(UserLoginTBL sender, UserLoginTBL receiver, int type, int status) {
        data.setSenderid(sender);
        data.setReceiverid(receiver);
        data.setType(type);
        data.setStatus(status);
        data.setDateupdated(LocalDateTime.now());
        save( );
    }

    public List<UserRelationshipTBL> fetch(UserLoginTBL sender, UserLoginTBL receiver, int type, int status) {
        return userRelationshipREPO.findBySenderidAndReceiveridAndTypeAndStatus(sender, receiver, type, status);
    }

    public List<UserRelationshipTBL> fetch(UserLoginTBL sender, UserLoginTBL receiver, int type) {
        return userRelationshipREPO.findBySenderidAndReceiveridAndType(sender, receiver, type);
    }

    public List<UserRelationshipTBL> fetch(UserLoginTBL sender, UserLoginTBL receiver) {
        return userRelationshipREPO.findBySenderidAndReceiverid(sender, receiver);
    }

    public List<UserRelationshipTBL> fetch(UserLoginTBL sender, int type, int status) {
        return userRelationshipREPO.findBySenderIdOrReceiverIdAndStatusAndType( sender.getId(), sender.getId(), type, status);
    }

    public List<UserRelationshipTBL> fetchFollowers(UserLoginTBL sender, int type, int status) {
        return userRelationshipREPO.findByReceiveridAndTypeAndStatus(sender, type, status);
    }

    /**
     * Use the function save( )
     * @param record
     */
    public void update(UserRelationshipTBL record) {
        userRelationshipREPO.save(record);
    }

    public void save( ){
        data = this.userRelationshipREPO.save( data );
    }

    public void saveAll( ){
        Iterable<UserRelationshipTBL> datalist = this.userRelationshipREPO.saveAll( dataList ) ;
        dataList.clear( );
        datalist.forEach( x -> dataList.add( x ) );
    }

}
