package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.PasswordHashRecordsTBL;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PasswordHashRecordsREPO extends CrudRepository<PasswordHashRecordsTBL, Integer> {

    @Query( value = "SELECT * FROM Password_Hash_Records WHERE USER_LOGIN_ID = ? ORDER BY ID DESC LIMIT 10", nativeQuery = true)
    List< PasswordHashRecordsTBL > findLast10Passwords( int userloginid );
}
