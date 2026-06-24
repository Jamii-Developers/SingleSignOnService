package com.jamii.administrative;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdministrativeServices
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
