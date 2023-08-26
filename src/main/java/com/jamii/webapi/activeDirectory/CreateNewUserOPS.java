package com.jamii.webapi.activeDirectory;

import com.jamii.Utils.JamiiResponseErrorMessages;
import com.jamii.responses.MapUserLoginInformation;
import com.jamii.webapi.activeDirectory.controllers.PasswordHashRecordsCONT;
import com.jamii.webapi.activeDirectory.controllers.UserLoginInformationCONT;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CreateNewUserOPS extends activeDirectoryAbstract{

    private String emailaddress;
    private String username;
    private String password ;

    @Autowired
    private UserLoginInformationCONT userLoginInformationCONT;
    @Autowired
    private PasswordHashRecordsCONT passwordHashRecordsCONT;
    protected UserLoginTBL userData;

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String email_address) {
        this.emailaddress = email_address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void processRequest( ) throws Exception {
        this.userData = userLoginInformationCONT.createNewUser( this );

        //Add new password records
        if( this.userData != null){
            passwordHashRecordsCONT.addUserNewPasswordRecord( this.userData ) ;
        }
    }

    @Override
    public ResponseEntity< HashMap<String, String> > response( ) {

        if( this.userData == null ){
            jamiiDebug.warning(  String.format( "Username : %s or Email address: %s are already in the system", this.getUsername( ), this.getEmailaddress( ) ) );
            return new ResponseEntity<>( JamiiResponseErrorMessages.createNewUserError( ), HttpStatus.BAD_REQUEST );
        }

        jamiiDebug.info( "User has been found" );
        MapUserLoginInformation response = new MapUserLoginInformation( this.userData );
        return new ResponseEntity< >( response.getResponseMap() , HttpStatus.ACCEPTED ) ;
    }

    @Override
    public void reset( ){
        this.userData = null ;
    }
}
