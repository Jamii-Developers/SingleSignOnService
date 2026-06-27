package com.jamii.jSocial.repo;

import com.jamii.jSocial.model.UserBlockListTBL;
import com.jamii.jUser.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link UserBlockListTBL} entities.
 * 
 * <p>This repository provides CRUD operations and custom queries for user block list records.</p>
 */
public interface UserBlockListREPO
        extends CrudRepository<UserBlockListTBL, Integer>
{

    /**
     * Finds block list records by user, blocked user, and status.
     * @param userid the user who blocked someone
     * @param blockedid the user who was blocked
     * @param status the status to match
     * @return list of matching block list records
     */
    List<UserBlockListTBL> findByUseridAndBlockedidAndStatus(UserLoginTBL userid, UserLoginTBL blockedid, Integer status);

    /**
     * Finds block list records by user and blocked user.
     * @param userid the user who blocked someone
     * @param blockedid the user who was blocked
     * @return list of matching block list records
     */
    List<UserBlockListTBL> findByUseridAndBlockedid(UserLoginTBL userid, UserLoginTBL blockedid);

    /**
     * Finds block list records by user and status.
     * @param user the user to search for
     * @param status the status to match
     * @return list of matching block list records
     */
    List<UserBlockListTBL> findByUseridAndStatus(UserLoginTBL user, Integer status);
}
