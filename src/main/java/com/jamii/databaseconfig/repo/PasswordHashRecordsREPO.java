package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.PasswordHashRecordsTBL;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link PasswordHashRecordsTBL} entities.
 * 
 * <p>This repository provides CRUD operations and custom queries for password hash records.</p>
 */
public interface PasswordHashRecordsREPO
        extends CrudRepository<PasswordHashRecordsTBL, Integer>
{

    /**
     * Finds the last 10 password hash records for a user.
     * @param userloginid the user ID to search for
     * @return list of the last 10 password hash records for the user
     */
    @Query(value = "SELECT * FROM jamiidb.Password_Hash_Records WHERE USER_LOGIN_ID = ? ORDER BY ID DESC LIMIT 10", nativeQuery = true)
    List<PasswordHashRecordsTBL> findLast10Passwords(int userloginid);
}
