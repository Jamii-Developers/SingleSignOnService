package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.DeviceInformationREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class DeviceInformationCONT {

    @Autowired
    private DeviceInformationREPO deviceInformationREPO;



    public boolean checkIfKeyExisitsInTheDatabase( String deviceKey ){
        return deviceInformationREPO.findByDevicekey( deviceKey ).isEmpty( );
    }

    public Optional< DeviceInformationTBL > fetch( UserLoginTBL user, String deviceKey){
        return deviceInformationREPO.findByUserloginidAndDevicekey( user, deviceKey ).stream().findFirst();
    }
    public Optional< DeviceInformationTBL > fetch( UserLoginTBL user, String deviceKey, int active ){
        return deviceInformationREPO.findByUserloginidAndDevicekeyAndActive( user, deviceKey, active ).stream().findFirst();
    }

    public void update( DeviceInformationTBL deviceInformationTBL ){
        this.deviceInformationREPO.save( deviceInformationTBL );
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
