package com.jamii;

import com.jamii.applicationControllers.PublicServices;
import com.jamii.applicationControllers.UserServices;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@SpringBootApplication
public class ApplicationStart {

    @Autowired
    PublicServices publicServices;
    @Autowired
    UserServices userServices;


    public static void main(String[ ] args ) {
        SpringApplication.run( ApplicationStart.class, args);
    }

    Map<String, Object > directoryMap = new HashMap< >( );

    @PostConstruct
    private void initGlobalPathing( ){
        directoryMap.put( "public", publicServices );
        directoryMap.put( "user", userServices );
    }

    @PostMapping(path = "{requestType}/{operation}")
    public ResponseEntity<?> processRequest(@PathVariable String requestType, @PathVariable String operation, @RequestBody Object payload) throws Exception {
        try{
            Object handler = directoryMap.get(requestType);

            if( handler instanceof PublicServices ){
                return ((PublicServices) handler).processRequest( operation, payload );
            }

            if ( handler instanceof UserServices ){
                return ( (UserServices) handler).processRequest( operation, payload );
            }

        }catch( Exception e ) {
            e.printStackTrace();
            return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
        }

        return null;
    }

}
