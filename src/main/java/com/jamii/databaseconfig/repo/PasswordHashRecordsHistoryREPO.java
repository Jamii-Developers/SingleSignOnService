package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.PasswordHashRecordsHistoryTBL;
import org.springframework.data.repository.CrudRepository;

public interface PasswordHashRecordsHistoryREPO
        extends CrudRepository<PasswordHashRecordsHistoryTBL, Integer> {
}
