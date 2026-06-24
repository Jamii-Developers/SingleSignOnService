package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.UserGroupsTBL;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA repository for {@link UserGroupsTBL} entities.
 * 
 * <p>This repository provides CRUD operations for user group records stored in the database.</p>
 */
public interface UserGroupsREPO
        extends CrudRepository<UserGroupsTBL, Integer>
{}
