package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.databaseconfig.model.UserRequestsTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link UserRequestsTBL} entities.
 * 
 * <p>This repository provides CRUD operations and custom queries for user request records.</p>
 */
public interface UserRequestsREPO
        extends CrudRepository<UserRequestsTBL, Integer>
{

    /**
     * Finds request records by sender and receiver.
     * @param user1 the user who sent the request
     * @param user2 the user who received the request
     * @return list of matching request records
     */
    List<UserRequestsTBL> findBySenderidAndReceiverid(UserLoginTBL user1, UserLoginTBL user2);

    /**
     * Finds request records by sender, receiver, and status.
     * @param user1 the user who sent the request
     * @param user2 the user who received the request
     * @param status the status to match
     * @return list of matching request records
     */
    List<UserRequestsTBL> findBySenderidAndReceiveridAndStatus(UserLoginTBL user1, UserLoginTBL user2, Integer status);

    /**
     * Finds request records by sender, receiver, type, and status.
     * @param user1 the user who sent the request
     * @param user2 the user who received the request
     * @param type the request type to match
     * @param status the status to match
     * @return list of matching request records
     */
    List<UserRequestsTBL> findBySenderidAndReceiveridAndTypeAndStatus(UserLoginTBL user1, UserLoginTBL user2, Integer type, Integer status);

    /**
     * Finds request records by receiver, type, and status.
     * @param user the user who received the request
     * @param type the request type to match
     * @param status the status to match
     * @return list of matching request records
     */
    List<UserRequestsTBL> findByReceiveridAndTypeAndStatus(UserLoginTBL user, Integer type, Integer status);
}
