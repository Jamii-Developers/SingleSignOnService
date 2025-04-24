package com.jamii.jamiidb.controllers;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.model.PasswordHashRecordsTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.PasswordHashRecordsREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class PasswordHashRecords {

    @Autowired
    private PasswordHashRecordsREPO passwordHashRecordsREPO;

    public PasswordHashRecordsTBL data = new PasswordHashRecordsTBL( );
    public ArrayList < PasswordHashRecordsTBL > dataList = new ArrayList< >( );

    public PasswordHashRecordsTBL addUserNewPasswordRecord(UserLoginTBL userLoginTBL) {

        data.setPasswordsalt( userLoginTBL.getPasswordsalt( ) );
        data.setUserloginid( userLoginTBL);
        data.setDateadded( LocalDateTime.now( ) );

        return passwordHashRecordsREPO.save( data );

    }

    public Boolean isPasswordInLastTenRecords(UserLoginTBL userLoginTBL) {

        List<PasswordHashRecordsTBL> passwordHashRecords = passwordHashRecordsREPO.findLast10Passwords(userLoginTBL.getId());
        for (PasswordHashRecordsTBL passwordHashRecord : passwordHashRecords) {
            if (JamiiStringUtils.equals(passwordHashRecord.getPasswordsalt(), userLoginTBL.getPasswordsalt())) {
                return true;
            }
        }

        return false;
    }

    public void save( ){
        data = this.passwordHashRecordsREPO.save( data );
    }

    public void saveAll( ){
        Iterable< PasswordHashRecordsTBL > datalist = this.passwordHashRecordsREPO.saveAll( dataList ) ;
        dataList.clear( );
        datalist.forEach( x -> dataList.add( x ) );
    }

}
