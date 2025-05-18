package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.UserDataREPO;
import com.jamii.requests.userServices.profileREQ.EditUserDataServicesREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserData {

    @Autowired
    private UserDataREPO userDataREPO;

    //Creating a table object to reference when creating data for that table
    public UserDataTBL data = new UserDataTBL( );
    public ArrayList< UserDataTBL > dataList = new ArrayList<>( );

    /**
     * Below are the Current Statuses. "Current" reffers to the most recent record of a particular user
     */
    public static final Boolean CURRENT_STATUS_OFF = false;
    public static final Boolean CURRENT_STATUS_ON = true;

    public void markAllPreviousUserDataInActive(UserLoginTBL user) {
        List<UserDataTBL> records = new ArrayList<>();
        for (UserDataTBL record : this.userDataREPO.findByUserloginidAndCurrent(user, CURRENT_STATUS_ON)) {
            record.setCurrent( CURRENT_STATUS_OFF );
            records.add(record);
        }
        userDataREPO.saveAll(records);
    }

    public void add( UserLoginTBL user, EditUserDataServicesREQ editUserDataREQ ) {

        data = new UserDataTBL( );
        data.setFirstname(editUserDataREQ.getFirstname());
        data.setLastname(editUserDataREQ.getLastname());
        data.setMiddlename(editUserDataREQ.getMiddlename());
        data.setAddress1(editUserDataREQ.getAddress1());
        data.setAddress2(editUserDataREQ.getAddress2());
        data.setCity(editUserDataREQ.getCity());
        data.setState(editUserDataREQ.getState());
        data.setProvince(editUserDataREQ.getProvince());
        data.setCity(editUserDataREQ.getCity());
        data.setCountry(editUserDataREQ.getCountry());
        data.setZipcode(editUserDataREQ.getZipcode());
        data.setCurrent(CURRENT_STATUS_ON);
        data.setLastupdated(LocalDateTime.now());
        data.setUserloginid(user);

        userDataREPO.save(data);
    }

    public Optional<UserDataTBL> fetch(UserLoginTBL user, Boolean active) {
        return this.userDataREPO.findByUserloginidAndCurrent(user, active).stream().findFirst();
    }

    public List<UserDataTBL> searchUserFirstname(String searchString) {
        return this.userDataREPO.findByFirstnameStartingWithAndCurrent(searchString, CURRENT_STATUS_ON);
    }

    public List<UserDataTBL> searchUserLastname(String searchString) {
        return this.userDataREPO.findByLastnameStartingWithAndCurrent(searchString, CURRENT_STATUS_ON);
    }

    public List<UserDataTBL> searchUserMiddlename(String searchString) {
        return this.userDataREPO.findByMiddlenameStartingWithAndCurrent(searchString, CURRENT_STATUS_ON);
    }

    public void save( ){
        data = this.userDataREPO.save( data );
    }

    public void saveAll( ){
        Iterable<UserDataTBL> datalist = this.userDataREPO.saveAll( dataList ) ;
        dataList.clear( );
        datalist.forEach( x -> dataList.add( x ) );
    }
}
