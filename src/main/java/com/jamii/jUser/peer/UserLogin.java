package com.jamii.jUser.peer;

import com.jamii.utils.JamiiStringUtils;
import com.jamii.utils.JamiiUserPasswordEncryptTool;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jUser.repo.UserLoginREPO;
import com.jamii.jPublic.requests.CreateNewUserREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service component for managing user login/account records in the database.
 * 
 * <p>This class handles user authentication, account creation, and user management operations.
 * It supports login validation, user registration, account activation/deactivation, and user search functionality.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Validate user login credentials</li>
 *     <li>Create new user accounts</li>
 *     <li>Fetch user information by various criteria</li>
 *     <li>Activate/deactivate user accounts</li>
 *     <li>Search jUser by username or email</li>
 *     <li>Save user login records individually or in batch</li>
 * </ul>
 */
@Component
public class UserLogin
{

    /**
     * User active status constant for inactive accounts.
     */
    public static final Integer ACTIVE_OFF = 0;
    
    /**
     * User active status constant for active accounts.
     */
    public static final Integer ACTIVE_ON = 1;
    
    /**
     * User active status constant for terminated accounts.
     */
    public static final Integer ACTIVE_TERMINATED = 2;
    
    /**
     * User privacy status constant for public profiles.
     */
    public static final Integer PRIVACY_OFF = 0;
    
    /**
     * User privacy status constant for private profiles.
     */
    public static final Integer PRIVACY_ON = 1;

    /**
     * Single user login record instance for creating new entries.
     */
    public UserLoginTBL data;
    
    /**
     * Additional user login record instance for comparisons.
     */
    public UserLoginTBL otherUser;
    
    /**
     * List of user login records for batch operations.
     */
    public ArrayList<UserLoginTBL> dataList;

    @Autowired private UserLoginREPO userLoginREPO;

    /**
     * Checks the database for valid login credentials.
     * 
     * <p>This method searches for a user by username or email address, validates that the account is active,
     * and verifies that the provided password matches the stored password hash.</p>
     * 
     * @param logincredential the username or email address to authenticate with
     * @param password the password to validate
     * @return the user login record if authentication succeeds, null otherwise
     */
    public UserLoginTBL checkAndRetrieveValidLogin(String logincredential, String password)
    {

        // Adding this so we are able to check only jUser that are active
        List<UserLoginTBL> fetchCredential = new ArrayList<>();

        fetchCredential.addAll(userLoginREPO.findByUsernameAndActive(logincredential, ACTIVE_ON));
        fetchCredential.addAll(userLoginREPO.findByEmailaddressAndActive(logincredential, ACTIVE_ON));

        if (fetchCredential.isEmpty()) {
            return null;
        }

        //Confirm if user password matches saved password
        for (UserLoginTBL cred : fetchCredential) {
            if (isPasswordValid(password, cred)) {
                return cred;
            }
        }
        return null;
    }

    /**
     * Checks if an email address or username already exists in the database.
     * 
     * @param emailaddress the email address to check
     * @param username the username to check
     * @return {@code true} if the email or username exists, {@code false} otherwise
     */
    public boolean checkifUserExists(String emailaddress, String username)
    {
        List<UserLoginTBL> checkCredential = new ArrayList<>(userLoginREPO.findByEmailaddressOrUsername(emailaddress, username));
        return !checkCredential.isEmpty();
    }

    /**
     * Creates a new user account.
     * 
     * @param createNewUserREQ the request object containing user creation details
     * @return the created user login record with generated ID
     * @deprecated Use {@link #add(CreateNewUserREQ)} instead
     */
    @Deprecated
    public UserLoginTBL createNewUser(CreateNewUserREQ createNewUserREQ)
    {
        //Save the newly created user
        return add(createNewUserREQ);
    }

    /**
     * Saves a user login record to the database.
     * 
     * @param userLoginTBL the user login record to save
     */
    public void add(UserLoginTBL userLoginTBL)
    {
        userLoginREPO.save(userLoginTBL);
    }

    /**
     * Creates and saves a new user account from a request object.
     * 
     * <p>This method populates a new user login record with the provided details,
     * encrypts the password, generates a unique user key, and saves the record to the database.</p>
     * 
     * @param createNewUserREQ the request object containing user creation details
     * @return the saved user login record with generated ID
     * @deprecated This method should be replaced with a more modern approach
     */
    @Deprecated
    public UserLoginTBL add(CreateNewUserREQ createNewUserREQ)
    {

        UserLoginTBL user = new UserLoginTBL();
        user.setEmailAddress(createNewUserREQ.getEmailaddress());
        user.setUsername(createNewUserREQ.getUsername());
        user.setPasswordsalt(JamiiUserPasswordEncryptTool.encryptPassword(createNewUserREQ.getPassword()));
        user.setActive(ACTIVE_ON);
        user.setPrivacy(0);

        LocalDateTime dateCreated = LocalDateTime.now();
        user.setDatecreated(dateCreated);

        String userKey = JamiiUserPasswordEncryptTool.generateUserKey(user.getUsername(), user.getEmailaddress(), user.getDatecreated().toString());
        user.setUserKey(userKey);

        return userLoginREPO.save(user);
    }

