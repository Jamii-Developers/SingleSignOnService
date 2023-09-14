package com.jamii.services.fileManagement;

import com.jamii.requests.fileManagement.UserFileDeleteREQ;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserFileDeleteOPS extends fileManagementAbstract {

    protected UserFileDeleteREQ userFileDeleteREQ;
    protected Boolean isSuccessful;

    public UserFileDeleteREQ getUserFileDeleteREQ() {
        return userFileDeleteREQ;
    }

    public void setUserFileDeleteREQ(UserFileDeleteREQ userFileDeleteREQ) {
        this.userFileDeleteREQ = userFileDeleteREQ;
    }

    public void reset( ){
        super.reset();
        this.isSuccessful = false ;
        this.userFileDeleteREQ = null;
    }
    @Override
    public void processRequest() throws IOException {

    }

    @Override
    public ResponseEntity<  String > getResponse( ){

        if( this.isSuccessful ){

        }

        return super.getResponse( );
    }
}
