package com.jamii.webapi.activeDirectory;

import com.jamii.Utils.JamiiDebug;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

abstract class activeDirectoryAbstract implements activeDirectoryInterface {

    protected final JamiiDebug jamiiDebug = new JamiiDebug( );

    public abstract void processRequest( ) throws Exception;

    @Deprecated
    public abstract ResponseEntity<HashMap<String, String> > response( );

    public abstract ResponseEntity< String > getResponse( );

    public void reset( ){

    }
}
