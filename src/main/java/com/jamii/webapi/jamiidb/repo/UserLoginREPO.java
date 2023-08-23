package com.jamii.webapi.jamiidb.repo;

import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLoginREPO extends CrudRepository<UserLoginTBL, Integer> {
    List <UserLoginTBL> findByUsernameAndActive(String username, int active );
    List <UserLoginTBL> findByEmailaddressAndActive(String emailAddress, int active );
    List <UserLoginTBL> findByEmailaddressOrUsername(String emailAddress, String username );
    
    List <UserLoginTBL> findByEmailaddressAndUsername(String emailAddress, String username );
    List <UserLoginTBL> findByUsername(String username );
    List <UserLoginTBL> findByEmailaddress(String emailAddress );


}
