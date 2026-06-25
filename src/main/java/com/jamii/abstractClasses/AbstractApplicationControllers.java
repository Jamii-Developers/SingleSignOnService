package com.jamii.abstractClasses;

import com.jamii.utils.JamiiDebug;
import com.jamii.utils.JamiiLoggingUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractApplicationControllers
{

    protected final JamiiDebug jamiiDebug = new JamiiDebug(this.getClass());
    protected Map<String, Object> directoryMap = new HashMap<>();

    @Autowired protected JamiiLoggingUtils jamiiLoggingUtils;

    @PostConstruct
    protected abstract void initPathing();

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processRequest( @RequestHeader("Service-Header") String operation, @RequestBody Object jsonPayload)
    {

        try {
            jamiiDebug.info("Received request for operation: " + operation);

            // Lookup the handler
            AbstractUserServicesOPS handler = (AbstractUserServicesOPS) directoryMap.get(operation);

            if (handler == null) {
                jamiiDebug.warn("Unknown Service-Header : " + operation);
                throw new Exception("Operation could not be found " + operation);
            }

            handler.reset();
            return handler.run(jsonPayload);
        }
        catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger(this.getClass().getName(), e, null);
        }

        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<?> processMultipartRequest(@PathVariable String requestType, @RequestHeader("Service-Header") String operation, @RequestBody(required = true) Object jsonPayload)
    {
        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.FORBIDDEN);
    }
}
