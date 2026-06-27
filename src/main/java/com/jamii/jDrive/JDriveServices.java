package com.jamii.jDrive;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import com.jamii.jDrive.services.UserFileDeleteOPS;
import com.jamii.jDrive.services.UserFileDirectoryUpdateOPS;
import com.jamii.jDrive.services.UserFileDownloadOPS;
import com.jamii.jDrive.services.UserFileUploadOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jdrive/")
@CrossOrigin(origins = "*")
public class JDriveServices extends AbstractApplicationControllers
{

    @Autowired UserFileUploadOPS userFileUploadOPS;
    @Autowired UserFileDirectoryUpdateOPS userFileDirectoryUpdateOPS;
    @Autowired UserFileDeleteOPS userFileDeleteOPS;
    @Autowired UserFileDownloadOPS userFileDownloadOPS;


    @Override
    protected void initPathing()
    {
        directoryMap.put("userfileupload", userFileUploadOPS);
        directoryMap.put("userdirupd", userFileDirectoryUpdateOPS);
        directoryMap.put("userfiledel", userFileDeleteOPS);
        directoryMap.put("userfiledwnld", userFileDownloadOPS);
    }
}
