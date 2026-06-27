package com.jamii.jSocial.controllers;

import com.jamii.jSocial.model.UserBlockListTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jSocial.repo.UserBlockListREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service component for managing user block list records in the database.
 * 
 * <p>This class handles user blocking functionality, allowing jUser to block other jUser
 * from interacting with them. Blocked relationships can be tracked with different statuses.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Fetch block list records by jUser and status</li>
 *     <li>Add new block list entries</li>
 *     <li>Fetch blocked user lists for a specific user</li>
 *     <li>Save block list records individually or in batch</li>
 * </ul>
 */
@Component
public class UserBlockList
{

    /**
     * Block status constant for inactive blocks.
     */
    public static final Integer STATUS_INACTIVE = 0;
    
    /**
     * Block status constant for active blocks.
     */
    public static final Integer STATUS_ACTIVE = 1;

    /**
     * Single block list record instance for creating new entries.
     */
    public UserBlockListTBL data;
    
    /**
     * List of block list records for batch operations.
     */
    public ArrayList<UserBlockListTBL> dataList;
    @Autowired UserBlockListREPO userBlockListREPO;

    /**
     * Fetches block list records for a specific user-blocked user pair with status filter.
     * 
     * @param userid the user who blocked another user
     * @param blockedid the user who was blocked
     * @param status the status to filter by
     * @return list of block list records matching the criteria
     */
    public List<UserBlockListTBL> fetch(UserLoginTBL userid, UserLoginTBL blockedid, Integer status)
    {
        return userBlockListREPO.findByUseridAndBlockedidAndStatus(userid, blockedid, status);
    }

    /**
     * Fetches all block list records for a specific user-blocked user pair.
     * 
     * @param userid the user who blocked another user
     * @param blockedid the user who was blocked
     * @return list of all block list records for the user pair
     */
    public List<UserBlockListTBL> fetch(UserLoginTBL userid, UserLoginTBL blockedid)
    {
        return userBlockListREPO.findByUseridAndBlockedid(userid, blockedid);
    }

    /**
     * Fetches all blocked jUser for a specific user with status filter.
     * 
     * @param user the user to fetch the block list for
     * @param status the status to filter by
     * @return list of blocked jUser matching the criteria
     */
    public List<UserBlockListTBL> fetchBlockedList(UserLoginTBL user, Integer status)
    {
        return userBlockListREPO.findByUseridAndStatus(user, status);
    }

    /**
     * Updates a block list record in the database.
     * 
     * @param blockListTBL the block list record to update
     * @deprecated Use {@link #save()} instead which operates on the {@code data} field
     */
    @Deprecated
    public void update(UserBlockListTBL blockListTBL)
    {
        userBlockListREPO.save(blockListTBL);
    }

    /**
     * Adds a new block list entry.
     * 
     * @param userid the user who is blocking another user
     * @param blockedid the user being blocked
     * @param status the status of the block
     */
    public void add(UserLoginTBL userid, UserLoginTBL blockedid, Integer status)
    {
        data.setUserid(userid);
        data.setBlockedid(blockedid);
        data.setStatus(status);
        data.setDateupdated(LocalDateTime.now());
        save();
    }

    /**
     * Saves the current block list record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        this.userBlockListREPO.save(data);
    }

    /**
     * Saves all block list records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<UserBlockListTBL> datalist = this.userBlockListREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
