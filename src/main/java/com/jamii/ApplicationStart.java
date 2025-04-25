package com.jamii;

import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiLoggingUtils;
import com.jamii.applicationControllers.AbstractApplicationControllers;
import com.jamii.applicationControllers.HealthCheck;
import com.jamii.applicationControllers.PublicServices;
import com.jamii.applicationControllers.UserServices;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "*")
@SpringBootApplication
public class ApplicationStart {

    @Autowired
    JamiiLoggingUtils jamiiLoggingUtils;
    @Autowired
    PublicServices publicServices;
    @Autowired
    UserServices userServices;
    @Autowired
    HealthCheck healthCheck;

    protected final JamiiDebug jamiiDebug = new JamiiDebug( this.getClass( ) );


    public static void main(String[ ] args ) {SpringApplication.run( ApplicationStart.class, args); }

    Map<String, AbstractApplicationControllers > directoryMap = new HashMap< >( );

    @PostConstruct
    private void initGlobalPathing( ){
        directoryMap.put( "public", publicServices );
        directoryMap.put( "user", userServices );
    }

    @PostMapping(path = "/{requestType}", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> processRequest( @PathVariable String requestType,@RequestHeader("Service-Header") String operation, @RequestBody Object jsonPayload) {
        try {
            jamiiDebug.info("Received request for operation: " + requestType);

            // Lookup the handler
            AbstractApplicationControllers handler = directoryMap.get(requestType);

            if (handler == null) {
                jamiiDebug.warn("Unknown operation: " + operation);
                throw new Exception( "Operation could not be found " + operation );
            }

            return handler.processJSONRequest( operation, jsonPayload );

        } catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , null ) ;
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "{requestType}/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> processFileUploadRequest(
            @PathVariable String requestType,
            @RequestHeader("Service-Header") String operation,
            @RequestParam String userKey,
            @RequestParam String deviceKey,
            @RequestParam String sessionKey,
            @RequestParam(value = "uploadFile" ) MultipartFile file) {
        try {
            AbstractApplicationControllers handler = directoryMap.get(requestType);

            if (handler == null) {
                jamiiDebug.warn("Unknown operation: " + requestType);
                throw new Exception( "Operation could not be found " + requestType );
            }

            return handler.processMultipartRequest( operation, userKey, deviceKey, sessionKey, file);

        } catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , null ) ;
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }

    @GetMapping( path = "{requestType}/{filename}")
    public ResponseEntity<?> processFileDownloadRequest( @PathVariable String requestType, @RequestHeader("Service-Header") String operation, @RequestBody( required = true ) Object jsonPayload) {
        try {
            AbstractApplicationControllers handler = directoryMap.get(requestType);

            if (handler == null) {
                jamiiDebug.warn("Unknown operation: " + requestType);
                throw new Exception( "Operation could not be found " + requestType );
            }

            return handler.processJSONRequest( operation, jsonPayload );

        } catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , null ) ;
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }

    @PostMapping( path = "health" )
    public ResponseEntity<?> processHealthRequestCheck( ) {
        try {
            jamiiDebug.info("Received request for operation: health ");

            return healthCheck.processJSONRequest( null, null );

        } catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , null ) ;
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }
}
