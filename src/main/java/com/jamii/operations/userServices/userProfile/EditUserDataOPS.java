package com.jamii.operations.userServices.userProfile;

import com.jamii.jamiidb.controllers.UserDataCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServices;
import com.jamii.requests.userServices.profileREQ.EditUserDataServicesREQ;
import com.jamii.responses.userResponses.profileResponses.EditUserDataRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditUserDataOPS extends AbstractUserServices {

    public EditUserDataOPS( ) { }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserDataCONT userDataCONT;

    private EditUserDataServicesREQ editUserDataREQ;
    private Boolean profileUpdateSuccessful = false;

    public EditUserDataServicesREQ getEditUserDataREQ() {
        return editUserDataREQ;
    }

    public void setEditUserDataREQ(EditUserDataServicesREQ editUserDataREQ) {
        this.editUserDataREQ = editUserDataREQ;
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getEditUserDataREQ().getDeviceKey();
        UserKey = getEditUserDataREQ().getUserKey();
        SessionKey = getEditUserDataREQ().getSessionkey();
        super.validateCookie( );
    }

    @Override
    public void processRequest( ) throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> user = this.userLoginCONT.fetchByUserKey( this.getEditUserDataREQ( ).getUserKey( ), UserLoginTBL.ACTIVE_ON );

        //Find userData currently active and deactivate them all
        this.userDataCONT.markAllPreviousUserDataInActive( user.get( ) );

        //Adds the latest userData to the database
        this.userDataCONT.add( user.get( ), getEditUserDataREQ( ) );

        //Updates Privacy Settings
        user.get( ).setPrivacy( getEditUserDataREQ( ).getPrivacy( ) );
        this.userLoginCONT.add( user.get( ) );

        profileUpdateSuccessful = true;

    }



    @Override
    public ResponseEntity< ? > getResponse( ) {

        if( profileUpdateSuccessful ){
            StringBuilder response = new StringBuilder( );
            jamiiDebug.warning( "Profile has been update successfully" );
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
