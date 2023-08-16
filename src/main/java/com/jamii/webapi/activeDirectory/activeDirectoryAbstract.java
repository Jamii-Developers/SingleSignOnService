package com.jamii.webapi.activeDirectory;

import com.jamii.Utils.JamiiDebug;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

abstract class activeDirectoryAbstract implements activeDirectoryInterface {

    protected final JamiiDebug jamiiDebug = new JamiiDebug( );

    abstract void processRequest( ) ;
    abstract ResponseEntity<HashMap<String, String> > response( );

}
