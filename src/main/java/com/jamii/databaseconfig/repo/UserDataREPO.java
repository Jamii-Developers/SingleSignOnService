package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.UserDataTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDataREPO
        extends CrudRepository<UserDataTBL, Integer>
{

    List<UserDataTBL> findByUserloginid(UserLoginTBL userLoginTBL);

    List<UserDataTBL> findByFirstnameStartingWith(String searchString);

    List<UserDataTBL> findByLastnameStartingWith(String searchString);

    List<UserDataTBL> findByMiddlenameStartingWith(String searchString);
}
