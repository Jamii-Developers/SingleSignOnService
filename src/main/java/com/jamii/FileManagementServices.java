package com.jamii;


import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.fileManagement.FunctionREQ.UserFileDeleteREQ;
import com.jamii.requests.fileManagement.FunctionREQ.UserFileDirectoryUpdateREQ;
import com.jamii.requests.fileManagement.FunctionREQ.UserFileDownloadREQ;
import com.jamii.requests.fileManagement.FunctionREQ.UserFileUploadREQ;
import com.jamii.operations.fileManagement.FunctionOPS.UserFileDeleteOPS;
import com.jamii.operations.fileManagement.FunctionOPS.UserFileDirectoryUpdateOPS;
import com.jamii.operations.fileManagement.FunctionOPS.UserFileDownloadOPS;
import com.jamii.operations.fileManagement.FunctionOPS.UserFileUploadOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Configured to run on port 4000
 */
@CrossOrigin(origins = "*") // Apply to the entire controller
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
        this.userFileUploadOPS.setUserFileUploadREQ( userFileUploadREQ );
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

    @PostMapping( path = "userfiledelete", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity< ? > userfiledelete( @RequestBody UserFileDeleteREQ userFileDeleteREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.userFileDeleteOPS.reset( );
        this.userFileDeleteOPS.setUserFileDeleteREQ( userFileDeleteREQ ); ;
        this.userFileDeleteOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.userFileDeleteOPS.getResponse( );
    }

    @PostMapping( path = "userfiledirectoryupdate", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity< ? > userfiledirectoryupdate( @RequestBody UserFileDirectoryUpdateREQ userFileDirectoryUpdateREQ ) throws Exception {
        jamiiDebug.info("Received request" );

        this.userFileDirectoryUpdateOPS.reset( );
        this.userFileDirectoryUpdateOPS.setUserFileDirectoryUpdateREQ( userFileDirectoryUpdateREQ ); ;
        this.userFileDirectoryUpdateOPS.processRequest( );

        jamiiDebug.info("Request completed");
        return this.userFileDirectoryUpdateOPS.getResponse( );
    }




}
