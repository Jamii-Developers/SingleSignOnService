package com.jamii.services.fileManagement;

import com.jamii.requests.fileManagement.UserFileDownloadREQ;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserFileDownloadOPS extends fileManagementAbstract {

    protected UserFileDownloadREQ userFileDownloadREQ;
    protected boolean isSuccessful = false;

    public UserFileDownloadREQ getUserFileDownloadREQ() {
        return userFileDownloadREQ;
    }

    public void setUserFileDownloadREQ(UserFileDownloadREQ userFileDownloadREQ) {
        this.userFileDownloadREQ = userFileDownloadREQ;
    }

    @Override
    public void processRequest() throws IOException {

    }

    @Override
    public void reset( ){
        super.reset( );
        setUserFileDownloadREQ( null ) ;
    }

    @Override
    public ResponseEntity<  String > getResponse( ){

        if( this.isSuccessful ){

        }

        return super.getResponse( );
    }
}
