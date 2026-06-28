package com.jamii.jPublic;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import com.jamii.abstractClasses.AbstractPublicServices;
import com.jamii.jPublic.services.CreateNewUserOPS;
import com.jamii.jPublic.services.ReactivateUserOPS;
import com.jamii.jPublic.services.UserLoginOPS;
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
@RequestMapping("/jpublic/")
@CrossOrigin(origins = "*")
public class JPublicServices
        extends AbstractApplicationControllers
{

    private static final String OPERATION_CREATE_NEW_USER = "createnewuser";
    private static final String OPERATION_USER_LOGIN = "userlogin";
    private static final String OPERATION_REACTIVATE_USER = "reactivateuser";
    private static final String ERROR_MESSAGE_GENERIC = "Oops! something went wrong with your request";
    private static final String ERROR_MESSAGE_OPERATION_NOT_FOUND = "Operation could not be found: ";

    @Autowired CreateNewUserOPS createNewUserOPS;
    @Autowired UserLoginOPS userLoginOPS;
    @Autowired ReactivateUserOPS reactivateUserOPS;

    @PostConstruct
    protected void initPathing()
    {
        directoryMap.put(OPERATION_CREATE_NEW_USER, createNewUserOPS);
        directoryMap.put(OPERATION_USER_LOGIN, userLoginOPS);
        directoryMap.put(OPERATION_REACTIVATE_USER, reactivateUserOPS);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processRequest( @RequestHeader("Service-Header") String operation, @RequestBody Object jsonPayload)
    {

        // Validate operation parameter
        if (operation == null || operation.trim().isEmpty()) {
            jamiiDebug.warning("Service-Header is null or empty");
            return new ResponseEntity<>("Service-Header is required", HttpStatus.BAD_REQUEST);
        }

        // Validate jsonPayload
        if (jsonPayload == null) {
            jamiiDebug.warning("Request body is null for operation: " + operation);
            return new ResponseEntity<>("Request body is required", HttpStatus.BAD_REQUEST);
        }

        try {
            jamiiDebug.info("Received request for operation: " + operation);

            // Lookup the handler
            AbstractPublicServices handler = (AbstractPublicServices) directoryMap.get(operation);

            if (handler == null) {
                jamiiDebug.warning("Unknown Service-Header: " + operation);
                return new ResponseEntity<>(ERROR_MESSAGE_OPERATION_NOT_FOUND + operation, HttpStatus.NOT_FOUND);
            }

            handler.reset();
            return handler.run(jsonPayload);
        }
        catch (IllegalArgumentException e) {
            jamiiLoggingUtils.ExceptionLogger(this.getClass().getName(), e, null);
            return new ResponseEntity<>("Invalid request parameters: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger(this.getClass().getName(), e, null);
            return new ResponseEntity<>(ERROR_MESSAGE_GENERIC, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
