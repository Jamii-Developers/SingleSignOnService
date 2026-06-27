package com.jamii.jUser.controller;

import com.jamii.jUser.model.UserDataTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jUser.repo.UserDataREPO;
import com.jamii.jUser.requests.EditUserDataServicesREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service component for managing user data records in the database.
 * 
 * <p>This class handles user profile information including name, address, and contact details.
 * It supports profile updates and maintains search functionality for finding jUser by name.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Add/update user profile data</li>
 *     <li>Fetch user profile information</li>
 *     <li>Search jUser by first name, last name, or middle name</li>
 *     <li>Save user data records individually or in batch</li>
 * </ul>
 */
@Component
public class UserData
{

    /**
     * Current status constant for non-current records.
     * @deprecated This status is no longer needed as of 2025-12-16.
     */
    @Deprecated
    public static final Boolean CURRENT_STATUS_OFF = false;
    
    /**
     * Current status constant for current records.
     * @deprecated This status is no longer needed as of 2025-12-16.
     */
    @Deprecated
    public static final Boolean CURRENT_STATUS_ON = true;

    /**
     * Single user data record instance for creating new entries.
     */
    public UserDataTBL data ;
    
    /**
     * List of user data records for batch operations.
     */
    public ArrayList<UserDataTBL> dataList ;

    @Autowired private UserDataREPO userDataREPO;

    /**
     * Migrates previous user profile snapshots to the user data history.
     * 
     * <p>This method was used to archive old profile records before updates.
     * It is no longer needed as the history migration is handled differently.</p>
     * 
     * @param user the user whose records should be migrated
     * @deprecated This method is no longer needed
     */
    @Deprecated
    public void migratePreviousRecordtoUserHistory(UserLoginTBL user)
    {

        List<UserDataTBL> records = new ArrayList<>();
        for (UserDataTBL record : this.userDataREPO.findByUserloginid(user)) {
            records.add(record);
        }

        userDataREPO.saveAll(records);
    }

    /**
     * Adds or updates user profile data from a request object.
     * 
     * <p>This method populates the user's profile information from the request
     * and saves it to the database. If the user already has profile data, it updates
     * the existing record; otherwise, it creates a new one.</p>
     * 
     * @param user the user to add/update profile data for
     * @param editUserDataREQ the request object containing profile data
     */
    public void add( UserLoginTBL user, EditUserDataServicesREQ editUserDataREQ )
    {

        if (user.getUserData() != null) {
            data = user.getUserData( );
        }else{
            data = new UserDataTBL();
        }
        data.setFirstname(editUserDataREQ.getFirstname());
        data.setLastname(editUserDataREQ.getLastname());
        data.setMiddlename(editUserDataREQ.getMiddlename());
        data.setAddress1(editUserDataREQ.getAddress1());
        data.setAddress2(editUserDataREQ.getAddress2());
        data.setCity(editUserDataREQ.getCity());
        data.setState(editUserDataREQ.getState());
        data.setProvince(editUserDataREQ.getProvince());
        data.setCity(editUserDataREQ.getCity());
        data.setCountry(editUserDataREQ.getCountry());
        data.setZipcode(editUserDataREQ.getZipcode());
        data.setLastupdated(LocalDateTime.now());
        data.setUserloginid(user);

        userDataREPO.save(data);
    }

    /**
     * Fetches user profile data for a specific user.
     * 
     * @param user the user to fetch profile data for
     * @param active the active status to filter by (currently unused)
     * @return an Optional containing the user profile data if found, empty otherwise
     */
    public Optional<UserDataTBL> fetch(UserLoginTBL user, Boolean active)
    {
        return this.userDataREPO.findByUserloginid( user ).stream().findFirst();
    }

    /**
     * Searches for jUser by first name prefix.
     * 
     * @param searchString the prefix to search for in first names
     * @return list of jUser whose first name starts with the search string
     */
    public List<UserDataTBL> searchUserFirstname(String searchString)
    {
        return this.userDataREPO.findByFirstnameStartingWith(searchString);
    }

    /**
     * Searches for jUser by last name prefix.
     * 
     * @param searchString the prefix to search for in last names
     * @return list of jUser whose last name starts with the search string
     */
    public List<UserDataTBL> searchUserLastname(String searchString)
    {
        return this.userDataREPO.findByLastnameStartingWith(searchString);
    }

    /**
     * Searches for jUser by middle name prefix.
     * 
     * @param searchString the prefix to search for in middle names
     * @return list of jUser whose middle name starts with the search string
     */
    public List<UserDataTBL> searchUserMiddlename(String searchString)
    {
        return this.userDataREPO.findByMiddlenameStartingWith(searchString);
    }

    /**
     * Saves the current user data record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = this.userDataREPO.save(data);
    }

    /**
     * Saves all user data records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<UserDataTBL> datalist = this.userDataREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
