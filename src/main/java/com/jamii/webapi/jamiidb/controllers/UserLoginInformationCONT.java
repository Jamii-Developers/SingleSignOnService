package com.jamii.webapi.jamiidb.controllers;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.webapi.activeDirectory.UserLoginOPS;
import com.jamii.webapi.jamiidb.model.UserLoginInformationTBL;
import com.jamii.webapi.jamiidb.repo.UserInformationLoginREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserLoginInformationCONT {

    @Autowired
    private UserInformationLoginREPO userInformationLoginREPO;

    public UserLoginInformationTBL checkAndRetrieveValidLogin ( UserLoginOPS userLoginOPS ){

        // Adding this so we are able to check only users that are active
        List <UserLoginInformationTBL> fetchCredential = new ArrayList< > ( );

        fetchCredential.addAll( userInformationLoginREPO.findByUsernameAndActive( userLoginOPS.getLoginCredential( ),  userLoginOPS.getActiveStatus( ) ) );
        fetchCredential.addAll( userInformationLoginREPO.findByEmailaddressAndActive( userLoginOPS.getLoginCredential( ),  userLoginOPS.getActiveStatus( ) ) );

        if( fetchCredential.isEmpty( )  ){
            return null;
        }


        for( UserLoginInformationTBL cred : fetchCredential ){
            if(JamiiStringUtils.equals( cred.getPasswordsalt( ), userLoginOPS.getLoginPassword( ) ) ) {
                return cred;
            }
        }

        return null ;
    }
}
