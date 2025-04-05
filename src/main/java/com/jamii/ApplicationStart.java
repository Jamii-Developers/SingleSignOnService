package com.jamii;

import com.jamii.Utils.JamiiLoggingUtils;
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


    public static void main(String[ ] args ) {SpringApplication.run( ApplicationStart.class, args); }

    Map<String, Object > directoryMap = new HashMap< >( );

    @PostConstruct
    private void initGlobalPathing( ){
        directoryMap.put( "public", publicServices );
        directoryMap.put( "user", userServices );
    }

    @PostMapping(path = "{requestType}/{operation}", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> processRequest( @PathVariable String requestType, @PathVariable String operation, @RequestBody(required = true) Object jsonPayload) throws Exception {
        try {
            Object handler = directoryMap.get(requestType);

            if (handler instanceof PublicServices) {
                return ((PublicServices) handler).processRequest(operation, jsonPayload);
            }

            if (handler instanceof UserServices) {
                return ( (UserServices) handler).processRequest(operation, jsonPayload);
            }

        } catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , null ) ;
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "{requestType}/{operation}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> processFileUploadRequest(
            @PathVariable String requestType,
            @PathVariable String operation,
            @RequestParam String userKey,
            @RequestParam String deviceKey,
            @RequestParam String sessionKey,
            @RequestParam(value = "uploadFile", required = true ) MultipartFile file) throws Exception {
        try {
            Object handler = directoryMap.get(requestType);

            if (handler instanceof PublicServices) {
                return ((PublicServices) handler).processMultipartRequest(operation, userKey, deviceKey, sessionKey, file );
            }

            if (handler instanceof UserServices) {
                return ((UserServices) handler).processMultipartRequest( operation, userKey, deviceKey, sessionKey, file);
            }

        } catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , null ) ;
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "{requestType}/{operation}/{filename}")
    public ResponseEntity<?> processFileDownloadRequest( @PathVariable String requestType, @PathVariable String operation, @RequestBody( required = true ) Object jsonPayload) throws Exception {
        try {
            Object handler = directoryMap.get(requestType);

            if (handler instanceof PublicServices) {
                return ((PublicServices) handler).processRequest(operation, jsonPayload);
            }

            if (handler instanceof UserServices) {
                return ((UserServices) handler).processRequest(operation, jsonPayload);
            }

        } catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , null ) ;
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }
}
