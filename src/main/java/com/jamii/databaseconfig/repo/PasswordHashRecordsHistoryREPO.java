package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.PasswordHashRecordsHistoryTBL;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA repository for {@link PasswordHashRecordsHistoryTBL} entities.
 * 
 * <p>This repository provides CRUD operations for password hash history records stored in the database.</p>
 */
public interface PasswordHashRecordsHistoryREPO
        extends CrudRepository<PasswordHashRecordsHistoryTBL, Integer> {
}
