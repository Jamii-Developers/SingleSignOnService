package com.jamii.operations.activedirectory.FunctionOPS;

import com.jamii.configs.FileServerConfigs;
import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.PasswordHashRecordsCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.activedirectory.AbstractPublicDirectory;
import com.jamii.requests.activeDirectory.FunctionREQ.CreateNewUserREQ;
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
    @Autowired
    private DeviceInformationCONT deviceInformationCONT;

    private UserLoginTBL userData;
    private DeviceInformationTBL userDeviceInformation;
    private CreateNewUserREQ createNewUserREQ;
    private Boolean isSuccessful = false;

    public CreateNewUserREQ getCreateNewUserREQ() {
        return createNewUserREQ;
    }

    public void setCreateNewUserREQ( CreateNewUserREQ createNewUserREQ ) {
        this.createNewUserREQ = createNewUserREQ;
    }

    @Override
    public void processRequest( ) throws Exception {

        if( userLoginCONT.checkifUserExists( getCreateNewUserREQ( ) ) ){
            this.jamiiErrorsMessagesRESP.createNewUserError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        this.userData = userLoginCONT.createNewUser( getCreateNewUserREQ( ) );

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
        this.setCreateNewUserREQ( null );
    }
}
