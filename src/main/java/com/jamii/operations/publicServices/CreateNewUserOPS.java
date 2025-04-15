package com.jamii.operations.publicServices;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.configs.FileServerConfigs;
import com.jamii.jamiidb.controllers.PasswordHashRecords;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.requests.publicServices.CreateNewUserREQ;
import com.jamii.responses.publicResponses.CreateNewUserRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class CreateNewUserOPS extends AbstractPublicServices {

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private PasswordHashRecords passwordHashRecords;

    @Override
    public void processRequest( ) throws Exception {

        CreateNewUserREQ req = (CreateNewUserREQ) JamiiMapperUtils.mapObject( getRequest( ), CreateNewUserREQ.class );

        // First check if user information exists in the system
        if( userLogin.checkifUserExists( req.getEmailaddress( ), req.getUsername( ) ) ){
            this.jamiiErrorsMessagesRESP.createNewUserError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        this.userLogin.data = this.userLogin.createNewUser( req );

        //Add new password records
        if( this.userLogin.data != null){
            this.passwordHashRecords.addUserNewPasswordRecord( this.userLogin.data ) ;
        }

        setIsSuccessful( true );
    }


    @Override
    public ResponseEntity< ? > getResponse() {

        if( getIsSuccessful() ){

            StringBuilder response = new StringBuilder( );

            File file = new File(FileServerConfigs.USER_IMAGE_STORE + File.separator + this.userLogin.data.getIdAsString( ) ) ;
            file.mkdir( ) ;

            CreateNewUserRESP createNewUserRESP = new CreateNewUserRESP(  );

            createNewUserRESP.setUSER_KEY( this.userLogin.data.getUserKey( ) );
            createNewUserRESP.setUSERNAME( this.userLogin.data.getUsername( ) );
            createNewUserRESP.setEMAIL_ADDRESS( this.userLogin.data.getEmailaddress( ) );
            createNewUserRESP.setDATE_CREATED( this.userLogin.data.getDatecreated( ).toString( ) );

            response.append(  createNewUserRESP.getJSONRESP( ) );

            return new ResponseEntity< >( response.toString( ),HttpStatus.OK );
        }


        return super.getResponse( );
    }


}
