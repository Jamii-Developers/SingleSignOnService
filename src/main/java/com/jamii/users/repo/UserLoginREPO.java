package com.jamii.users.repo;

import com.jamii.users.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link UserLoginTBL} entities.
 * 
 * <p>This repository provides CRUD operations and custom queries for user login records.</p>
 */
@Repository
public interface UserLoginREPO
        extends CrudRepository<UserLoginTBL, Integer>
{

    /**
     * Finds user login records by username and active status.
     * @param username the username to match
     * @param active the active status to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByUsernameAndActive(String username, int active);

    /**
     * Finds user login records by email address and active status.
     * @param emailaddress the email address to match
     * @param active the active status to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByEmailaddressAndActive(String emailaddress, int active);

    /**
     * Finds user login records by email address or username.
     * @param emailaddress the email address to match
     * @param username the username to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByEmailaddressOrUsername(String emailaddress, String username);

    /**
     * Finds user login records by email address and username.
     * @param emailaddress the email address to match
     * @param username the username to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByEmailaddressAndUsername(String emailaddress, String username);

    /**
     * Finds user login records by email address, username, and active status.
     * @param emailaddress the email address to match
     * @param username the username to match
     * @param active the active status to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByEmailaddressAndUsernameAndActive(String emailaddress, String username, int active);

    /**
     * Finds user login records by email address, username, user key, and active status.
     * @param emailaddress the email address to match
     * @param username the username to match
     * @param userkey the user key to match
     * @param active the active status to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByEmailaddressAndUsernameAndUserkeyAndActive(String emailaddress, String username, String userkey, int active);

    /**
     * Finds user login records by user key and active status.
     * @param userkey the user key to match
     * @param active the active status to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByUserkeyAndActive(String userkey, int active);

    /**
     * Finds user login records by username.
     * @param username the username to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByUsername(String username);

    /**
     * Finds user login records by email address.
     * @param emailaddress the email address to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByEmailaddress(String emailaddress);

    /**
     * Finds user login records by user key.
     * @param userKey the user key to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByUserkeyIs(String userKey);

    /**
     * Finds user login records by ID and active status.
     * @param id the ID to match
     * @param active the active status to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByIdAndActive(int id, int active);

    /**
     * Finds user login records by username starting with search string and active status.
     * @param searchString the string to match at the start of username
     * @param activeOn the active status to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByUsernameStartingWithAndActive(String searchString, Integer activeOn);

    /**
     * Finds user login records by email address starting with search string and active status.
     * @param searchString the string to match at the start of email address
     * @param activeOn the active status to match
     * @return list of matching user login records
     */
    List<UserLoginTBL> findByEmailaddressStartingWithAndActive(String searchString, Integer activeOn);
}
