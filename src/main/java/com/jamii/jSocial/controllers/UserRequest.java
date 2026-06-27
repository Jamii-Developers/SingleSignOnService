package com.jamii.jSocial.controllers;

import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jSocial.model.UserRequestsTBL;
import com.jamii.jSocial.repo.UserRequestsREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service component for managing user request records in the database.
 * 
 * <p>This class handles pending relationship requests between jUser, such as friend requests
 * or follow requests. Requests can be tracked with different types and statuses before being
 * converted into actual relationships.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Add new relationship requests</li>
 *     <li>Fetch requests by jUser, type, and status</li>
 *     <li>Fetch pending requests for a user</li>
 *     <li>Save request records individually or in batch</li>
 * </ul>
 */
@Component
public class UserRequest
{

    /**
     * Request type constant for friend requests.
     */
    public static final Integer TYPE_FRIEND = 1;
    
    /**
     * Request type constant for follow requests.
     */
    public static final Integer TYPE_FOLLOW = 2;

    /**
     * Request status constant for inactive requests.
     */
    public static final Integer STATUS_INACTIVE = 0;
    
    /**
     * Request status constant for active requests.
     */
    public static final Integer STATUS_ACTIVE = 1;

    /**
     * Single request record instance for creating new entries.
     */
    public UserRequestsTBL data;
    
    /**
     * List of request records for batch operations.
     */
    public ArrayList<UserRequestsTBL> dataList;

    @Autowired UserRequestsREPO userRequestsREPO;

    /**
     * Fetches request records between two jUser.
     * 
     * @param user_1 the user who sent the request
     * @param user_2 the user who received the request
     * @return list of request records between the two jUser
     */
    public List<UserRequestsTBL> fetch(UserLoginTBL user_1, UserLoginTBL user_2)
    {
        return userRequestsREPO.findBySenderidAndReceiverid(user_1, user_2);
    }

    /**
     * Fetches request records between two jUser with status filter.
     * 
     * @param user_1 the user who sent the request
     * @param user_2 the user who received the request
     * @param status the request status to filter by
     * @return list of request records matching the criteria
     */
    public List<UserRequestsTBL> fetch(UserLoginTBL user_1, UserLoginTBL user_2, Integer status)
    {
        return userRequestsREPO.findBySenderidAndReceiveridAndStatus(user_1, user_2, status);
    }

    /**
     * Fetches request records between two jUser with type and status filters.
     * 
     * @param user_1 the user who sent the request
     * @param user_2 the user who received the request
     * @param type the request type to filter by
     * @param status the request status to filter by
     * @return list of request records matching the criteria
     */
    public List<UserRequestsTBL> fetch(UserLoginTBL user_1, UserLoginTBL user_2, Integer type, Integer status)
    {
        return userRequestsREPO.findBySenderidAndReceiveridAndTypeAndStatus(user_1, user_2, type, status);
    }

    /**
     * Fetches pending requests for a specific user.
     * 
     * <p>This method finds requests where the specified user is the receiver,
     * effectively finding pending requests that the user needs to respond to.</p>
     * 
     * @param user the user to fetch requests for
     * @param type the request type to filter by
     * @param status the request status to filter by
     * @return list of pending request records
     */
    public List<UserRequestsTBL> fetchRequests(UserLoginTBL user, Integer type, Integer status)
    {
        return userRequestsREPO.findByReceiveridAndTypeAndStatus(user, type, status);
    }

    /**
     * Adds a new relationship request between two jUser.
     * 
     * @param senderid the user sending the request
     * @param receiverid the user receiving the request
     * @param type the type of request (friend or follow)
     * @param status the status of the request
     */
    public void add(UserLoginTBL senderid, UserLoginTBL receiverid, Integer type, Integer status)
    {
        data.setSenderid(senderid);
        data.setReceiverid(receiverid);
        data.setType(type);
        data.setStatus(status);
        data.setDateupdated(LocalDateTime.now());
        save();
    }

    /**
     * Updates a request record in the database.
     * 
     * @param request the request record to update
     * @deprecated Use {@link #save()} instead which operates on the {@code data} field
     */
    @Deprecated
    public void update(UserRequestsTBL request)
    {
        userRequestsREPO.save(request);
    }

    /**
     * Saves the current request record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = this.userRequestsREPO.save(data);
    }

    /**
     * Saves all request records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<UserRequestsTBL> datalist = this.userRequestsREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
