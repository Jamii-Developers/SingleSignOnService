package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.UserBlockListREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserBlockList
{

    // STATUS
    public static final Integer STATUS_INACTIVE = 0;
    public static final Integer STATUS_ACTIVE = 1;

    //Creating a table object to reference when creating data for that table
    public UserBlockListTBL data = new UserBlockListTBL();
    public ArrayList<UserBlockListTBL> dataList = new ArrayList<>();
    @Autowired UserBlockListREPO userBlockListREPO;

    public List<UserBlockListTBL> fetch(UserLoginTBL userid, UserLoginTBL blockedid, Integer status)
    {
        return userBlockListREPO.findByUseridAndBlockedidAndStatus(userid, blockedid, status);
    }

    public List<UserBlockListTBL> fetch(UserLoginTBL userid, UserLoginTBL blockedid)
    {
        return userBlockListREPO.findByUseridAndBlockedid(userid, blockedid);
    }

    public List<UserBlockListTBL> fetchBlockedList(UserLoginTBL user, Integer status)
    {
        return userBlockListREPO.findByUseridAndStatus(user, status);
    }

    /**
     * Use the save ( ) function
     *
     * @param blockListTBL
     */
    @Deprecated
    public void update(UserBlockListTBL blockListTBL)
    {
        userBlockListREPO.save(blockListTBL);
    }

    public void add(UserLoginTBL userid, UserLoginTBL blockedid, Integer status)
    {
        data.setUserid(userid);
        data.setBlockedid(blockedid);
        data.setStatus(status);
        data.setDateupdated(LocalDateTime.now());
        save();
    }

    public void save()
    {
        this.userBlockListREPO.save(data);
    }

    public void saveAll()
    {
        Iterable<UserBlockListTBL> datalist = this.userBlockListREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
