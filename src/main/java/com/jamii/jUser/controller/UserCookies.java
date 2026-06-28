package com.jamii.jUser.controller;

import com.jamii.jUser.model.DeviceInformationTBL;
import com.jamii.jUser.model.UserCookiesTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jUser.repo.UserCookiesREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Service component for managing user cookie/session records in the database.
 * 
 * <p>This class handles user session management through cookies, tracking login sessions
 * across different devices with support for "remember me" functionality.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Add new session cookies for jUser</li>
 *     <li>Fetch session cookies by user, device, and session key</li>
 *     <li>Check if session key exists in database</li>
 *     <li>Save cookie records individually or in batch</li>
 * </ul>
 */
@Component
public class UserCookies
{

    /**
     * Cookie active status constant for enabled sessions.
     */
    public static boolean ACTIVE_STATUS_ENABLED = true;
    
    /**
     * Cookie active status constant for disabled sessions.
     */
    public static boolean ACTIVE_STATUS_DISABLED = false;

    /**
     * Single cookie record instance for creating new entries.
     */
    public UserCookiesTBL data ;
    
    /**
     * List of cookie records for batch operations.
     */
    public ArrayList<UserCookiesTBL> dataList ;
    @Autowired private UserCookiesREPO userCookiesREPO;

    /**
     * Adds a new session cookie for a user on a specific device.
     * 
     * <p>The session expiration is set based on the rememberLogin parameter:
     * 30 days if rememberLogin is true, 1 day otherwise.</p>
     * 
     * @param user the user to create the session for
     * @param device the device the session is created on
     * @param sessionKey the session key for the cookie
     * @param rememberLogin whether to remember the login for 30 days
     * @return the saved cookie record with generated ID
     */
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

    /**
     * Fetches a session cookie by user, device, session key, and active status.
     * 
     * @param user the user to fetch the cookie for
     * @param device the device to fetch the cookie for
     * @param sessionKey the session key to fetch
     * @param active the active status to filter by
     * @return an Optional containing the cookie record if found, empty otherwise
     */
    public Optional<UserCookiesTBL> fetch(UserLoginTBL user, DeviceInformationTBL device, String sessionKey, boolean active)
    {
        return userCookiesREPO.findByUserloginidAndDeviceinformationidAndSessionkeyAndActive(user, device, sessionKey, active).stream().findFirst();
    }

    /**
     * Updates a cookie record in the database.
     * 
     * @param cookie the cookie record to update
     * @deprecated Use {@link #save()} instead which operates on the {@code data} field
     */
    @Deprecated
    public void update(UserCookiesTBL cookie)
    {
        userCookiesREPO.save(cookie);
    }

    /**
     * Checks if a session key exists in the database for a specific user and device.
     * 
     * @param userData the user to check against
     * @param device the device to check against
     * @param sessionkey the session key to check
     * @return {@code true} if the session key does not exist, {@code false} if it exists
     */
    public boolean checkIfKeyExisitsInTheDatabase(UserLoginTBL userData, DeviceInformationTBL device, String sessionkey)
    {
        return fetch(userData, device, sessionkey, ACTIVE_STATUS_ENABLED).isEmpty();
    }

    /**
     * Saves the current cookie record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = this.userCookiesREPO.save(data);
    }

    public void save( UserCookiesTBL data )
    {
        this.userCookiesREPO.save(data);
    }


    /**
     * Saves all cookie records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<UserCookiesTBL> datalist = this.userCookiesREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
