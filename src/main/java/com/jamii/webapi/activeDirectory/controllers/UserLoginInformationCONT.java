package com.jamii.webapi.activeDirectory.controllers;

import com.jamii.Utils.JamiiConstants;
import com.jamii.Utils.JamiiStringUtils;
import com.jamii.webapi.activeDirectory.CreateNewUserOPS;
import com.jamii.webapi.activeDirectory.UserLoginOPS;
import com.jamii.webapi.jamiidb.model.UserLoginInformationTBL;
import com.jamii.webapi.jamiidb.repo.UserInformationLoginREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserLoginInformationCONT {
    @Autowired
    private UserInformationLoginREPO userInformationLoginREPO;

    /**
     * Used to check the login validity of a request
     * @param userLoginOPS - This class checks for the User login validity
     * @return - Returns user's information
     */

    public UserLoginInformationTBL checkAndRetrieveValidLogin ( UserLoginOPS userLoginOPS ){

        // Adding this so we are able to check only users that are active
        List <UserLoginInformationTBL> fetchCredential = new ArrayList< > ( );

        fetchCredential.addAll( userInformationLoginREPO.findByUsernameAndActive( userLoginOPS.getLoginCredential( ),  userLoginOPS.getActiveStatus( ) ) );
        fetchCredential.addAll( userInformationLoginREPO.findByEmailaddressAndActive( userLoginOPS.getLoginCredential( ),  userLoginOPS.getActiveStatus( ) ) );

        if( fetchCredential.isEmpty( )  ){

            return null;
        }


        for( UserLoginInformationTBL cred : fetchCredential ){
            if( JamiiStringUtils.equals( cred.getPasswordsalt( ), userLoginOPS.getLoginPassword( ) ) ) {
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
    public Boolean createNewUser ( CreateNewUserOPS createNewUserOPS ){

        //Check if username and email address exist in the system
        List <UserLoginInformationTBL> checkCredential = new ArrayList< > ( );
        checkCredential.addAll( userInformationLoginREPO.findByEmailaddressOrUsername( createNewUserOPS.getEmailaddress( ), createNewUserOPS.getUsername( ) ) );

        if( !checkCredential.isEmpty( ) ){
            return JamiiConstants.PROCESS_STATUS_FAILED ;
        }

        //Save the newly created user
        UserLoginInformationTBL newUser = new UserLoginInformationTBL( );

        newUser.setEmailAddress( createNewUserOPS.getEmailaddress( ) ) ;
        newUser.setUsername( createNewUserOPS.getUsername( ) );
        newUser.setLastname( createNewUserOPS.getLastname( ) );
        newUser.setFirstname( createNewUserOPS.getFirstname( ) );
        newUser.setPasswordsalt( createNewUserOPS.getPassword( ) );
        newUser.setActive( UserLoginInformationTBL.ACTIVE_ON ) ;

        userInformationLoginREPO.save( newUser );

        return JamiiConstants.PROCESS_STATUS_SUCCESSFUL ;
    }

    /**
     * Fetches User using both username and email
     * @param username - Clients username
     * @param emailAddress - Clients email address
     * @return - returns the information on a fetched user
     */
    public UserLoginInformationTBL fetchUser( String username, String emailAddress ){
        List <UserLoginInformationTBL> userList = userInformationLoginREPO.findByEmailaddressAndUsername( username, emailAddress );
        UserLoginInformationTBL user = userList.get(0);
        return user;
    }
}
