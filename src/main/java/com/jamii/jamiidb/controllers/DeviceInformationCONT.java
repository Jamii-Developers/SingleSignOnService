package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.DeviceInformationREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DeviceInformationCONT {

    @Autowired
    private DeviceInformationREPO deviceInformationREPO;


    public boolean checkIfKeyExisitsInTheDatabase( String deviceKey ){
        return deviceInformationREPO.findByDevicekey( deviceKey ).isEmpty( );
    }

    public Optional< DeviceInformationTBL > fetchByUserandDeviceKey( UserLoginTBL user, String deviceKey){
        return deviceInformationREPO.findByUserloginidAndDevicekey( user, deviceKey ).stream().findFirst();
    }

    public DeviceInformationTBL add( UserLoginTBL userLoginTBL, String key, String deviceName){
        DeviceInformationTBL deviceInformationTBL = new DeviceInformationTBL( );
        deviceInformationTBL.setDevicekey( key );
        deviceInformationTBL.setLastconnected( LocalDateTime.now( ) );
        deviceInformationTBL.setUserloginid( userLoginTBL );
        deviceInformationTBL.setDevicename( deviceName );
        deviceInformationTBL.setActive( DeviceInformationTBL.ACTIVE_STATUS_ENABLED );

        return this.deviceInformationREPO.save( deviceInformationTBL);
    }



}
