package com.jamii.webapi.activeDirectory;

import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.EditUserDataREQ;
import com.jamii.webapi.jamiidb.controllers.UserDataCONT;
import com.jamii.webapi.jamiidb.controllers.UserLoginCONT;
import com.jamii.webapi.jamiidb.model.UserDataTBL;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class EditUserDataOPS extends activeDirectoryAbstract{
    public EditUserDataOPS() {
    }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserDataCONT userDataCONT;

    private EditUserDataREQ editUserDataREQ;


    public EditUserDataREQ getEditUserDataREQ() {
        return editUserDataREQ;
    }

    public void setEditUserDataREQ(EditUserDataREQ editUserDataREQ) {
        this.editUserDataREQ = editUserDataREQ;
    }

    @Override
    public void processRequest( ) throws Exception {

        Optional<UserLoginTBL> user = this.userLoginCONT.fetchWithUserKey( this.getEditUserDataREQ( ).getUserkey( ) );

        //Check if userKey exists
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "Cannot find userkey : " + this.getEditUserDataREQ( ).getUserkey( ) );
            return;
        }

        //Check if password matches
        if( this.userLoginCONT.isPasswordValid( this.getEditUserDataREQ( ).getPassword( ) ,user.get( ) ) ){
            JamiiDebug.warning( "The password does not match");
            return;
        }

        //Find userData currently active and deactivate them all
        this.userDataCONT.markAllPreviousUserDataInActive( user.get( ) );

        //Adds the latest userData to the database
        this.userDataCONT.addUserData( user.get( ), getEditUserDataREQ() );

    }

    @Override
    public ResponseEntity<HashMap<String, String>> response() {
        return null;
    }

    @Override
    public void reset() {
        this.setEditUserDataREQ( null );
    }
}
