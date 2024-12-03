package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserCookiesTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.UserCookiesREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class UserCookiesCONT {

    @Autowired
    private UserCookiesREPO userCookiesREPO;

    public UserCookiesTBL add(UserLoginTBL user, DeviceInformationTBL device, String sessionKey,boolean rememberLogin ) {
        UserCookiesTBL newSession = new UserCookiesTBL( );
        newSession.setUserloginid( user );
        newSession.setDeviceinformationid( device );
        newSession.setActive( true );
        newSession.setSessionkey( sessionKey );
        newSession.setDatecreated( LocalDateTime.now( ) );
        newSession.setExpiredate( rememberLogin ? LocalDateTime.now().plusDays(30) : LocalDateTime.now().plusDays( 1 ) );
        return userCookiesREPO.save( newSession );
    }

    public Optional<UserCookiesTBL> fetch(UserLoginTBL user, DeviceInformationTBL device, String sessionKey, boolean active ){
        return userCookiesREPO.findByUserloginidAndDeviceinformationidAndSessionkeyAndActive( user, device, sessionKey, active).stream( ).findFirst( );
    }

    public void update(UserCookiesTBL cookie){
        userCookiesREPO.save( cookie );
    }

    public boolean checkIfKeyExisitsInTheDatabase(UserLoginTBL userData, DeviceInformationTBL device,String sessionkey) {
        return fetch( userData, device, sessionkey, UserCookiesTBL.ACTIVE_STATUS_ENABLED ).isEmpty();
    }
}
