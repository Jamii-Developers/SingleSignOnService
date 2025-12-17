package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLoginREPO
        extends CrudRepository<UserLoginTBL, Integer>
{

    List<UserLoginTBL> findByUsernameAndActive(String username, int active);

    List<UserLoginTBL> findByEmailaddressAndActive(String emailaddress, int active);

    List<UserLoginTBL> findByEmailaddressOrUsername(String emailaddress, String username);

    List<UserLoginTBL> findByEmailaddressAndUsername(String emailaddress, String username);

    List<UserLoginTBL> findByEmailaddressAndUsernameAndActive(String emailaddress, String username, int active);

    List<UserLoginTBL> findByEmailaddressAndUsernameAndUserkeyAndActive(String emailaddress, String username, String userkey, int active);

    List<UserLoginTBL> findByUserkeyAndActive(String userkey, int active);

    List<UserLoginTBL> findByUsername(String username);

    List<UserLoginTBL> findByEmailaddress(String emailaddress);

    List<UserLoginTBL> findByUserkeyIs(String userKey);

    List<UserLoginTBL> findByIdAndActive(int id, int active);

    List<UserLoginTBL> findByUsernameStartingWithAndActive(String searchString, Integer activeOn);

    List<UserLoginTBL> findByEmailaddressStartingWithAndActive(String searchString, Integer activeOn);
}
