package com.jamii.services.FileManagement;


import com.jamii.jamiidb.repo.FileDirectoryREPO;
import com.jamii.jamiidb.repo.FileTableOwnerREPO;
import com.jamii.requests.fileManagement.UploadREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UploadFileOPS extends FileManagementAbstract{

    @Autowired
    private FileTableOwnerREPO fileTableOwnerREPO;
    @Autowired
    FileDirectoryREPO fileDirectoryREPO;

    protected UploadREQ uploadREQ;

    public UploadREQ getUploadREQ() {
        return uploadREQ;
    }

    public void setUploadREQ(UploadREQ uploadREQ) {
        this.uploadREQ = uploadREQ;
    }

    @Override
    public void reset( ) {
        super.reset();

    }

    @Override
    public void processRequest() {

    }

    @Override
    public ResponseEntity< String > getResponse() {
        return new ResponseEntity< String >("Something",HttpStatus.OK);
    }
}
