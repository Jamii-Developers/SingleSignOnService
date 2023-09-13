package com.jamii;


import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.fileManagement.UploadREQ;
import com.jamii.services.fileManagement.UploadFileOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Configured to run on port 4000
 */
@CrossOrigin(origins = "*") // Apply to the entire controller
@SpringBootApplication
@RestController
public class FileManagementServices {

    @Autowired
    private UploadFileOPS uploadFileOPS;

    private final JamiiDebug jamiiDebug = new JamiiDebug( );

    public static void main( String[ ] args ) {
        SpringApplication.run( FileManagementServices.class, args);
    }

    @PostMapping( path = "userfileupload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public @ResponseBody ResponseEntity< String > userfileupload(@ModelAttribute UploadREQ uploadREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.uploadFileOPS.reset( );
        this.uploadFileOPS.setUploadREQ( uploadREQ );
        this.uploadFileOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.uploadFileOPS.getResponse( );
    }




}
