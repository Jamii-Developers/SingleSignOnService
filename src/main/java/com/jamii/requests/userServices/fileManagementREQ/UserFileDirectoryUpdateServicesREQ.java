package com.jamii.requests.userServices.fileManagementREQ;

import com.jamii.requests.userServices.AbstractUserServicesREQ;

public class UserFileDirectoryUpdateServicesREQ extends AbstractUserServicesREQ {

    private String fileName;
    private String directoryUpdate;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDirectoryUpdate() {
        return directoryUpdate;
    }

    public void setDirectoryUpdate(String directoryUpdate) {
        this.directoryUpdate = directoryUpdate;
    }
}
