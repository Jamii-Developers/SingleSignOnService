package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.databaseconfig.model.UserRequestsTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRequestsREPO
        extends CrudRepository<UserRequestsTBL, Integer>
{

    List<UserRequestsTBL> findBySenderidAndReceiverid(UserLoginTBL user1, UserLoginTBL user2);

    List<UserRequestsTBL> findBySenderidAndReceiveridAndStatus(UserLoginTBL user1, UserLoginTBL user2, Integer status);

    List<UserRequestsTBL> findBySenderidAndReceiveridAndTypeAndStatus(UserLoginTBL user1, UserLoginTBL user2, Integer type, Integer status);

    List<UserRequestsTBL> findByReceiveridAndTypeAndStatus(UserLoginTBL user, Integer type, Integer status);
}
