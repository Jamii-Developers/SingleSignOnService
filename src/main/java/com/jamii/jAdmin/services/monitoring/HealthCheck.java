package com.jamii.jAdmin.services.monitoring;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class HealthCheck
        extends AbstractApplicationControllers
{

    @Override
    protected void initPathing() {}

    @RequestMapping(path = "monitor-health", method = RequestMethod.HEAD)
    public ResponseEntity<?> processMonitorHealthRequest()
    {
        jamiiDebug.info("Received request for operation: monitor-health ");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "health")
    public ResponseEntity<?> processHealthRequest( )
    {
        jamiiDebug.info("Received request for operation: health ");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
