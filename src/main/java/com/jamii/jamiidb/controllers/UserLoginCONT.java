package com.jamii.jamiidb.controllers;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.Utils.JamiiUserPasswordEncryptTool;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.UserLoginREPO;
import com.jamii.services.singleSignOn.CreateNewUserOPS;
import com.jamii.services.singleSignOn.UserLoginOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserLoginCONT {

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

        fetchCredential.addAll( userLoginREPO.findByUsernameAndActive( userLoginOPS.getUserLoginREQ( ).getLoginCredential( ),  userLoginOPS.getUserLoginREQ( ).getActiveStatus( ) ) );
        fetchCredential.addAll( userLoginREPO.findByEmailaddressAndActive( userLoginOPS.getUserLoginREQ().getLoginCredential( ),  userLoginOPS.getUserLoginREQ().getActiveStatus( ) ) );

        if( fetchCredential.isEmpty( )  ){
            return null;
        }

        //Confirm if user password matches saved password
        for( UserLoginTBL cred : fetchCredential ){
            if( isPasswordValid( userLoginOPS.getUserLoginREQ( ).getLoginPassword( ), cred ) ) {
                return cred;
            }
        }
        return null ;
    }

    public boolean checkifUserExists( CreateNewUserOPS createNewUserOPS ){
        //Check if username and email address exist in the system
        List<UserLoginTBL> checkCredential = new ArrayList<>( userLoginREPO.findByEmailaddressOrUsername( createNewUserOPS.getCreateNewUserREQ( ).getEmailaddress( ), createNewUserOPS.getCreateNewUserREQ( ).getUsername( ) ) );

        return !checkCredential.isEmpty( );
    }

    /**
     * Used to create a new user
     * @param createNewUserOPS - This class creates the new user
     * @return - returns boolean on whether a new user was created
     */
    @Transactional
    public UserLoginTBL createNewUser ( CreateNewUserOPS createNewUserOPS ){



        //Save the newly created user
        return add( createNewUserOPS );
    }

    public void add( UserLoginTBL userLoginTBL ){
        userLoginREPO.save(userLoginTBL);
    }

    /**
     * Populates a new user to the userLoginTBL table
     * @param createNewUserOPS - Contains the basic details we need to populate the userLoginTBL
     * @return - returns the user login information
     */
    public UserLoginTBL add( CreateNewUserOPS createNewUserOPS ) {

        UserLoginTBL newUser = new UserLoginTBL( );
        newUser.setEmailAddress( createNewUserOPS.getCreateNewUserREQ( ).getEmailaddress( ) ) ;
        newUser.setUsername( createNewUserOPS.getCreateNewUserREQ( ).getUsername( ) );
        newUser.setPasswordsalt( JamiiUserPasswordEncryptTool.encryptPassword( createNewUserOPS.getCreateNewUserREQ( ).getPassword( ) ) );
        newUser.setActive( UserLoginTBL.ACTIVE_ON ) ;

        LocalDateTime dateCreated = LocalDateTime.now( );
        newUser.setDatecreated( dateCreated );

        String userKey = JamiiUserPasswordEncryptTool.generateUserKey( newUser.getUsername( ),newUser.getEmailaddress( ), newUser.getDatecreated( ).toString( ) );
        newUser.setUserKey( userKey );

        return userLoginREPO.save( newUser ) ;
    }

    /**
     * Fetches User using both username and email
     * @param username - Clients username
     * @param emailAddress - Clients email address
     * @return - returns the information on a fetched user
     */
    public Optional<UserLoginTBL> fetch( String emailAddress ,String username ){
        return userLoginREPO.findByEmailaddressAndUsername( emailAddress , username ).stream( ).findFirst( );
    }

    /**
     * This is an overloaded method that checks if a user is active
     * @param username - Intakes the username
     * @param emailAddress - intakes the email address
     * @param active - Intakes the user's active state
     * @return - returns the user record
     */
    public Optional<UserLoginTBL> fetch( String emailAddress ,String username, int active ){
        return userLoginREPO.findByEmailaddressAndUsernameAndActive( emailAddress, username, active ).stream( ).findFirst( );
    }
    public Optional<UserLoginTBL> fetch( String emailAddress ,String username, String userkey ,int active ){
        return userLoginREPO.findByEmailaddressAndUsernameAndUserkeyAndActive( emailAddress, username, userkey, active ).stream( ).findFirst( );
    }

    public Optional<UserLoginTBL> fetchWithUserKey( String userKey ){
        return userLoginREPO.findByUserkeyIs( userKey ).stream( ).findFirst( );
    }

    public void update( UserLoginTBL userLoginTBL ){
        userLoginREPO.save( userLoginTBL ) ;
    }

    public Boolean isPasswordValid( String password, UserLoginTBL user){
        String encryptedPassword = JamiiUserPasswordEncryptTool.encryptPassword( password );
        return JamiiStringUtils.equals( encryptedPassword, user.getPasswordsalt( ) );
    }

    public void deactivateUser( UserLoginTBL userLoginTBL ){
        userLoginTBL.setActive( UserLoginTBL.ACTIVE_OFF );
        userLoginREPO.save( userLoginTBL );
    }

    public void reactivateUser( UserLoginTBL userLoginTBL ){
        userLoginTBL.setActive( UserLoginTBL.ACTIVE_ON );
        userLoginREPO.save( userLoginTBL );
    }

}
