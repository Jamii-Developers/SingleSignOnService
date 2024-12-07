package com.jamii.applicationControllers.userControllers;

import com.jamii.Utils.JamiiDebug;
import com.jamii.operations.clientCommunication.FunctionOPS.ContactUsOPS;
import com.jamii.requests.clientCommunication.FunctionREQ.ContactUsREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ClientCommunicationServices {

    @Autowired
    private ContactUsOPS contactUsOPS;

    protected final JamiiDebug jamiiDebug = new JamiiDebug( this.getClass( ) );

    @PostMapping( path = "contactus", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< ? > contactus( @RequestBody ContactUsREQ contactUsREQ ) throws Exception {

        jamiiDebug.info("Received request" );

        this.contactUsOPS.reset( );
        this.contactUsOPS.setContactUsREQ( contactUsREQ );
        this.contactUsOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.contactUsOPS.getResponse( );
    }
}
