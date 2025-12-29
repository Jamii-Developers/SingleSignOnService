package com.jamii.databaseconfig.controllers;

import com.jamii.databaseconfig.model.PasswordHashRecordsHistoryTBL;
import com.jamii.databaseconfig.model.PasswordHashRecordsTBL;
import com.jamii.databaseconfig.repo.PasswordHashRecordsHistoryREPO;
import com.jamii.databaseconfig.repo.PasswordHashRecordsREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PasswordHashRecordsHistory {

    public PasswordHashRecordsHistoryTBL data = new PasswordHashRecordsHistoryTBL();
    public ArrayList<PasswordHashRecordsHistoryTBL> dataList = new ArrayList<>();

    @Autowired private PasswordHashRecordsHistoryREPO passwordHashRecordsHistoryREPO;

    public void add( PasswordHashRecords record ){

    }

    public void checkIfExists( PasswordHashRecords record ){

    }

    public boolean checkIfExistsFromLastTen( PasswordHashRecords record ){
        //TO-DO Add the fetch functionality for this code
        return false;
    }

    public void save( ){

    }

    public void saveall( ){

    }
}
