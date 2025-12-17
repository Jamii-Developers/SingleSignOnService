package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.UserBlockListTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserBlockListREPO
        extends CrudRepository<UserBlockListTBL, Integer>
{

    List<UserBlockListTBL> findByUseridAndBlockedidAndStatus(UserLoginTBL userid, UserLoginTBL blockedid, Integer status);

    List<UserBlockListTBL> findByUseridAndBlockedid(UserLoginTBL userid, UserLoginTBL blockedid);

    List<UserBlockListTBL> findByUseridAndStatus(UserLoginTBL user, Integer status);
}
