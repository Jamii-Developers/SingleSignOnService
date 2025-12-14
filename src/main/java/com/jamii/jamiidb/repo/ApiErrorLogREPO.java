package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.ApiErrorLogTBL;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiErrorLogREPO
        extends CrudRepository<ApiErrorLogTBL, Integer>
{

}
