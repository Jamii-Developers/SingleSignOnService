package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.UserDataREPO;
import com.jamii.requests.activeDirectory.EditUserDataREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDataCONT {

    @Autowired
    private UserDataREPO userDataREPO;

    public void markAllPreviousUserDataInActive( UserLoginTBL user ){
        List<UserDataTBL> records = new ArrayList<>( );
        for( UserDataTBL record : this.userDataREPO.findByUserloginidAndCurrent( user , UserDataTBL.CURRENT_STATUS_ON ) ) {
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
        newUserData.setUserloginid( user );

        userDataREPO.save( newUserData );
    }

    public Optional< UserDataTBL > fetch( UserLoginTBL user, Boolean active ){
        return this.userDataREPO.findByUserloginidAndCurrent( user, active ).stream( ).findFirst( );
    }

    public List<UserDataTBL> searchUserFirstname(String searchString) {
        return this.userDataREPO.findByFirstnameStartingWithAndCurrent( searchString, UserDataTBL.CURRENT_STATUS_ON );
    }

    public List<UserDataTBL> searchUserLastname(String searchString) {
        return this.userDataREPO.findByLastnameStartingWithAndCurrent( searchString, UserDataTBL.CURRENT_STATUS_ON );
    }

    public List<UserDataTBL> searchUserMiddlename(String searchString) {
        return this.userDataREPO.findByMiddlenameStartingWithAndCurrent( searchString, UserDataTBL.CURRENT_STATUS_ON );
    }
}
