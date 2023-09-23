package com.jamii;

import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.clientCommunication.ContactUsREQ;
import com.jamii.services.clientCommunication.ContactUsOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Configured to run on port 4001
 */
@CrossOrigin(origins = "*") // Apply to the entire controller
@RestController
public class ClientCommunicationServices {

    @Autowired
    private ContactUsOPS contactUsOPS;

    private final JamiiDebug jamiiDebug = new JamiiDebug( );

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
