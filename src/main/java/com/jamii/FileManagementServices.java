package com.jamii;


import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.fileManagement.UploadREQ;
import com.jamii.services.FileManagement.UploadFileOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Configured to run on port 4000
 */
@CrossOrigin(origins = "*") // Apply to the entire controller
@SpringBootApplication
@RestController
public class FileManagementServices {

    @Autowired
    UploadFileOPS uploadFileOPS;

    private final JamiiDebug jamiiDebug = new JamiiDebug( );

    public static void main( String[ ] args ) {
        SpringApplication.run( FileManagementServices.class, args);
    }

    @PostMapping( path = "upload", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity< String > upload(@RequestBody UploadREQ uploadREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.uploadFileOPS.reset( );
        this.uploadFileOPS.setUploadREQ( uploadREQ );
        this.uploadFileOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.uploadFileOPS.getResponse( );
    }




}
