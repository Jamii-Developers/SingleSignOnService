package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.UserDataHistoryTBL;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA repository for {@link UserDataHistoryTBL} entities.
 * 
 * <p>This repository provides CRUD operations for user data history records stored in the database.</p>
 */
public interface UserDataHistoryREPO
        extends CrudRepository<UserDataHistoryTBL, Integer>
{

}
