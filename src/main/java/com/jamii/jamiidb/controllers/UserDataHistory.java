package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserDataHistoryTBL;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.UserDataHistoryREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class UserDataHistory
{

    // Creating instances for the object
    private UserDataHistoryTBL data = new UserDataHistoryTBL();
    private ArrayList<UserDataHistoryTBL> dataList  = new ArrayList<>();

    @Autowired private UserDataHistoryREPO userDataHistoryREPO;

    public void save()
    {
        data = this.userDataHistoryREPO.save(data);
    }

    public void saveAll()
    {
        Iterable<UserDataHistoryTBL> datalist = this.userDataHistoryREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }

    public void copyUserData(Optional<UserLoginTBL> user) {

        if( user.isEmpty() || user.get().getUserData() == null ){
            return;
        }
        copyUserData( user.get( ).getUserData( ) );
    }

    public void copyUserData( UserDataTBL user ){

        UserDataHistoryTBL data = new UserDataHistoryTBL( );

        data.setFirstname(user.getFirstname());
        data.setLastname(user.getLastname());
        data.setMiddlename(user.getMiddlename());
        data.setAddress1(user.getAddress1());
        data.setAddress2(user.getAddress2());
        data.setCity(user.getCity());
        data.setState(user.getState());
        data.setProvince(user.getProvince());
        data.setCity(user.getCity());
        data.setCountry(user.getCountry());
        data.setZipcode(user.getZipcode());
        data.setLastupdated(LocalDateTime.now());
        data.setUserdataid( user );

        userDataHistoryREPO.save(data);
    }

}
