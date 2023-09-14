package com.jamii.jamiidb.controllers;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.model.PasswordHashRecordsTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.PasswordHashRecordsREPO;
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
        passwordHashRecordsTBL.setUserloginid( userLoginTBL );
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
