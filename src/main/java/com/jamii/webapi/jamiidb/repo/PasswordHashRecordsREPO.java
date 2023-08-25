package com.jamii.webapi.jamiidb.repo;

import com.jamii.webapi.jamiidb.model.PasswordHashRecordsTBL;
import org.springframework.data.repository.CrudRepository;

public interface PasswordHashRecordsREPO extends CrudRepository<PasswordHashRecordsTBL, Integer> {
}
