package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.jamiidb.repo.UserRequestsREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserRequest {


    @Autowired
    UserRequestsREPO userRequestsREPO;

    //Creating a table object to reference when creating data for that table
    public UserRequestsTBL data = new UserRequestsTBL( );
    public ArrayList< UserRequestsTBL > dataList = new ArrayList< >( );

    // REQUEST_TYPE
    public static final Integer TYPE_FRIEND = 1;
    public static final Integer TYPE_FOLLOW = 2;
    // STATUS
    public static final Integer STATUS_INACTIVE = 0;
    public static final Integer STATUS_ACTIVE = 1;


    public List<UserRequestsTBL> fetch(UserLoginTBL user_1, UserLoginTBL user_2) {
        return userRequestsREPO.findBySenderidAndReceiverid(user_1, user_2);
    }

    public List<UserRequestsTBL> fetch(UserLoginTBL user_1, UserLoginTBL user_2, Integer status) {
        return userRequestsREPO.findBySenderidAndReceiveridAndStatus(user_1, user_2, status);
    }

    public List<UserRequestsTBL> fetch(UserLoginTBL user_1, UserLoginTBL user_2, Integer type, Integer status) {
        return userRequestsREPO.findBySenderidAndReceiveridAndTypeAndStatus(user_1, user_2, type, status);
    }

    public List<UserRequestsTBL> fetchRequests(UserLoginTBL user, Integer type, Integer status) {
        return userRequestsREPO.findByReceiveridAndTypeAndStatus(user, type, status);
    }

    public void add(UserLoginTBL senderid, UserLoginTBL receiverid, Integer type, Integer status) {
        data.setSenderid(senderid);
        data.setReceiverid(receiverid);
        data.setType(type);
        data.setStatus(status);
        data.setDateupdated(LocalDateTime.now());
        save( );
    }

    /**
     * Use the function save( )
     * @param request
     */
    @Deprecated
    public void update(UserRequestsTBL request) {
        userRequestsREPO.save(request);
    }

    public void save( ){
        data = this.userRequestsREPO.save( data );
    }

    public void saveAll( ){
        Iterable<UserRequestsTBL> datalist = this.userRequestsREPO.saveAll( dataList ) ;
        dataList.clear( );
        datalist.forEach( x -> dataList.add( x ) );
    }


}
