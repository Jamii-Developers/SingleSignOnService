package com.jamii.services.singleSignOn;

import com.jamii.Utils.JamiiRandomKeyToolGen;
import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
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

        //Create Device Key
        boolean checkIfKeyExists = false;
        JamiiRandomKeyToolGen keyToolGen = new JamiiRandomKeyToolGen( );
        keyToolGen.setLen( 50 );
        keyToolGen.setInclude_letters( true );
        keyToolGen.setInclude_numbers( true );
        keyToolGen.setInclude_special_chars( true );
        String key = "";
        while( !checkIfKeyExists ){
            key = keyToolGen.generate( );
            checkIfKeyExists = this.deviceInformationCONT.checkIfKeyExisitsInTheDatabase( key );
        }

        this.userDeviceInformation = this.deviceInformationCONT.add( this.userData, key, getCreateNewUserREQ( ).getDeviceName( ) );

        this.isSuccessful = true;
    }


    @Override
    public ResponseEntity< String > getResponse() {

        if( this.isSuccessful ){

            StringBuilder response = new StringBuilder( );

            CreateNewUserRESP createNewUserRESP = new CreateNewUserRESP(  );
            createNewUserRESP.setUSER_KEY( this.userData.getUserKey( ) );
            createNewUserRESP.setUSERNAME( this.userData.getUsername( ) );
            createNewUserRESP.setEMAIL_ADDRESS( this.userData.getEmailaddress( ) );
            createNewUserRESP.setDATE_CREATED( this.userData.getDatecreated( ).toString( ) );
            createNewUserRESP.setDEVICE_KEY( this.userDeviceInformation.getDevicekey( ) );
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
