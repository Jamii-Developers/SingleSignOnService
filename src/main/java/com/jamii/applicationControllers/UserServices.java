package com.jamii.applicationControllers;

import com.jamii.Utils.JamiiDebug;
import com.jamii.operations.userServices.clientCommunication.ReviewUs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServices {

    @Autowired
    ReviewUs reviewUs;

    private final JamiiDebug jamiiDebug = new JamiiDebug( this.getClass() );
    private final Map<String, Object> directoryMap = new HashMap<>();

    @PostConstruct
    private void initPathing() {
        directoryMap.put("reviewus", reviewUs );

    }


    public ResponseEntity<?> processRequest( String operation, Object requestPayload ) throws Exception{

        jamiiDebug.info("Received request for operation: " + operation);

        // Lookup the handler
        Object handler = directoryMap.get(operation);

        if ( handler instanceof ReviewUs){
            ( (ReviewUs) handler).reset( );
            return ( (ReviewUs) handler).run( requestPayload );
        }

        throw  new Exception( operation );
    }
}
