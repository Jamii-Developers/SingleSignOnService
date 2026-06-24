package com.jamii.jDrive.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;

public class UserFileDownloadREQ
        extends AbstractUserServicesREQ
{

    private String fileName;

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
