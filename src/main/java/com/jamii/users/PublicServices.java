package com.jamii.users;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import com.jamii.abstractClasses.AbstractPublicServices;
import com.jamii.users.services.CreateNewUserOPS;
import com.jamii.users.services.ReactivateUserOPS;
import com.jamii.users.services.UserLoginOPS;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/")
@CrossOrigin(origins = "*")
public class PublicServices
        extends AbstractApplicationControllers
{

    @Autowired CreateNewUserOPS createNewUserOPS;
    @Autowired UserLoginOPS userLoginOPS;
    @Autowired ReactivateUserOPS reactivateUserOPS;

    @PostConstruct
    protected void initPathing()
    {
        directoryMap.put("createnewuser", createNewUserOPS);
        directoryMap.put("userlogin", userLoginOPS);
        directoryMap.put("reactivateuser", reactivateUserOPS);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processRequest( @RequestHeader("Service-Header") String operation, @RequestBody Object jsonPayload)
    {

        try {
            jamiiDebug.info("Received request for operation: " + operation);

            // Lookup the handler
            AbstractPublicServices handler = (AbstractPublicServices) directoryMap.get(operation);

            if (handler == null) {
                jamiiDebug.warn("Unknown Service-Header: " + operation);
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

}
