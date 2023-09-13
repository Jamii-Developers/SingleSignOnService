package com.jamii.services.FileManagement;


import com.jamii.Utils.JamiiDebug;
import com.jamii.jamiidb.repo.FileDirectoryREPO;
import com.jamii.jamiidb.repo.FileTableOwnerREPO;
import com.jamii.jamiidb.repo.UserLoginREPO;
import com.jamii.requests.fileManagement.UploadREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UploadFileOPS extends fileManagementAbstract {

    @Autowired
    private FileTableOwnerREPO fileTableOwnerREPO;
    @Autowired
    private FileDirectoryREPO fileDirectoryREPO;
    @Autowired
    private UserLoginREPO userLoginREPO;

    protected UploadREQ uploadREQ;

    protected boolean fileUploadSuccessful = false;

    public UploadREQ getUploadREQ( ) {
        return uploadREQ;
    }

    public void setUploadREQ(UploadREQ uploadREQ) {
        this.uploadREQ = uploadREQ;
    }

    @Override
    public void reset( ) {
        super.reset( );
        this.fileUploadSuccessful = false;
        this.uploadREQ = null;
    }

    @Override
    public void processRequest() {

        if( this.userLoginREPO.findByUserkeyIs( this.uploadREQ.getUser_key( ) ) != null ){
            JamiiDebug.warning( "This user key does not exists : " + getUploadREQ( ).getUser_key( ) );
            this.jamiiErrorsMessagesRESP.setUploadFileOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }


    }

    @Override
    public ResponseEntity< String > getResponse() {
        return new ResponseEntity< String >("Something",HttpStatus.OK);
    }
}
