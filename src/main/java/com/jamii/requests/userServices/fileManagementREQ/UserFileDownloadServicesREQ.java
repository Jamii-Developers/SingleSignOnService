package com.jamii.requests.userServices.fileManagementREQ;

import com.jamii.requests.userServices.AbstractUserServicesREQ;

public class UserFileDownloadServicesREQ extends AbstractUserServicesREQ {

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
