package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.DeviceInformationTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeviceInformationREPO
        extends CrudRepository<DeviceInformationTBL, Integer>
{

    List<DeviceInformationTBL> findByUserloginid(UserLoginTBL userLoginTBL);

    List<DeviceInformationTBL> findByUserloginidAndDevicekey(UserLoginTBL userLoginTBL, String devicekey);

    List<DeviceInformationTBL> findByDevicekey(String devicekey);

    List<DeviceInformationTBL> findByUserloginidAndDevicekeyAndActive(UserLoginTBL userLoginTBL, String devicekey, int active);
}
