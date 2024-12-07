package com.jamii.requests.userServices.fileManagementREQ;

import com.jamii.requests.userServices.AbstractUserServicesREQ;
import org.springframework.web.multipart.MultipartFile;

public class UserFileUploadServicesREQ extends AbstractUserServicesREQ {

    public MultipartFile uploadfile;

    public MultipartFile getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(MultipartFile uploadfile) {
        this.uploadfile = uploadfile;
    }

}
