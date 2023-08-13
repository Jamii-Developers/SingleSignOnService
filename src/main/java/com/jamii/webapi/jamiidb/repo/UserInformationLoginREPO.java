package com.jamii.webapi.jamiidb.repo;

import com.jamii.webapi.jamiidb.model.UserLoginInformationTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserInformationLoginREPO extends CrudRepository<UserLoginInformationTBL, Integer> {
    List <UserLoginInformationTBL> findByUsername( String username );
    List <UserLoginInformationTBL> findByEmail( String email );
}
