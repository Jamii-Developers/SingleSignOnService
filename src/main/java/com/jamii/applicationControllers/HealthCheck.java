package com.jamii.applicationControllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HealthCheck
        extends AbstractApplicationControllers
{

    @Override
    protected void initPathing() {}

    @Override
    public ResponseEntity<?> processJSONRequest(String operation, Object requestPayload)
    {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> processMultipartRequest(String operation, String userKey, String deviceKey, String sessionKey, MultipartFile file)
    {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
