package com.jamii.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jamii.Utils.JamiiConstants;
import com.jamii.Utils.JamiiErrorUtils;
import com.jamii.responses.AbstractResponses;

public class JamiiErrorsMessagesRESP extends AbstractResponses {

    public class error_message{

    }

    private String MSGTYPE = JamiiErrorUtils.RESPONSE_TYPE;

    private String ERROR_FIELD_SUBJECT;
    private String ERROR_FIELD_MESSAGE;
    private String ERROR_FIELD_CODE;

    public String getERROR_FIELD_SUBJECT() {
        return ERROR_FIELD_SUBJECT;
    }

    public void setERROR_FIELD_SUBJECT(String ERROR_FIELD_SUBJECT) {
        this.ERROR_FIELD_SUBJECT = ERROR_FIELD_SUBJECT;
    }

    public String getERROR_FIELD_MESSAGE() {
        return ERROR_FIELD_MESSAGE;
    }

    public void setERROR_FIELD_MESSAGE(String ERROR_FIELD_MESSAGE) {
        this.ERROR_FIELD_MESSAGE = ERROR_FIELD_MESSAGE;
    }

    public String getERROR_FIELD_CODE() {
        return ERROR_FIELD_CODE;
    }

    public void setERROR_FIELD_CODE(String ERROR_FIELD_CODE) {
        this.ERROR_FIELD_CODE = ERROR_FIELD_CODE;
    }

    public void setLoginError( ){
        setERROR_FIELD_SUBJECT( "Login Error!"  );
        setERROR_FIELD_MESSAGE("The credentials are not valid" );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0001 ) ;
    }

    public void createNewUserError( ){
        setERROR_FIELD_SUBJECT( "User Sign Up Error!"  );
        setERROR_FIELD_MESSAGE( "The username or email address provided are already in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0002 ) ;
    }

    public void setPasswordChange_UsernameOrEmailAddressDoesNotExist( ){
        setERROR_FIELD_SUBJECT( "Password Change Error!"  );
        setERROR_FIELD_MESSAGE( "The username or email address provided does not exist in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0003 ) ;
    }

    public void setPasswordChange_PasswordsNotMatching( ){
        setERROR_FIELD_SUBJECT( "Password Change Error!"  );
        setERROR_FIELD_MESSAGE( "The passwords does not match what we currently have in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0004) ;

    }
    public void setPasswordChange_PasswordMatchesLastTen( ){
        setERROR_FIELD_SUBJECT( "Password Change Error!"  );
        setERROR_FIELD_MESSAGE( "The password matches the last ten"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0005 ) ;
    }

    public void setDeactivateUser_UsernameOrEmailAddressDoesNotExist( ){
        setERROR_FIELD_SUBJECT( "Account Deactivation Error!"  );
        setERROR_FIELD_MESSAGE( "The username or email address provided does not exist in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0006 ) ;
    }


    public void setDeactivateUser_PasswordsNotMatching( ){
        setERROR_FIELD_SUBJECT( "Account Deactivation Error!"  );
        setERROR_FIELD_MESSAGE( "The passwords does not match what we currently have in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0007 ) ;
    }

    public void setReactivateUser_UsernameOrEmailAddressDoesNotExist( ){
        setERROR_FIELD_SUBJECT( "Account Reactivation Error!"  );
        setERROR_FIELD_MESSAGE( "No deactivated username and email address matches the information provided"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0008 ) ;
    }

    public void setReactivateUser_PasswordsNotMatching( ){
        setERROR_FIELD_SUBJECT( "Account Reactivation Error!"  );
        setERROR_FIELD_MESSAGE( "The passwords does not match what we currently have in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0009 ) ;
    }

    public void setEditUserData_UserKeyDoesNotExist( ){
        setERROR_FIELD_SUBJECT( "Profile Update Error!"  );
        setERROR_FIELD_MESSAGE( "The system is not familiar with User Key"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0010 ) ;
    }

    public void setEditUserData_PasswordMatching( ){
        setERROR_FIELD_SUBJECT( "Profile Update Error!"  );
        setERROR_FIELD_MESSAGE( "The passwords does not match what we currently have in the system"  );
        setERROR_FIELD_CODE( JamiiErrorUtils.ERROR_CODE_0011 ) ;
    }




}
