package com.jamii.databaseconfig.controllers;

import com.jamii.databaseconfig.model.UserGroupsTBL;
import com.jamii.databaseconfig.repo.UserGroupsREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Service component for managing user group records in the database.
 * 
 * <p>This class handles the storage and retrieval of user group information,
 * allowing users to be organized into groups for access control and categorization.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Save user group records individually or in batch</li>
 * </ul>
 */
@Component
public class UserGroups
{

    /**
     * Single user group record instance for creating new entries.
     */
    public UserGroupsTBL data = new UserGroupsTBL();
    
    /**
     * List of user group records for batch operations.
     */
    public ArrayList<UserGroupsTBL> dataList = new ArrayList<>();

    @Autowired private UserGroupsREPO userGroupsREPO;

    /**
     * Saves the current user group record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = this.userGroupsREPO.save(data);
    }

    /**
     * Saves all user group records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<UserGroupsTBL> datalist = this.userGroupsREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
