package com.jamii.databaseconfig.controllers;

import com.jamii.databaseconfig.model.UserDataTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.databaseconfig.repo.UserDataREPO;
import com.jamii.requests.userServices.profileREQ.EditUserDataServicesREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserData
{

    /**
     * Below are the Current Statuses. "Current" refers to the most recent record of a particular user
     *
     * 2025-12-16 - This status is no longer needed.
     *
     */
    @Deprecated
    public static final Boolean CURRENT_STATUS_OFF = false;
    @Deprecated
    public static final Boolean CURRENT_STATUS_ON = true;

    //Creating a table object to reference when creating data for that table
    public UserDataTBL data = new UserDataTBL();
    public ArrayList<UserDataTBL> dataList = new ArrayList<>();

    @Autowired private UserDataREPO userDataREPO;

    /**
     * All updates old user profile snapshots will be added to the user data history at the time of saving
     * @param user
     */
    @Deprecated
    public void migratePreviousRecordtoUserHistory(UserLoginTBL user)
    {

        List<UserDataTBL> records = new ArrayList<>();
        for (UserDataTBL record : this.userDataREPO.findByUserloginid(user)) {
            records.add(record);
        }

        userDataREPO.saveAll(records);
    }

    public void add( UserLoginTBL user, EditUserDataServicesREQ editUserDataREQ )
    {

        if (user.getUserData() != null) {
            data = user.getUserData( );
        }else{
            data = new UserDataTBL();
        }
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
        data.setLastupdated(LocalDateTime.now());
        data.setUserloginid(user);

        userDataREPO.save(data);
    }

    public Optional<UserDataTBL> fetch(UserLoginTBL user, Boolean active)
    {
        return this.userDataREPO.findByUserloginid( user ).stream().findFirst();
    }

    public List<UserDataTBL> searchUserFirstname(String searchString)
    {
        return this.userDataREPO.findByFirstnameStartingWith(searchString);
    }

    public List<UserDataTBL> searchUserLastname(String searchString)
    {
        return this.userDataREPO.findByLastnameStartingWith(searchString);
    }

    public List<UserDataTBL> searchUserMiddlename(String searchString)
    {
        return this.userDataREPO.findByMiddlenameStartingWith(searchString);
    }

    public void save()
    {
        data = this.userDataREPO.save(data);
    }

    public void saveAll()
    {
        Iterable<UserDataTBL> datalist = this.userDataREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
