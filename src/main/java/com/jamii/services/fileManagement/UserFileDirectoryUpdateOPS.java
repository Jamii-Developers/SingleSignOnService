package com.jamii.services.fileManagement;

import com.jamii.requests.fileManagement.UserFileDirectoryUpdateREQ;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserFileDirectoryUpdateOPS extends fileManagementAbstract {

    private UserFileDirectoryUpdateREQ userFileDirectoryUpdateREQ;
    protected Boolean isSuccessful = true ;

    public UserFileDirectoryUpdateREQ getUserFileDirectoryUpdateREQ() {
        return userFileDirectoryUpdateREQ;
    }

    public void setUserFileDirectoryUpdateREQ(UserFileDirectoryUpdateREQ userFileDirectoryUpdateREQ) {
        this.userFileDirectoryUpdateREQ = userFileDirectoryUpdateREQ;
    }

    @Override
    public void reset( ){
        super.reset( );
        this.isSuccessful = true ;
        this.userFileDirectoryUpdateREQ = null ;
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
