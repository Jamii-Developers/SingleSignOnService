package com.jamii.jSocial.peer;

import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jSocial.model.UserRelationshipTBL;
import com.jamii.jSocial.repo.UserRelationshipREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service component for managing user relationship records in the database.
 * 
 * <p>This class handles jSocial relationships between jUser, including friendships and follows.
 * Relationships can have different types and statuses, allowing for flexible jSocial networking features.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Add new relationship records (friend or follow)</li>
 *     <li>Fetch relationships by jUser, type, and status</li>
 *     <li>Check if a relationship exists</li>
 *     <li>Fetch followers for a user</li>
 *     <li>Save relationship records individually or in batch</li>
 * </ul>
 */
@Component
public class UserRelationship
{

    /**
     * Relationship type constant for friend relationships.
     */
    public static final Integer TYPE_FRIEND = 1;
    
    /**
     * Relationship type constant for follow relationships.
     */
    public static final Integer TYPE_FOLLOW = 2;
    
    /**
     * Relationship status constant for inactive relationships.
     */
    public static final Integer STATUS_INACTIVE = 0;
    
    /**
     * Relationship status constant for active relationships.
     */
    public static final Integer STATUS_ACTIVE = 1;

    /**
     * Single relationship record instance for creating new entries.
     */
    public UserRelationshipTBL data;
    
    /**
     * List of relationship records for batch operations.
     */
    public ArrayList<UserRelationshipTBL> dataList;
    @Autowired private UserRelationshipREPO userRelationshipREPO;

    /**
     * Adds a new relationship record between two jUser.
     * 
     * @param sender the user initiating the relationship
     * @param receiver the user receiving the relationship request
     * @param type the type of relationship (friend or follow)
     * @param status the status of the relationship
     */
    public void add(UserLoginTBL sender, UserLoginTBL receiver, int type, int status)
    {
        data.setSenderid(sender);
        data.setReceiverid(receiver);
        data.setType(type);
        data.setStatus(status);
        data.setDateupdated(LocalDateTime.now());
        save();
    }

    /**
     * Fetches relationship records between two jUser with type and status filters.
     * 
     * @param sender the user who initiated the relationship
     * @param receiver the user receiving the relationship
     * @param type the relationship type to filter by
     * @param status the relationship status to filter by
     * @return list of relationship records matching the criteria
     */
    public List<UserRelationshipTBL> fetch(UserLoginTBL sender, UserLoginTBL receiver, int type, int status)
    {
        return userRelationshipREPO.findBySenderidAndReceiveridAndTypeAndStatus(sender, receiver, type, status);
    }

    /**
     * Checks if a relationship exists between two jUser with specific type and status.
     * 
     * @param sender the user who initiated the relationship
     * @param receiver the user receiving the relationship
     * @param type the relationship type to check for
     * @param status the relationship status to check for
     * @return {@code true} if the relationship does not exist, {@code false} if it exists
     */
    public boolean checkIfRelationShipExists(UserLoginTBL sender, UserLoginTBL receiver, int type, int status)
    {
        return userRelationshipREPO.findBySenderidAndReceiveridAndTypeAndStatus(sender, receiver, type, status).isEmpty();
    }

    /**
     * Fetches relationship records between two jUser with type filter.
     * 
     * @param sender the user who initiated the relationship
     * @param receiver the user receiving the relationship
     * @param type the relationship type to filter by
     * @return list of relationship records matching the criteria
     */
    public List<UserRelationshipTBL> fetch(UserLoginTBL sender, UserLoginTBL receiver, int type)
    {
        return userRelationshipREPO.findBySenderidAndReceiveridAndType(sender, receiver, type);
    }

    /**
     * Fetches all relationship records between two jUser.
     * 
     * @param sender the user who initiated the relationship
     * @param receiver the user receiving the relationship
     * @return list of all relationship records between the two jUser
     */
    public List<UserRelationshipTBL> fetch(UserLoginTBL sender, UserLoginTBL receiver)
    {
        return userRelationshipREPO.findBySenderidAndReceiverid(sender, receiver);
    }

    /**
     * Fetches relationship records for a user as sender or receiver with type and status filters.
     * 
     * @param sender the user to fetch relationships for (as both sender and receiver)
     * @param status the relationship status to filter by
     * @param type the relationship type to filter by
     * @return list of relationship records matching the criteria
     */
    public List<UserRelationshipTBL> fetch(UserLoginTBL sender, int status, int type)
    {
        return userRelationshipREPO.findBySenderOrReceiverAndStatusAndType(sender, sender, status, type);
    }

    /**
     * Fetches followers for a specific user.
     * 
     * <p>This method finds jUser who have a relationship with the specified user as the receiver,
     * effectively finding the user's followers.</p>
     * 
     * @param sender the user to fetch followers for
     * @param type the relationship type to filter by
     * @param status the relationship status to filter by
     * @return list of follower relationship records
     */
    public List<UserRelationshipTBL> fetchFollowers(UserLoginTBL sender, int type, int status)
    {
        return userRelationshipREPO.findByReceiveridAndTypeAndStatus(sender, type, status);
    }

    /**
     * Updates a relationship record in the database.
     * 
     * @param record the relationship record to update
     */
    public void update(UserRelationshipTBL record)
    {
        userRelationshipREPO.save(record);
    }

    /**
     * Saves the current relationship record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = this.userRelationshipREPO.save(data);
    }

    /**
     * Saves all relationship records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<UserRelationshipTBL> datalist = this.userRelationshipREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
