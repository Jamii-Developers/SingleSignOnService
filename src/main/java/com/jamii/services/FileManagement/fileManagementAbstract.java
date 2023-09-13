package com.jamii.services.FileManagement;

import com.jamii.responses.JamiiErrorsMessagesRESP;
import org.springframework.http.ResponseEntity;

abstract class fileManagementAbstract {

    protected String JamiiError;
    protected JamiiErrorsMessagesRESP jamiiErrorsMessagesRESP = null;

    public void reset( ){
        this.JamiiError = "";
        this.jamiiErrorsMessagesRESP = new JamiiErrorsMessagesRESP( );
    }

    public abstract void processRequest( );

    public abstract ResponseEntity< String > getResponse( );

}
