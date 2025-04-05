package com.jamii.requests.userServices.fileManagementREQ;

import com.jamii.requests.userServices.AbstractUserServicesREQ;
import org.springframework.web.multipart.MultipartFile;

public class UserFileUploadServicesREQ extends AbstractUserServicesREQ {

    public MultipartFile uploadFile;

    public MultipartFile getUploadfile() {
        return uploadFile;
    }

    public void setUploadfile(MultipartFile uploadfile) {
        this.uploadFile = uploadfile;
    }

}
