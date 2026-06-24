package com.jamii.social;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/social")
@CrossOrigin(origins = "*")
public class SocialServices
    extends AbstractApplicationControllers
{
    @Override
    protected void initPathing()
    {

    }

    @Override
    public ResponseEntity<?> processJSONRequest(String operation, Object requestPayload)
    {
        return null;
    }

    @Override
    public ResponseEntity<?> processMultipartRequest(String operation, String userKey, String deviceKey, String sessionKey, MultipartFile file)
    {
        return null;
    }
}
