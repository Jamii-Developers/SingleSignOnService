package com.jamii.webapi.jamiidb.controllers;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.webapi.jamiidb.model.PasswordHashRecordsTBL;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import com.jamii.webapi.jamiidb.repo.PasswordHashRecordsREPO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PasswordHashRecordsCONT {

    @Autowired
    private PasswordHashRecordsREPO passwordHashRecordsREPO;

    public PasswordHashRecordsTBL addUserNewPasswordRecord( UserLoginTBL userLoginTBL ){

        PasswordHashRecordsTBL passwordHashRecordsTBL = new PasswordHashRecordsTBL( );
        passwordHashRecordsTBL.setPasswordsalt( userLoginTBL.getPasswordsalt( ) );
        passwordHashRecordsTBL.setFK_USER_LOGIN_DATA( userLoginTBL );
        passwordHashRecordsTBL.setDateadded( LocalDateTime.now( ) );

        return passwordHashRecordsREPO.save( passwordHashRecordsTBL );

    }

    public Boolean isPasswordInLastTenRecords( UserLoginTBL userLoginTBL ){

        List< PasswordHashRecordsTBL > passwordHashRecords = passwordHashRecordsREPO.findLast10Passwords( userLoginTBL.getId( ) );
        for( PasswordHashRecordsTBL passwordHashRecord : passwordHashRecords ){
            if( JamiiStringUtils.equals( passwordHashRecord.getPasswordsalt( ), userLoginTBL.getPasswordsalt( ) ) ){
                return true;
            }
        }

        return false;
    }

}
