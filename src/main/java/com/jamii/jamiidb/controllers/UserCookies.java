package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserCookiesTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.UserCookiesREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class UserCookies {

    @Autowired
    private UserCookiesREPO userCookiesREPO;

    //Creating a table object to reference when creating data for that table
    public UserCookiesTBL data = new UserCookiesTBL( );
    public ArrayList< UserCookiesTBL > dataList = new ArrayList< >( );

    //ACTIVE STATUS
    public static boolean ACTIVE_STATUS_ENABLED = true;
    public static boolean ACTIVE_STATUS_DISABLED = false;

    public UserCookiesTBL add(UserLoginTBL user, DeviceInformationTBL device, String sessionKey, boolean rememberLogin) {
        UserCookiesTBL newSession = new UserCookiesTBL();
        newSession.setUserloginid(user);
        newSession.setDeviceinformationid(device);
        newSession.setActive(true);
        newSession.setSessionkey(sessionKey);
        newSession.setDatecreated(LocalDateTime.now());
        newSession.setExpiredate(rememberLogin ? LocalDateTime.now().plusDays(30) : LocalDateTime.now().plusDays(1));
        return userCookiesREPO.save(newSession);
    }

    public Optional<UserCookiesTBL> fetch(UserLoginTBL user, DeviceInformationTBL device, String sessionKey, boolean active) {
        return userCookiesREPO.findByUserloginidAndDeviceinformationidAndSessionkeyAndActive(user, device, sessionKey, active).stream().findFirst();
    }

    /**
     * Use the function save( );
     * @param cookie
     */
    @Deprecated
    public void update(UserCookiesTBL cookie) {
        userCookiesREPO.save(cookie);
    }

    public boolean checkIfKeyExisitsInTheDatabase(UserLoginTBL userData, DeviceInformationTBL device, String sessionkey) {
        return fetch(userData, device, sessionkey, ACTIVE_STATUS_ENABLED).isEmpty();
    }

    public void save( ){
        data = this.userCookiesREPO.save( data );
    }

    public void saveAll( ){
        Iterable< UserCookiesTBL > datalist = this.userCookiesREPO.saveAll( dataList ) ;
        dataList.clear( );
        datalist.forEach( x -> dataList.add( x ) );
    }


}
