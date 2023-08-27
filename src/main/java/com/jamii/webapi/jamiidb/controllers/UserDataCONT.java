package com.jamii.webapi.jamiidb.controllers;

import com.jamii.requests.EditUserDataREQ;
import com.jamii.webapi.jamiidb.model.UserDataTBL;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import com.jamii.webapi.jamiidb.repo.UserDataREPO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDataCONT {

    @Autowired
    private UserDataREPO userDataREPO;

    public void markAllPreviousUserDataInActive( UserLoginTBL user ){

        List<UserDataTBL> records = new ArrayList<>( );
        for( UserDataTBL record : userDataREPO.findbyuserloginidandactive( user.getId( ), UserDataTBL.CURRENT_STATUS_ON ) ) {
            record.setCurrent( UserDataTBL.CURRENT_STATUS_OFF );
            records.add( record );
        }
        userDataREPO.saveAll( records );
    }

    public void addUserData(UserLoginTBL user, EditUserDataREQ editUserDataREQ) {

        UserDataTBL newUserData = new UserDataTBL( );
        newUserData.setFirstname( editUserDataREQ.getFirstname( ) );
        newUserData.setLastname( editUserDataREQ.getLastname( ) );
        newUserData.setMiddlename( editUserDataREQ.getMiddlename( ) );
        newUserData.setAddress1( editUserDataREQ.getAddress1( ) );
        newUserData.setAddress2( editUserDataREQ.getAddress2( ) );
        newUserData.setCity( editUserDataREQ.getCity( ) );
        newUserData.setState( editUserDataREQ.getState( ) );
        newUserData.setProvince( editUserDataREQ.getProvince( ) );
        newUserData.setCity( editUserDataREQ.getCity( ) );
        newUserData.setCountry( editUserDataREQ.getCountry( ) );
        newUserData.setZipcode( editUserDataREQ.getZipcode( ) );
        newUserData.setCurrent( UserDataTBL.CURRENT_STATUS_ON );
        newUserData.setLastupdated( LocalDateTime.now( ) );
        newUserData.setFK_USER_LOGIN_DATA( user );

        userDataREPO.save( newUserData );
    }
}
