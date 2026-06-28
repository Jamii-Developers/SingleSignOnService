package com.jamii.abstractClasses;

import com.jamii.jPublic.JPublicServices;
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

    private static final String ERROR_MESSAGE_GENERIC = "Oops! something went wrong with your request";
    private static final String ERROR_MESSAGE_OPERATION_NOT_FOUND = "Operation could not be found: ";

    protected final JamiiDebug jamiiDebug = new JamiiDebug(this.getClass());
    protected Map<String, Object> directoryMap = new HashMap<>();

    @Autowired protected JamiiLoggingUtils jamiiLoggingUtils;

    @PostConstruct
    protected abstract void initPathing();

    /**
     * Processes incoming user-specific service requests.
     *
     * <p>This method validates the request, routes it to the appropriate service handler
     * based on the {@code Service-Header}, and returns the response. It follows the same
     * pattern as {@link JPublicServices#processRequest} but for authenticated
     * user operations.
     *
     * @param operation the operation identifier from the Service-Header
     * @param jsonPayload the request payload as a JSON object
     * @return ResponseEntity containing the response data or error message
     */
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
            AbstractUserServicesOPS handler = (AbstractUserServicesOPS) directoryMap.get(operation);

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


    public ResponseEntity<?> processMultipartRequest(@PathVariable String requestType, @RequestHeader("Service-Header") String operation, @RequestBody(required = true) Object jsonPayload)
    {
        return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.FORBIDDEN);
    }
}
