package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.UserDataHistoryTBL;
import org.springframework.data.repository.CrudRepository;

public interface UserDataHistoryREPO
        extends CrudRepository<UserDataHistoryTBL, Integer>
{

}
