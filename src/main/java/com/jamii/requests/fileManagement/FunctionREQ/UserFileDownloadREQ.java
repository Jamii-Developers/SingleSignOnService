package com.jamii.requests.fileManagement.FunctionREQ;

import com.jamii.requests.fileManagement.AbstractFileManagement;

public class UserFileDownloadREQ extends AbstractFileManagement {

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
