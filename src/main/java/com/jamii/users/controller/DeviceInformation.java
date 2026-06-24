package com.jamii.users.controller;

import com.jamii.users.model.DeviceInformationTBL;
import com.jamii.users.model.UserLoginTBL;
import com.jamii.users.repo.DeviceInformationREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Service component for managing device information records in the database.
 * 
 * <p>This class handles device registration, tracking, and management for user sessions.
 * Devices can be associated with user accounts and have different active states.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Check if device key exists in database</li>
 *     <li>Fetch device information by user and device key</li>
 *     <li>Add new device records</li>
 *     <li>Save device records individually or in batch</li>
 * </ul>
 */
@Component
public class DeviceInformation
{

    /**
     * Device active status constant for disabled devices.
     */
    public static final Integer ACTIVE_STATUS_DISABLED = 0;
    
    /**
     * Device active status constant for enabled devices.
     */
    public static final Integer ACTIVE_STATUS_ENABLED = 1;
    
    /**
     * Device active status constant for blocked devices.
     */
    public static final Integer ACTIVE_STATUS_BLOCKED = 2;

    /**
     * Single device record instance for creating new entries.
     */
    public DeviceInformationTBL data ;
    
    /**
     * List of device records for batch operations.
     */
    public ArrayList<DeviceInformationTBL> dataList;
    @Autowired private DeviceInformationREPO deviceInformationREPO;

    /**
     * Checks if a device key exists in the database.
     * 
     * @param deviceKey the device key to check
     * @return {@code true} if the device key does not exist, {@code false} if it exists
     */
    public boolean checkIfKeyExisitsInTheDatabase(String deviceKey)
    {
        return deviceInformationREPO.findByDevicekey(deviceKey).isEmpty();
    }

    /**
     * Checks if a device key exists for a specific user in the database.
     * 
     * @param user the user to check against
     * @param deviceKey the device key to check
     * @return {@code true} if the device key does not exist for the user, {@code false} if it exists
     */
    public boolean checkIfKeyExisitsInTheDatabase(UserLoginTBL user, String deviceKey)
    {
        return deviceInformationREPO.findByUserloginidAndDevicekey(user, deviceKey).isEmpty();
    }

    /**
     * Fetches device information for a specific user and device key.
     * 
     * @param user the user to fetch device information for
     * @param deviceKey the device key to fetch
     * @return an Optional containing the device information if found, empty otherwise
     */
    public Optional<DeviceInformationTBL> fetch(UserLoginTBL user, String deviceKey)
    {
        return deviceInformationREPO.findByUserloginidAndDevicekey(user, deviceKey).stream().findFirst();
    }

    /**
     * Fetches device information for a specific user, device key, and active status.
     * 
     * @param user the user to fetch device information for
     * @param deviceKey the device key to fetch
     * @param active the active status to filter by
     * @return an Optional containing the device information if found, empty otherwise
     */
    public Optional<DeviceInformationTBL> fetch(UserLoginTBL user, String deviceKey, int active)
    {
        return deviceInformationREPO.findByUserloginidAndDevicekeyAndActive(user, deviceKey, active).stream().findFirst();
    }

    /**
     * Updates a device information record in the database.
     * 
     * @param deviceInformationTBL the device information to update
     * @deprecated Use {@link #save()} instead which operates on the {@code data} field
     */
    @Deprecated
    public void update(DeviceInformationTBL deviceInformationTBL)
    {
        this.deviceInformationREPO.save(deviceInformationTBL);
    }

    /**
     * Adds a new device record for a user.
     * 
     * @param userLoginTBL the user to associate the device with
     * @param key the device key
     * @param deviceName the name of the device
     * @param location the location of the device
     * @return the saved device information record with generated ID
     */
    public DeviceInformationTBL add(UserLoginTBL userLoginTBL, String key, String deviceName, String location)
    {

        data.setDevicekey(key);
        data.setLastconnected(LocalDateTime.now());
        data.setUserloginid(userLoginTBL);
        data.setDevicename(deviceName);
        data.setLocation(location);
        data.setActive(ACTIVE_STATUS_ENABLED);
        return this.deviceInformationREPO.save(data);
    }

    /**
     * Saves the current device record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = deviceInformationREPO.save(data);
    }

    /**
     * Saves all device records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<DeviceInformationTBL> datalist = this.deviceInformationREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
