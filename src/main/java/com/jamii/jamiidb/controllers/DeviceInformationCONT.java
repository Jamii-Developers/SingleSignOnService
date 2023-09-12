package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.repo.DeviceInformationREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceInformationCONT {

    @Autowired
    private DeviceInformationREPO deviceInformationREPO;

}
