package com.jamii.services.FileManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

abstract class FileManagementAbstract {



    public void reset( ){

    }

    public abstract void processRequest( );

    public abstract ResponseEntity< String > getResponse( );

}
