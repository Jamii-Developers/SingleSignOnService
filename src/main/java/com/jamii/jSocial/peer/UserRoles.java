package com.jamii.jSocial.peer;

import com.jamii.jSocial.model.UserRolesTBL;
import com.jamii.jSocial.repo.UserRolesREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Service component for managing user role records in the database.
 * 
 * <p>This class handles the storage and retrieval of user role information,
 * allowing jUser to be assigned specific roles for access control and authorization purposes.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Save user role records individually or in batch</li>
 * </ul>
 */
@Component
public class UserRoles
{

    /**
     * Single user role record instance for creating new entries.
     */
    public UserRolesTBL data;
    
    /**
     * List of user role records for batch operations.
     */
    public ArrayList<UserRolesTBL> dataList ;

    @Autowired private UserRolesREPO userRolesREPO;

    /**
     * Saves the current user role record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = this.userRolesREPO.save(data);
    }

    /**
     * Saves all user role records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<UserRolesTBL> datalist = this.userRolesREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
