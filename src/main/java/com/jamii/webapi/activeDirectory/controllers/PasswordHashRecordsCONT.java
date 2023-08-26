package com.jamii.webapi.activeDirectory.controllers;

import com.jamii.webapi.jamiidb.model.PasswordHashRecordsTBL;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import com.jamii.webapi.jamiidb.repo.PasswordHashRecordsREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

}
