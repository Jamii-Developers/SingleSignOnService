package com.jamii.webapi.activeDirectory;

import com.jamii.Utils.JamiiDebug;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

abstract class activeDirectoryAbstract implements activeDirectoryInterface {

    protected final JamiiDebug jamiiDebug = new JamiiDebug( );

    public abstract void processRequest( ) throws Exception;
    public abstract ResponseEntity<HashMap<String, String> > response( );

    public void reset( ){

    }
}
