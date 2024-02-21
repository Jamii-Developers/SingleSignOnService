package com.jamii.services.singleSignOn;

import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.activeDirectory.EditUserDataREQ;
import com.jamii.responses.activeDirectory.EditUserDataRESP;
import com.jamii.jamiidb.controllers.UserDataCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditUserDataOPS extends activeDirectoryAbstract {

    public EditUserDataOPS( ) { }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserDataCONT userDataCONT;

    private EditUserDataREQ editUserDataREQ;
    private Boolean profileUpdateSuccessful = false;


    public EditUserDataREQ getEditUserDataREQ() {
        return editUserDataREQ;
    }

    public void setEditUserDataREQ(EditUserDataREQ editUserDataREQ) {
        this.editUserDataREQ = editUserDataREQ;
    }

    @Override
    public void processRequest( ) throws Exception {

        Optional<UserLoginTBL> user = this.userLoginCONT.fetch( this.getEditUserDataREQ( ).getUserkey( ), UserLoginTBL.ACTIVE );

        //Check if userKey exists
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "Cannot find userkey : " + this.getEditUserDataREQ( ).getUserkey( ) );
            this.jamiiErrorsMessagesRESP.setEditUserData_UserKeyDoesNotExist( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        //Check if password matches
        if( !this.userLoginCONT.isPasswordValid( this.getEditUserDataREQ( ).getPassword( ) ,user.get( ) ) ){
            JamiiDebug.warning( "The password does not match");
            this.jamiiErrorsMessagesRESP.setEditUserData_PasswordMatching( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        //Find userData currently active and deactivate them all
        this.userDataCONT.markAllPreviousUserDataInActive( user.get( ) );

        //Adds the latest userData to the database
        this.userDataCONT.addUserData( user.get( ), getEditUserDataREQ( ) );

        profileUpdateSuccessful = true;

    }



    @Override
    public ResponseEntity< ? > getResponse( ) {

        if( profileUpdateSuccessful ){
            StringBuilder response = new StringBuilder( );
            JamiiDebug.warning( "Profile has been update successfully" );
            EditUserDataRESP editUserDataRESP = new EditUserDataRESP( );
            response.append( editUserDataRESP.getJSONRESP( ) );
            return new ResponseEntity<>( response.toString( ), HttpStatus.OK );
        }


        return super.getResponse( );
    }

    @Override
    public void reset() {
        super.reset( );
        this.setEditUserDataREQ( null );
        profileUpdateSuccessful = false;
    }
}
