package com.jamii.requests.fileManagement;

public class UserFileDirectoryUpdateREQ extends  AbstractFileManagement{

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
