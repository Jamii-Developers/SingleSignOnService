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
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = {"https://jamiix.netlify.app", "http://localhost:3000"})
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

}
