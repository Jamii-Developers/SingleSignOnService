package com.jamii.services.SingleSignOn;

import com.jamii.responses.activeDirectory.CreateNewUserRESP;
import com.jamii.requests.activeDirectory.CreateNewUserREQ;
import com.jamii.jamiidb.controllers.PasswordHashRecordsCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

        if( userLoginCONT.checkifUserExists( this ) ){
            this.jamiiErrorsMessagesRESP.createNewUserError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        this.userData = userLoginCONT.createNewUser( this );

        //Add new password records
        if( this.userData != null){
            passwordHashRecordsCONT.addUserNewPasswordRecord( this.userData ) ;
        }
    }


    @Override
    public ResponseEntity< String > getResponse() {

        if( this.JamiiError.isEmpty( ) ){

            StringBuilder response = new StringBuilder( );

            CreateNewUserRESP createNewUserRESP = new CreateNewUserRESP(  );
            createNewUserRESP.setUSER_KEY( this.userData.getUserKey( ) );
            createNewUserRESP.setUSERNAME( this.userData.getUsername( ) );
            createNewUserRESP.setEMAIL_ADDRESS( this.userData.getEmailaddress( ) );
            createNewUserRESP.setDATE_CREATED( this.userData.getDatecreated( ).toString( ) );
            response.append(  createNewUserRESP.getJSONRESP( ) );

            return new ResponseEntity< String >( response.toString( ),HttpStatus.OK );
        }


        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.userData = null ;
        this.setCreateNewUserREQ( null );
    }
}
