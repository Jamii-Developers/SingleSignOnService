package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.UserGroupsTBL;
import org.springframework.data.repository.CrudRepository;

public interface UserGroupsREPO
        extends CrudRepository<UserGroupsTBL, Integer>
{}