    /**
     * Fetches a user by email address and username.
     * 
     * @param emailAddress the email address to search for
     * @param username the username to search for
     * @return an Optional containing the user login record if found, empty otherwise
     */
    public Optional<UserLoginTBL> fetch(String emailAddress, String username)
    {
        return userLoginREPO.findByEmailaddressAndUsername(emailAddress, username).stream().findFirst();
    }

    /**
     * Fetches a user by email address, username, and active status.
     * 
     * @param emailAddress the email address to search for
     * @param username the username to search for
     * @param active the active status to filter by
     * @return an Optional containing the user login record if found, empty otherwise
     */
    public Optional<UserLoginTBL> fetch(String emailAddress, String username, int active)
    {
        return userLoginREPO.findByEmailaddressAndUsernameAndActive(emailAddress, username, active).stream().findFirst();
    }

    /**
     * Fetches a user by email address, username, user key, and active status.
     * 
     * @param emailAddress the email address to search for
     * @param username the username to search for
     * @param userkey the user key to search for
     * @param active the active status to filter by
     * @return an Optional containing the user login record if found, empty otherwise
     */
    public Optional<UserLoginTBL> fetch(String emailAddress, String username, String userkey, int active)
    {
        return userLoginREPO.findByEmailaddressAndUsernameAndUserkeyAndActive(emailAddress, username, userkey, active).stream().findFirst();
    }

    /**
     * Fetches a user by user key and active status.
     * 
     * @param userkey the user key to search for
     * @param active the active status to filter by
     * @return an Optional containing the user login record if found, empty otherwise
     */
    public Optional<UserLoginTBL> fetchByUserKey(String userkey, int active)
    {
        return userLoginREPO.findByUserkeyAndActive(userkey, active).stream().findFirst();
    }

    /**
     * Fetches a user by ID and active status.
     * 
     * @param id the user ID to search for
     * @param active the active status to filter by (currently unused)
     * @return an Optional containing the user login record if found, empty otherwise
     */
    public Optional<UserLoginTBL> fetch(int id, int active)
    {
        return userLoginREPO.findById(id).stream().findFirst();
    }

    /**
     * Updates a user login record in the database.
     * 
     * @param userLoginTBL the user login record to update
     */
    public void update(UserLoginTBL userLoginTBL)
    {
        userLoginREPO.save(userLoginTBL);
    }

    /**
     * Validates if a password matches the stored password hash for a user.
     * 
     * @param password the plain text password to validate
     * @param user the user record containing the stored password hash
     * @return {@code true} if the password is valid, {@code false} otherwise
     */
    public Boolean isPasswordValid(String password, UserLoginTBL user)
    {
        String encryptedPassword = JamiiUserPasswordEncryptTool.encryptPassword(password);
        return JamiiStringUtils.equals(encryptedPassword, user.getPasswordsalt());
    }

    /**
     * Deactivates a user account.
     * 
     * @param userLoginTBL the user account to deactivate
     */
    public void deactivateUser(UserLoginTBL userLoginTBL)
    {
        userLoginTBL.setActive(ACTIVE_OFF);
        userLoginREPO.save(userLoginTBL);
    }

    /**
     * Reactivates a previously deactivated user account.
     * 
     * @param userLoginTBL the user account to reactivate
     */
    public void reactivateUser(UserLoginTBL userLoginTBL)
    {
        userLoginTBL.setActive(ACTIVE_ON);
        userLoginREPO.save(userLoginTBL);
    }

    /**
     * Searches for active jUser by username prefix.
     * 
     * @param searchString the prefix to search for in usernames
     * @return list of active jUser whose username starts with the search string
     */
    public List<UserLoginTBL> searchUserUsername(String searchString)
    {
        return userLoginREPO.findByUsernameStartingWithAndActive(searchString, ACTIVE_ON);
    }

    /**
     * Searches for active jUser by email address prefix.
     * 
     * @param searchString the prefix to search for in email addresses
     * @return list of active jUser whose email address starts with the search string
     */
    public List<UserLoginTBL> searchUserEmailAddress(String searchString)
    {
        return userLoginREPO.findByEmailaddressStartingWithAndActive(searchString, ACTIVE_ON);
    }

    /**
     * Saves the current user login record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = this.userLoginREPO.save(data);
    }

    /**
     * Saves all user login records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<UserLoginTBL> datalist = this.userLoginREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
