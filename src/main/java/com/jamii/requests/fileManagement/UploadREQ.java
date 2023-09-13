package com.jamii.requests.fileManagement;

import org.springframework.web.multipart.MultipartFile;

public class UploadREQ {

    public String user_key;
    public String device_key;
    public MultipartFile uploadfile;

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public MultipartFile getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(MultipartFile uploadfile) {
        this.uploadfile = uploadfile;
    }

    public String getDevice_key() {
        return device_key;
    }

    public void setDevice_key(String device_key) {
        this.device_key = device_key;
    }
}
