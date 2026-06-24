package com.jamii.jDrive.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;

public class UserFileDeleteREQ
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
