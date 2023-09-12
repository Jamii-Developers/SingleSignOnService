package com.jamii;


import com.jamii.Utils.JamiiDebug;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;


/**
 * Configured to run on port 4000
 */
@CrossOrigin(origins = "*") // Apply to the entire controller
@SpringBootApplication
@RestController
public class FileManagementServices {

    private final JamiiDebug jamiiDebug = new JamiiDebug( );

    public static void main( String[ ] args ) {
        SpringApplication.run( FileManagementServices.class, args);
    }
}
