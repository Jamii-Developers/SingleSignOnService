package com.jamii.jDrive.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;

public class UserFileDirectoryUpdateREQ
        extends AbstractUserServicesREQ
{

    private String fileName;
    private String directoryUpdate;

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getDirectoryUpdate()
    {
        return directoryUpdate;
    }

    public void setDirectoryUpdate(String directoryUpdate)
    {
        this.directoryUpdate = directoryUpdate;
    }
}
