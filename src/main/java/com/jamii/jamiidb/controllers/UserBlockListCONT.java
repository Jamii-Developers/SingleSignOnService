package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.UserBlockListREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserBlockListCONT {

    @Autowired
    UserBlockListREPO userBlockListREPO;

    public List<UserBlockListTBL> fetch(UserLoginTBL userid, UserLoginTBL blockedid, Integer status) {
        return userBlockListREPO.findByUseridAndBlockedidAndStatus( userid, blockedid, status );
    }

    public List<UserBlockListTBL> fetch(UserLoginTBL userid, UserLoginTBL blockedid ) {
        return userBlockListREPO.findByUseridAndBlockedid( userid, blockedid );
    }

    public List< UserBlockListTBL > fetchBlockedList( UserLoginTBL user , Integer status ){
        return userBlockListREPO.findByUseridAndStatus( user, status );
    }

    public void update( UserBlockListTBL blockListTBL ){
        userBlockListREPO.save( blockListTBL );
    }

    public void add( UserLoginTBL userid, UserLoginTBL blockedid, Integer status ){

        UserBlockListTBL record = new UserBlockListTBL( );
        record.setUserid( userid );
        record.setBlockedid( blockedid );
        record.setStatus( status );
        record.setDateupdated( LocalDateTime.now( ) );
        userBlockListREPO.save( record );
    }
}
