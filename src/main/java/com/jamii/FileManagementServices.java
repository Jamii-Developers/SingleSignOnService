package com.jamii;


import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.fileManagement.UserFileDeleteREQ;
import com.jamii.requests.fileManagement.UserFileDirectoryUpdateREQ;
import com.jamii.requests.fileManagement.UserFileDownloadREQ;
import com.jamii.requests.fileManagement.UserFileUploadREQ;
import com.jamii.services.fileManagement.UserFileDeleteOPS;
import com.jamii.services.fileManagement.UserFileDirectoryUpdateOPS;
import com.jamii.services.fileManagement.UserFileDownloadOPS;
import com.jamii.services.fileManagement.UserFileUploadOPS;
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
    private UserFileUploadOPS userFileUploadOPS;
    @Autowired
    private UserFileDownloadOPS userFileDownloadOPS;
    @Autowired
    private UserFileDeleteOPS userFileDeleteOPS;
    @Autowired
    private UserFileDirectoryUpdateOPS userFileDirectoryUpdateOPS;


    private final JamiiDebug jamiiDebug = new JamiiDebug( );

    public static void main( String[ ] args ) {
        SpringApplication.run( FileManagementServices.class, args);
    }

    @PostMapping( path = "userfileupload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public @ResponseBody ResponseEntity< ? > userfileupload( @ModelAttribute UserFileUploadREQ userFileUploadREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.userFileUploadOPS.reset( );
        this.userFileUploadOPS.setUploadREQ(userFileUploadREQ);
        this.userFileUploadOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.userFileUploadOPS.getResponse( );
    }

    @GetMapping( path = "/userfiledownload/{filename}", consumes = MediaType.APPLICATION_JSON_VALUE )
    public  ResponseEntity< ? > userfiledownload( @PathVariable("filename") String filename, @RequestBody UserFileDownloadREQ userFileDownloadREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.userFileDownloadOPS.reset( );
        this.userFileDownloadOPS.setUserFileDownloadREQ( userFileDownloadREQ ) ;
        this.userFileDownloadOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.userFileDownloadOPS.getResponse( );
    }

    @PostMapping( path = "userfiledelete", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  ResponseEntity< ? > userfiledelete( @RequestBody UserFileDeleteREQ userFileDeleteREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.userFileDeleteOPS.reset( );
        this.userFileDeleteOPS.setUserFileDeleteREQ( userFileDeleteREQ ); ;
        this.userFileDeleteOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.userFileUploadOPS.getResponse( );
    }

    @PostMapping( path = "userfiledirectoryupdate", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  ResponseEntity< ? > userfiledirectoryupdate( @RequestBody UserFileDirectoryUpdateREQ userFileDirectoryUpdateREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.userFileDirectoryUpdateOPS.reset( );
        this.userFileDirectoryUpdateOPS.setUserFileDirectoryUpdateREQ( userFileDirectoryUpdateREQ ); ;
        this.userFileDirectoryUpdateOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.userFileUploadOPS.getResponse( );
    }




}
