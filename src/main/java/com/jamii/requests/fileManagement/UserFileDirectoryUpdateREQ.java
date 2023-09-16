package com.jamii.requests.fileManagement;

public class UserFileDirectoryUpdateREQ {

    private String deviceKey;
    private String userKey;
    private String fileName;
    private String directoryUpdate;

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

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
