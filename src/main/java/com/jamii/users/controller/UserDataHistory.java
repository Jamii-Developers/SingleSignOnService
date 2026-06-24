package com.jamii.users.controller;

import com.jamii.users.model.UserDataHistoryTBL;
import com.jamii.users.model.UserDataTBL;
import com.jamii.users.model.UserLoginTBL;
import com.jamii.users.repo.UserDataHistoryREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Service component for managing user data history records in the database.
 * 
 * <p>This class maintains an archive of user profile data snapshots, allowing
 * the application to track changes to user profiles over time. When a user's
 * profile is updated, the old data is copied to the history table.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Copy user profile data to history</li>
 *     <li>Save history records individually or in batch</li>
 * </ul>
 */
@Component
public class UserDataHistory
{

    /**
     * Single user data history record instance for creating new entries.
     */
    private UserDataHistoryTBL data ;
    
    /**
     * List of user data history records for batch operations.
     */
    private ArrayList<UserDataHistoryTBL> dataList ;

    @Autowired private UserDataHistoryREPO userDataHistoryREPO;

    /**
     * Saves the current user data history record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = this.userDataHistoryREPO.save(data);
    }

    /**
     * Saves all user data history records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<UserDataHistoryTBL> datalist = this.userDataHistoryREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }

    /**
     * Copies a user's current profile data to the history table.
     * 
     * <p>This method checks if the user exists and has profile data before copying.
     * If the user is empty or has no profile data, the method returns without doing anything.</p>
     * 
     * @param user an Optional containing the user whose profile data should be copied
     */
    public void copyUserData(Optional<UserLoginTBL> user) {

        if( user.isEmpty() || user.get().getUserData() == null ){
            return;
        }
        copyUserData( user.get( ).getUserData( ) );
    }

    /**
     * Copies user profile data to the history table.
     * 
     * <p>This method creates a new history record with all the profile fields
     * from the provided user data, including a timestamp for when the snapshot was taken.</p>
     * 
     * @param user the user profile data to copy to history
     */
    public void copyUserData( UserDataTBL user ){

        UserDataHistoryTBL data = new UserDataHistoryTBL( );

        data.setFirstname(user.getFirstname());
        data.setLastname(user.getLastname());
        data.setMiddlename(user.getMiddlename());
        data.setAddress1(user.getAddress1());
        data.setAddress2(user.getAddress2());
        data.setCity(user.getCity());
        data.setState(user.getState());
        data.setProvince(user.getProvince());
        data.setCity(user.getCity());
        data.setCountry(user.getCountry());
        data.setZipcode(user.getZipcode());
        data.setLastupdated(LocalDateTime.now());
        data.setUserdataid( user );

        userDataHistoryREPO.save(data);
    }

}
