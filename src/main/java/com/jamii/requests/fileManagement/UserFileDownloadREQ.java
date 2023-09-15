package com.jamii.requests.fileManagement;

public class UserFileDownloadREQ {

    private String devicekey;
    private String userkey;
    private String fileName;

    public String getDevicekey() {
        return devicekey;
    }

    public void setDevicekey(String devicekey) {
        this.devicekey = devicekey;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
