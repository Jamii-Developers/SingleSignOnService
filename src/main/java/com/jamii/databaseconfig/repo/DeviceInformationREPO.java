package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.DeviceInformationTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link DeviceInformationTBL} entities.
 * 
 * <p>This repository provides CRUD operations and custom queries for device information records.</p>
 */
public interface DeviceInformationREPO
        extends CrudRepository<DeviceInformationTBL, Integer>
{

    /**
     * Finds device information records by user.
     * @param userLoginTBL the user to search for
     * @return list of device information records for the user
     */
    List<DeviceInformationTBL> findByUserloginid(UserLoginTBL userLoginTBL);

    /**
     * Finds device information records by user and device key.
     * @param userLoginTBL the user to search for
     * @param devicekey the device key to match
     * @return list of matching device information records
     */
    List<DeviceInformationTBL> findByUserloginidAndDevicekey(UserLoginTBL userLoginTBL, String devicekey);

    /**
     * Finds device information records by device key.
     * @param devicekey the device key to match
     * @return list of matching device information records
     */
    List<DeviceInformationTBL> findByDevicekey(String devicekey);

    /**
     * Finds device information records by user, device key, and active status.
     * @param userLoginTBL the user to search for
     * @param devicekey the device key to match
     * @param active the active status to match
     * @return list of matching device information records
     */
    List<DeviceInformationTBL> findByUserloginidAndDevicekeyAndActive(UserLoginTBL userLoginTBL, String devicekey, int active);
}
