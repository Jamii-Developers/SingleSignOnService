package com.jamii.requests.fileManagement;

public class UserFileDirectoryUpdateREQ {

    private String device_key;
    private String user_key;
    private String fileName;
    private String directory_update;

    public String getDevice_key() {
        return device_key;
    }

    public void setDevice_key(String device_key) {
        this.device_key = device_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDirectory_update() {
        return directory_update;
    }

    public void setDirectory_update(String directory_update) {
        this.directory_update = directory_update;
    }
}
