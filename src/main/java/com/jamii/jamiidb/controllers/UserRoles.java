package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserRolesTBL;
import com.jamii.jamiidb.repo.UserRolesREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserRoles
{

    //Creating a table object to reference when creating data for that table
    public UserRolesTBL data = new UserRolesTBL();
    public ArrayList<UserRolesTBL> dataList = new ArrayList<>();

    @Autowired private UserRolesREPO userRolesREPO;

    public void save()
    {
        data = this.userRolesREPO.save(data);
    }

    public void saveAll()
    {
        Iterable<UserRolesTBL> datalist = this.userRolesREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
