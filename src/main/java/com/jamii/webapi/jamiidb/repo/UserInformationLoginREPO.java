package com.jamii.webapi.jamiidb.repo;

import com.jamii.webapi.jamiidb.model.UserLoginInformationTBL;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInformationLoginREPO extends CrudRepository<UserLoginInformationTBL, Integer> {
    List <UserLoginInformationTBL> findByUsernameAndActive( String username, int active );
    List <UserLoginInformationTBL> findByEmailaddressAndActive( String emailAddress, int active );
    List <UserLoginInformationTBL> findByEmailaddressOrUsername( String emailAddress, String username );
    List <UserLoginInformationTBL> findByEmailaddressAndUsername( String emailAddress, String username );
    List <UserLoginInformationTBL> findByUsername( String username );
    List <UserLoginInformationTBL> findByEmailaddress( String emailAddress );


}
