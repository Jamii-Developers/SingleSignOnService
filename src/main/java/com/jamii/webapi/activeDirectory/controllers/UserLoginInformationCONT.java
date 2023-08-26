package com.jamii.webapi.activeDirectory.controllers;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.Utils.JamiiUserPasswordEncryptTool;
import com.jamii.webapi.activeDirectory.CreateNewUserOPS;
import com.jamii.webapi.activeDirectory.UserLoginOPS;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import com.jamii.webapi.jamiidb.repo.UserLoginREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserLoginInformationCONT {

    @Autowired
    private UserLoginREPO userLoginREPO;
    
    /**
     * Used to check the login validity of a request
     * @param userLoginOPS - This class checks for the User login validity
     * @return - Returns user's information
     */

    public UserLoginTBL checkAndRetrieveValidLogin ( UserLoginOPS userLoginOPS ){

        // Adding this so we are able to check only users that are active
        List <UserLoginTBL> fetchCredential = new ArrayList< > ( );

        fetchCredential.addAll( userLoginREPO.findByUsernameAndActive( userLoginOPS.getLoginCredential( ),  userLoginOPS.getActiveStatus( ) ) );
        fetchCredential.addAll( userLoginREPO.findByEmailaddressAndActive( userLoginOPS.getLoginCredential( ),  userLoginOPS.getActiveStatus( ) ) );

        if( fetchCredential.isEmpty( )  ){
            return null;
        }

        String encryptedPassword = new JamiiUserPasswordEncryptTool( userLoginOPS.getLoginPassword( ) ).encryptPassword( );
        for( UserLoginTBL cred : fetchCredential ){
            if( JamiiStringUtils.equals( cred.getPasswordsalt( ),  encryptedPassword ) ) {
                return cred;
            }
        }

        return null ;
    }

    /**
     * Used to create a new user
     * @param createNewUserOPS - This class creates the new user
     * @return - returns boolean on whether a new user was created
     */
    @Transactional
    public UserLoginTBL createNewUser (CreateNewUserOPS createNewUserOPS ){

        //Check if username and email address exist in the system
        List<UserLoginTBL> checkCredential = new ArrayList<>( userLoginREPO.findByEmailaddressOrUsername( createNewUserOPS.getEmailaddress( ), createNewUserOPS.getUsername( ) ) );

        if( !checkCredential.isEmpty( ) ){
            return null ;
        }

        //Save the newly created user
        UserLoginTBL newUser = add( createNewUserOPS);
        return newUser;
    }

    /**
     * Populates a new user to the userLoginTBL table
     * @param createNewUserOPS - Contains the basic details we need to populate the userLoginTBL
     * @return - returns the user login information
     */
    private UserLoginTBL add( CreateNewUserOPS createNewUserOPS ) {

        UserLoginTBL newUser = new UserLoginTBL( );
        newUser.setEmailAddress( createNewUserOPS.getEmailaddress( ) ) ;
        newUser.setUsername( createNewUserOPS.getUsername( ) );
        newUser.setPasswordsalt( new JamiiUserPasswordEncryptTool( createNewUserOPS.getPassword( ) ).encryptPassword( ) );
        newUser.setActive( UserLoginTBL.ACTIVE_ON ) ;

        return userLoginREPO.save( newUser) ;
    }

    /**
     * Fetches User using both username and email
     * @param username - Clients username
     * @param emailAddress - Clients email address
     * @return - returns the information on a fetched user
     */
    public Optional<UserLoginTBL> fetchUser(String username, String emailAddress ){
        return userLoginREPO.findByEmailaddressAndUsername( username, emailAddress ).stream( ).findFirst( );
    }
}
