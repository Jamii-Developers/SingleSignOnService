package com.jamii.operations.publicServices;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.configs.FileServerConfigs;
import com.jamii.jamiidb.controllers.PasswordHashRecords;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.model.UserLoginTBL;
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

    private UserLoginTBL userData;

    @Override
    public void reset( ){
        super.reset( );
        this.userData = null ;
    }


    @Override
    public void processRequest( ) throws Exception {

        CreateNewUserREQ req = (CreateNewUserREQ) JamiiMapperUtils.mapObject( getRequest( ), CreateNewUserREQ.class );

        if( userLogin.checkifUserExists( req.getEmailaddress( ), req.getUsername( ) ) ){
            this.jamiiErrorsMessagesRESP.createNewUserError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        this.userData = userLogin.createNewUser( req );

        //Add new password records
        if( this.userData != null){
            passwordHashRecords.addUserNewPasswordRecord( this.userData ) ;
        }

        setIsSuccessful( true );
    }


    @Override
    public ResponseEntity< ? > getResponse() {

        if( getIsSuccessful() ){

            StringBuilder response = new StringBuilder( );

            File file = new File(FileServerConfigs.USER_IMAGE_STORE + File.separator + this.userData.getIdAsString( ) ) ;
            file.mkdir( ) ;

            CreateNewUserRESP createNewUserRESP = new CreateNewUserRESP(  );
            createNewUserRESP.setUSER_KEY( this.userData.getUserKey( ) );
            createNewUserRESP.setUSERNAME( this.userData.getUsername( ) );
            createNewUserRESP.setEMAIL_ADDRESS( this.userData.getEmailaddress( ) );
            createNewUserRESP.setDATE_CREATED( this.userData.getDatecreated( ).toString( ) );
            response.append(  createNewUserRESP.getJSONRESP( ) );

            return new ResponseEntity< >( response.toString( ),HttpStatus.OK );
        }


        return super.getResponse( );
    }


}
