package com.jamii.jUser;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import com.jamii.abstractClasses.AbstractPublicServices;
import com.jamii.jPublic.JPublicServices;
import com.jamii.jUser.services.ChangePasswordOPS;
import com.jamii.jUser.services.DeactivateUserOPS;
import com.jamii.jUser.services.EditUserDataOPS;
import com.jamii.jUser.services.FetchUserDataOPS;
import com.jamii.jUser.services.SearchUsersOPS;
import com.jamii.jUser.services.SessionValidator;
import com.jamii.jUser.services.UserLogoffOPS;
import com.jamii.jUser.services.ViewUserProfileOPS;
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

/**
 * REST controller for authenticated user-specific operations in the Jamii application.
 * 
 * <p>This controller handles operations that require user authentication, including:
 * <ul>
 *   <li>Profile management (edit, fetch, view)</li>
 *   <li>Password changes</li>
 *   <li>User deactivation</li>
 *   <li>Session management (validation, logoff)</li>
 *   <li>User search functionality</li>
 * </ul>
 * 
 * <p>Operations are routed based on the {@code Service-Header} HTTP header, which must
 * contain one of the supported operation keys. All endpoints require valid authentication
 * via session validation.
 * 
 * <p>Base path: {@code /juser/}
 * 
 * @see JPublicServices
 * @see AbstractPublicServices
 */
@RestController
@RequestMapping("/juser/")
@CrossOrigin(origins = "*")
public class JUserServices
        extends AbstractApplicationControllers
{

    private static final String OPERATION_CHANGE_PASSWORD = "chngpassword";
    private static final String OPERATION_EDIT_PROFILE = "editprofile";
    private static final String OPERATION_FETCH_PROFILE = "fetchprofile";
    private static final String OPERATION_DEACTIVATE_USER = "deactivateuser";
    private static final String OPERATION_USER_LOGOFF = "userlogoff";
    private static final String OPERATION_VALIDATE_SESSION = "validate-session";
    private static final String OPERATION_SEARCH_USER = "searchuser";
    private static final String OPERATION_VIEW_USER_PROFILE = "viewuserprofile";
    private static final String ERROR_MESSAGE_GENERIC = "Oops! something went wrong with your request";
    private static final String ERROR_MESSAGE_OPERATION_NOT_FOUND = "Operation could not be found: ";

    /** Service for editing user profile data */
    @Autowired EditUserDataOPS editUserDataOPS;
    
    /** Service for fetching user profile data */
    @Autowired FetchUserDataOPS fetchUserDataOPS;
    
    /** Service for deactivating user accounts */
    @Autowired DeactivateUserOPS deactivateUserOPS;
    
    /** Service for changing user passwords */
    @Autowired ChangePasswordOPS changePasswordOPS;
    
    /** Service for user logoff operations */
    @Autowired UserLogoffOPS userLogoffOPS;
    
    /** Service for session validation */
    @Autowired SessionValidator sessionValidator;
    
    /** Service for searching users */
    @Autowired SearchUsersOPS searchUsersOPS;
    
    /** Service for viewing user profiles */
    @Autowired ViewUserProfileOPS viewUserProfileOPS;

    /**
     * Initializes the operation directory map after bean construction.
     * 
     * <p>This method maps operation string keys to their corresponding service handlers.
     * The map is used by {@link #processRequest(String, Object)} to route incoming
     * requests to the appropriate service implementation.
     * 
     * <p>Supported operations:
     * <ul>
     *   <li>{@code chngpassword} - Change user password</li>
     *   <li>{@code editprofile} - Edit user profile</li>
     *   <li>{@code fetchprofile} - Fetch user profile</li>
     *   <li>{@code deactivateuser} - Deactivate user account</li>
     *   <li>{@code userlogoff} - Log off user session</li>
     *   <li>{@code validate-session} - Validate user session</li>
     *   <li>{@code searchuser} - Search for users</li>
     *   <li>{@code viewuserprofile} - View user profile</li>
     * </ul>
     */
    @PostConstruct
    protected void initPathing()
    {
        directoryMap.put(OPERATION_CHANGE_PASSWORD, changePasswordOPS);
        directoryMap.put(OPERATION_EDIT_PROFILE, editUserDataOPS);
        directoryMap.put(OPERATION_FETCH_PROFILE, fetchUserDataOPS);
        directoryMap.put(OPERATION_DEACTIVATE_USER, deactivateUserOPS);
        directoryMap.put(OPERATION_USER_LOGOFF, userLogoffOPS);
        directoryMap.put(OPERATION_VALIDATE_SESSION, sessionValidator);
        directoryMap.put(OPERATION_SEARCH_USER, searchUsersOPS);
        directoryMap.put(OPERATION_VIEW_USER_PROFILE, viewUserProfileOPS);
    }

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
    public ResponseEntity<?> processRequest(@RequestHeader("Service-Header") String operation, @RequestBody Object jsonPayload)
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
