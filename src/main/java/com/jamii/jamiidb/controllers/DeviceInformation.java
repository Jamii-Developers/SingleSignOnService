package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.DeviceInformationREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class DeviceInformation
{

    /**
     * Active Statuses
     */
    public static final Integer ACTIVE_STATUS_DISABLED = 0;
    public static final Integer ACTIVE_STATUS_ENABLED = 1;
    public static final Integer ACTIVE_STATUS_BLOCKED = 2;

    //Creating a table object to reference when creating data for that table
    public DeviceInformationTBL data = new DeviceInformationTBL();
    public ArrayList<DeviceInformationTBL> dataList = new ArrayList<>();
    @Autowired private DeviceInformationREPO deviceInformationREPO;

    public boolean checkIfKeyExisitsInTheDatabase(String deviceKey)
    {
        return deviceInformationREPO.findByDevicekey(deviceKey).isEmpty();
    }

    public boolean checkIfKeyExisitsInTheDatabase(UserLoginTBL user, String deviceKey)
    {
        return deviceInformationREPO.findByUserloginidAndDevicekey(user, deviceKey).isEmpty();
    }

    public Optional<DeviceInformationTBL> fetch(UserLoginTBL user, String deviceKey)
    {
        return deviceInformationREPO.findByUserloginidAndDevicekey(user, deviceKey).stream().findFirst();
    }

    public Optional<DeviceInformationTBL> fetch(UserLoginTBL user, String deviceKey, int active)
    {
        return deviceInformationREPO.findByUserloginidAndDevicekeyAndActive(user, deviceKey, active).stream().findFirst();
    }

    /**
     * This has been replaced with the new method save( )
     *
     * @param deviceInformationTBL
     */
    @Deprecated
    public void update(DeviceInformationTBL deviceInformationTBL)
    {
        this.deviceInformationREPO.save(deviceInformationTBL);
    }

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

    public void save()
    {
        data = deviceInformationREPO.save(data);
    }

    public void saveAll()
    {
        Iterable<DeviceInformationTBL> datalist = this.deviceInformationREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
