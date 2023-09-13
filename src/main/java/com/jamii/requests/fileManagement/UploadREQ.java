package com.jamii.requests.fileManagement;

import org.springframework.web.multipart.MultipartFile;

public class UploadREQ {

    public String user_key;
    public MultipartFile file;

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
