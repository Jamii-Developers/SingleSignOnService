package com.jamii.jAdmin.repo;

import com.jamii.jAdmin.model.ApiErrorLogTBL;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link ApiErrorLogTBL} entities.
 * 
 * <p>This repository provides CRUD operations for API error log records stored in the database.</p>
 */
@Repository
public interface ApiErrorLogREPO
        extends CrudRepository<ApiErrorLogTBL, Integer>
{

}
