package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.DeviceInformationTBL;
import com.jamii.databaseconfig.model.UserCookiesTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserCookiesREPO
        extends CrudRepository<UserCookiesTBL, Integer>
{

    List<UserCookiesTBL> findByUserloginidAndActive(UserLoginTBL userLoginTBL, boolean active);

    //    List<UserCookiesTBL> findByUserloginidAndDeviceinformationidAndSessionkeyAndActive(UserLoginTBL userLoginTBL, int device, String cookie, boolean active );
    List<UserCookiesTBL> findByUserloginidAndDeviceinformationidAndSessionkeyAndActive(UserLoginTBL userLoginTBL, DeviceInformationTBL device, String cookie, boolean active);
}
