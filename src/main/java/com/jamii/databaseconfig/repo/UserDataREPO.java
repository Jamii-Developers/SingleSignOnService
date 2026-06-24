package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.UserDataTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link UserDataTBL} entities.
 * 
 * <p>This repository provides CRUD operations and custom queries for user data records.</p>
 */
public interface UserDataREPO
        extends CrudRepository<UserDataTBL, Integer>
{

    /**
     * Finds user data records by user.
     * @param userLoginTBL the user to search for
     * @return list of user data records for the user
     */
    List<UserDataTBL> findByUserloginid(UserLoginTBL userLoginTBL);

    /**
     * Finds user data records by first name starting with the search string.
     * @param searchString the string to match at the start of first name
     * @return list of matching user data records
     */
    List<UserDataTBL> findByFirstnameStartingWith(String searchString);

    /**
     * Finds user data records by last name starting with the search string.
     * @param searchString the string to match at the start of last name
     * @return list of matching user data records
     */
    List<UserDataTBL> findByLastnameStartingWith(String searchString);

    /**
     * Finds user data records by middle name starting with the search string.
     * @param searchString the string to match at the start of middle name
     * @return list of matching user data records
     */
    List<UserDataTBL> findByMiddlenameStartingWith(String searchString);
}
