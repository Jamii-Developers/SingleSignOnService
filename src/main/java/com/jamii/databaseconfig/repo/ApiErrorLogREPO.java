package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.ApiErrorLogTBL;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiErrorLogREPO
        extends CrudRepository<ApiErrorLogTBL, Integer>
{

}
