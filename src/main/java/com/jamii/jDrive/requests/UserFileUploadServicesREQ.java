package com.jamii.jDrive.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;
import org.springframework.web.multipart.MultipartFile;

public class UserFileUploadServicesREQ
        extends AbstractUserServicesREQ
{

    public MultipartFile uploadFile;

    public MultipartFile getUploadfile()
    {
        return uploadFile;
    }

    public void setUploadfile(MultipartFile uploadfile)
    {
        this.uploadFile = uploadfile;
    }
}
