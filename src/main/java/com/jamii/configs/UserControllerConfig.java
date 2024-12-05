package com.jamii.configs;

import com.jamii.applicationControllers.publicControllers.PublicServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserControllerConfig {

    public static void main( String[ ] args ) {
        SpringApplication.run( UserControllerConfig.class, args);
    }

    private Map<String, Object> pathing( ){
        Map<String, Object > publicDirectoryMap = new HashMap< >( );
        publicDirectoryMap.put( "public", new PublicServices( ) );
        return publicDirectoryMap;
    }

    @PostMapping(path = "{requestType}/{operation}")
    public ResponseEntity<?> processRequest(@PathVariable String requestType, @PathVariable String operation , @RequestBody Object payload) throws Exception {

        Object handler = pathing( ).get(requestType);

        if( handler instanceof PublicServices ){
            return ((PublicServices) handler).processRequest( operation, payload );
        }

        return new ResponseEntity<>("This is not a valid request", HttpStatus.BAD_REQUEST);
    }

}
