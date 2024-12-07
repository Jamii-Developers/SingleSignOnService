package com.jamii.operations.activedirectory.functionOPS;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.configs.FileServerConfigs;
import com.jamii.jamiidb.controllers.PasswordHashRecordsCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.activedirectory.AbstractPublicDirectory;
import com.jamii.requests.activeDirectory.FunctionREQ.CreateNewUserREQ;
import com.jamii.requests.activeDirectory.FunctionREQ.UserLoginREQ;
import com.jamii.responses.activeDirectory.FunctionRESP.CreateNewUserRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class CreateNewUserOPS extends AbstractPublicDirectory {

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private PasswordHashRecordsCONT passwordHashRecordsCONT;


    private UserLoginTBL userData;
    private DeviceInformationTBL userDeviceInformation;
    private CreateNewUserREQ createNewUserREQ;
    private Boolean isSuccessful = false;


    @Override
    public void processRequest( ) throws Exception {

        CreateNewUserREQ req = (CreateNewUserREQ) JamiiMapperUtils.mapObject( getRequest( ), UserLoginREQ.class );

        if( userLoginCONT.checkifUserExists( req ) ){
            this.jamiiErrorsMessagesRESP.createNewUserError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        this.userData = userLoginCONT.createNewUser( req );

        //Add new password records
        if( this.userData != null){
            passwordHashRecordsCONT.addUserNewPasswordRecord( this.userData ) ;
        }

        this.isSuccessful = true;
    }


    @Override
    public ResponseEntity< ? > getResponse() {

        if( this.isSuccessful ){

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

    @Override
    public void reset( ){
        super.reset( );
        this.userData = null ;
        this.userDeviceInformation = null;
        this.isSuccessful = false ;
    }
}
