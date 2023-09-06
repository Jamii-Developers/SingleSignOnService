package com.jamii.webapi.activeDirectory;

import com.jamii.responses.JamiiResponseErrorMessages;
import com.jamii.requests.CreateNewUserREQ;
import com.jamii.responses.MapUserLoginInformation;
import com.jamii.webapi.jamiidb.controllers.PasswordHashRecordsCONT;
import com.jamii.webapi.jamiidb.controllers.UserLoginCONT;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CreateNewUserOPS extends activeDirectoryAbstract{

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private PasswordHashRecordsCONT passwordHashRecordsCONT;

    protected UserLoginTBL userData;
    protected CreateNewUserREQ createNewUserREQ;

    public CreateNewUserREQ getCreateNewUserREQ() {
        return createNewUserREQ;
    }

    public void setCreateNewUserREQ( CreateNewUserREQ createNewUserREQ ) {
        this.createNewUserREQ = createNewUserREQ;
    }

    @Override
    public void processRequest( ) throws Exception {
        this.userData = userLoginCONT.createNewUser( this );

        //Add new password records
        if( this.userData != null){
            passwordHashRecordsCONT.addUserNewPasswordRecord( this.userData ) ;
        }
    }

    @Override
    public ResponseEntity< HashMap<String, String> > response( ) {

        if( this.userData == null ){
            jamiiDebug.warning(  String.format( "Username : %s or Email address: %s are already in the system", this.getCreateNewUserREQ( ).getUsername( ), this.getCreateNewUserREQ( ).getEmailaddress( ) ) );
            return new ResponseEntity<>( JamiiResponseErrorMessages.createNewUserError( ), HttpStatus.BAD_REQUEST );
        }

        jamiiDebug.info( "User has been added" );
        MapUserLoginInformation response = new MapUserLoginInformation( this.userData );
        return new ResponseEntity< >( response.getResponseMap() , HttpStatus.ACCEPTED ) ;
    }

    @Override
    public void reset( ){
        this.userData = null ;
        this.setCreateNewUserREQ( null );
    }
}
