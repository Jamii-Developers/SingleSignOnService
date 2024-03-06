package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.jamiidb.repo.UserRequestsREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserRequestCONT {

    @Autowired
    UserRequestsREPO userRequestsREPO;

    public List<UserRequestsTBL> fetch( UserLoginTBL user_1 , UserLoginTBL user_2, Integer request_type ) {
        return userRequestsREPO.findBySenderidAndRecevieridAndRequestype( user_1, user_2, request_type  ) ;
    }

    public void add(UserLoginTBL senderid, UserLoginTBL receiverid, Integer type, Integer status) {
        
        UserRequestsTBL record = new UserRequestsTBL( );
        record.setSenderid( senderid );
        record.setReceiverid( receiverid );
        record.setRequesttype( type );
        record.setStatus( status );
        record.setDateupdated( LocalDateTime.now( ) );
        userRequestsREPO.save( record );
    }
}
