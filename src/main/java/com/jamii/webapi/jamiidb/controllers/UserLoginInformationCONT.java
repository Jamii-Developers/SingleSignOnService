package com.jamii.webapi.jamiidb.controllers;

import com.jamii.webapi.activeDirectory.UserLoginOPS;
import com.jamii.webapi.jamiidb.model.UserLoginInformationTBL;
import com.jamii.webapi.jamiidb.repo.UserInformationLoginREPO;

import java.util.List;

public class UserLoginInformationCONT {

    private UserInformationLoginREPO userInformationLoginREPO;

    public boolean checkifLoginIsValid ( UserLoginOPS userLoginOPS ){

        List <UserLoginInformationTBL> fetchUserByEmail = userInformationLoginREPO.findByEmail( userLoginOPS.getLoginCredential( ) );
        List <UserLoginInformationTBL> fetchUserByUsername = userInformationLoginREPO.findByUsername( userLoginOPS.getLoginCredential( ) );

        if( !fetchUserByEmail.isEmpty( ) ){

        }

        return false;
    }

    public String fetchLoginData( UserLoginOPS userLoginOPS ){
        return "Test data";
    }
}
