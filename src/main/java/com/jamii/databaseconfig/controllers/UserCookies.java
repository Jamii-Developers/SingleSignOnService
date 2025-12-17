package com.jamii.databaseconfig.controllers;

import com.jamii.databaseconfig.model.DeviceInformationTBL;
import com.jamii.databaseconfig.model.UserCookiesTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.databaseconfig.repo.UserCookiesREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class UserCookies
{

    //ACTIVE STATUS
    public static boolean ACTIVE_STATUS_ENABLED = true;
    public static boolean ACTIVE_STATUS_DISABLED = false;

    //Creating a table object to reference when creating data for that table
    public UserCookiesTBL data = new UserCookiesTBL();
    public ArrayList<UserCookiesTBL> dataList = new ArrayList<>();
    @Autowired private UserCookiesREPO userCookiesREPO;

    public UserCookiesTBL add(UserLoginTBL user, DeviceInformationTBL device, String sessionKey, boolean rememberLogin)
    {
        UserCookiesTBL newSession = new UserCookiesTBL();
        newSession.setUserloginid(user);
        newSession.setDeviceinformationid(device);
        newSession.setActive(true);
        newSession.setSessionkey(sessionKey);
        newSession.setDatecreated(LocalDateTime.now());
        newSession.setExpiredate(rememberLogin ? LocalDateTime.now().plusDays(30) : LocalDateTime.now().plusDays(1));
        return userCookiesREPO.save(newSession);
    }

    public Optional<UserCookiesTBL> fetch(UserLoginTBL user, DeviceInformationTBL device, String sessionKey, boolean active)
    {
        return userCookiesREPO.findByUserloginidAndDeviceinformationidAndSessionkeyAndActive(user, device, sessionKey, active).stream().findFirst();
    }

    /**
     * Use the function save( );
     *
     * @param cookie
     */
    @Deprecated
    public void update(UserCookiesTBL cookie)
    {
        userCookiesREPO.save(cookie);
    }

    public boolean checkIfKeyExisitsInTheDatabase(UserLoginTBL userData, DeviceInformationTBL device, String sessionkey)
    {
        return fetch(userData, device, sessionkey, ACTIVE_STATUS_ENABLED).isEmpty();
    }

    public void save()
    {
        data = this.userCookiesREPO.save(data);
    }

    public void saveAll()
    {
        Iterable<UserCookiesTBL> datalist = this.userCookiesREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
