package com.jamii.databaseconfig.controllers;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.Utils.JamiiUserPasswordEncryptTool;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.databaseconfig.repo.UserLoginREPO;
import com.jamii.requests.publicServices.CreateNewUserREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserLogin
{

    /**
     * ACTIVE STATUSES
     */
    public static final Integer ACTIVE_OFF = 0;
    public static final Integer ACTIVE_ON = 1;
    public static final Integer ACTIVE_TERMINATED = 2;
    /**
     * PRIVACY STATUSES
     */

    public static final Integer PRIVACY_OFF = 0;
    public static final Integer PRIVACY_ON = 1;

    //Creating a table object to reference when creating data for that table
    public UserLoginTBL data = new UserLoginTBL();
    public UserLoginTBL otherUser = new UserLoginTBL();
    public ArrayList<UserLoginTBL> dataList = new ArrayList<>();

    @Autowired private UserLoginREPO userLoginREPO;

    /**
     * Checks the database  user_login table for a valid login information
     *
     * @param logincredential
     * @param password
     * @return
     */

    public UserLoginTBL checkAndRetrieveValidLogin(String logincredential, String password)
    {

        // Adding this so we are able to check only users that are active
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
     * Checks if email address or username exists in the database
     *
     * @param emailaddress
     * @param username
     * @return
     */
    public boolean checkifUserExists(String emailaddress, String username)
    {
        List<UserLoginTBL> checkCredential = new ArrayList<>(userLoginREPO.findByEmailaddressOrUsername(emailaddress, username));
        return !checkCredential.isEmpty();
    }

    /**
     * Used to create a new user
     *
     * @param createNewUserREQ - This class creates the new user
     * @return - returns boolean on whether a new user was created
     */
    @Deprecated
    public UserLoginTBL createNewUser(CreateNewUserREQ createNewUserREQ)
    {
        //Save the newly created user
        return add(createNewUserREQ);
    }

    public void add(UserLoginTBL userLoginTBL)
    {
        userLoginREPO.save(userLoginTBL);
    }

    /**
     * Populates a new user to the userLoginTBL table
     *
     * @param createNewUserREQ - Contains the basic details we need to populate the userLoginTBL
     * @return - returns the user login information
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
     * Fetches User using both username and email
     *
     * @param username - Clients username
     * @param emailAddress - Clients email address
     * @return - returns the information on a fetched user
     */
    public Optional<UserLoginTBL> fetch(String emailAddress, String username)
    {
        return userLoginREPO.findByEmailaddressAndUsername(emailAddress, username).stream().findFirst();
    }

    /**
     * This is an overloaded method that checks if a user is active
     *
     * @param username - Intakes the username
     * @param emailAddress - intakes the email address
     * @param active - Intakes the user's active state
     * @return - returns the user record
     */
    public Optional<UserLoginTBL> fetch(String emailAddress, String username, int active)
    {
        return userLoginREPO.findByEmailaddressAndUsernameAndActive(emailAddress, username, active).stream().findFirst();
    }

    public Optional<UserLoginTBL> fetch(String emailAddress, String username, String userkey, int active)
    {
        return userLoginREPO.findByEmailaddressAndUsernameAndUserkeyAndActive(emailAddress, username, userkey, active).stream().findFirst();
    }

    public Optional<UserLoginTBL> fetchByUserKey(String userkey, int active)
    {
        return userLoginREPO.findByUserkeyAndActive(userkey, active).stream().findFirst();
    }

    public Optional<UserLoginTBL> fetch(int id, int active)
    {
        return userLoginREPO.findById(id).stream().findFirst();
    }

    public void update(UserLoginTBL userLoginTBL)
    {
        userLoginREPO.save(userLoginTBL);
    }

    public Boolean isPasswordValid(String password, UserLoginTBL user)
    {
        String encryptedPassword = JamiiUserPasswordEncryptTool.encryptPassword(password);
        return JamiiStringUtils.equals(encryptedPassword, user.getPasswordsalt());
    }

    public void deactivateUser(UserLoginTBL userLoginTBL)
    {
        userLoginTBL.setActive(ACTIVE_OFF);
        userLoginREPO.save(userLoginTBL);
    }

    public void reactivateUser(UserLoginTBL userLoginTBL)
    {
        userLoginTBL.setActive(ACTIVE_ON);
        userLoginREPO.save(userLoginTBL);
    }

    public List<UserLoginTBL> searchUserUsername(String searchString)
    {
        return userLoginREPO.findByUsernameStartingWithAndActive(searchString, ACTIVE_ON);
    }

    public List<UserLoginTBL> searchUserEmailAddress(String searchString)
    {
        return userLoginREPO.findByEmailaddressStartingWithAndActive(searchString, ACTIVE_ON);
    }

    public void save()
    {
        data = this.userLoginREPO.save(data);
    }

    public void saveAll()
    {
        Iterable<UserLoginTBL> datalist = this.userLoginREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
