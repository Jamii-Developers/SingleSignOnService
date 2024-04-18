package com.jamii.requests.fileManagement.FunctionREQ;

import com.jamii.requests.fileManagement.AbstractFileManagement;
import org.springframework.web.multipart.MultipartFile;

public class UserFileUploadREQ extends AbstractFileManagement {

    public MultipartFile uploadfile;

    public MultipartFile getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(MultipartFile uploadfile) {
        this.uploadfile = uploadfile;
    }

}
