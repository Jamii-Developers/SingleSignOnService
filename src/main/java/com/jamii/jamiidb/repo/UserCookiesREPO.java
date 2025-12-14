package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserCookiesTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserCookiesREPO
        extends CrudRepository<UserCookiesTBL, Integer>
{

    List<UserCookiesTBL> findByUserloginidAndActive(UserLoginTBL userLoginTBL, boolean active);

    //    List<UserCookiesTBL> findByUserloginidAndDeviceinformationidAndSessionkeyAndActive(UserLoginTBL userLoginTBL, int device, String cookie, boolean active );
    List<UserCookiesTBL> findByUserloginidAndDeviceinformationidAndSessionkeyAndActive(UserLoginTBL userLoginTBL, DeviceInformationTBL device, String cookie, boolean active);
}
