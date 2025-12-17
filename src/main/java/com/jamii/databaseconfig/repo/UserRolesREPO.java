package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.UserRolesTBL;
import org.springframework.data.repository.CrudRepository;

public interface UserRolesREPO
        extends CrudRepository<UserRolesTBL, Integer>
{}
