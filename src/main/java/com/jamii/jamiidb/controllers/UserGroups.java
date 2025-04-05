package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserGroupsTBL;
import com.jamii.jamiidb.repo.UserGroupsREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserGroups {

    @Autowired
    private UserGroupsREPO userGroupsREPO;

    //Creating a table object to reference when creating data for that table
    public UserGroupsTBL data = new UserGroupsTBL( );
    public ArrayList< UserGroupsTBL > dataList = new ArrayList< >( );

    public void save( ){
        data = this.userGroupsREPO.save( data );
    }

    public void saveAll( ){
        Iterable< UserGroupsTBL > datalist = this.userGroupsREPO.saveAll( dataList ) ;
        dataList.clear( );
        datalist.forEach( x -> dataList.add( x ) );
    }
}
