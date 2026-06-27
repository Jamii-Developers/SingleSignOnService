package com.jamii.jSocial.repo;

import com.jamii.jSocial.model.UserRolesTBL;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA repository for {@link UserRolesTBL} entities.
 * 
 * <p>This repository provides CRUD operations for user role records stored in the database.</p>
 */
public interface UserRolesREPO
        extends CrudRepository<UserRolesTBL, Integer>
{}
