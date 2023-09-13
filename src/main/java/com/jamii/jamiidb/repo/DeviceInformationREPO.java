package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeviceInformationREPO extends CrudRepository<DeviceInformationTBL, Integer> {

    List< DeviceInformationTBL > findByUserloginid( UserLoginTBL userLoginTBL );
    List< DeviceInformationTBL > findByUserloginidAndDevicekey( UserLoginTBL userLoginTBL, String devicekey );
    List< DeviceInformationTBL > findByDevicekey( String devicekey );

}
