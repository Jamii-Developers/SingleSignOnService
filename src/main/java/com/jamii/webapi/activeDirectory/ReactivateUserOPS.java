package com.jamii.webapi.activeDirectory;

import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.ReactivateUserREQ;
import com.jamii.webapi.jamiidb.controllers.UserLoginCONT;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class ReactivateUserOPS extends activeDirectoryAbstract{

    @Autowired
    private UserLoginCONT userLoginCONT;

    private ReactivateUserREQ reactivateUserREQ;
    private Boolean accountReactivationSuccessful = false;

    public ReactivateUserREQ getReactivateUserREQ() {
        return reactivateUserREQ;
    }

    public void setReactivateUserREQ(ReactivateUserREQ reactivateUserREQ) {
        this.reactivateUserREQ = reactivateUserREQ;
    }

    @Override
    public void processRequest() throws Exception {

        //Check if the user exists as active
        Optional<UserLoginTBL> user = userLoginCONT.fetch( getReactivateUserREQ( ).getEmailaddress( ), getReactivateUserREQ( ).getUsername( ), getReactivateUserREQ( ).getUserkey( ), getReactivateUserREQ( ).getActive( ) );
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "No deactivated user matches the information shared " + getReactivateUserREQ( ).getUsername( ) );
            return;
        }

        //Check if the password is valid
        if( !userLoginCONT.isPasswordValid( getReactivateUserREQ( ).getPassword( ), user.get( ) ) ){
            JamiiDebug.warning( "Password is incorrect " + getReactivateUserREQ( ).getUsername( ) );
            return;
        }

        //Deactivate user
        userLoginCONT.reactivateUser( user.get( ) );

        accountReactivationSuccessful = true;

    }

    @Override
    public ResponseEntity<HashMap<String, String>> response() {
        if( !accountReactivationSuccessful ){
            JamiiDebug.warning( String.format( "This is account cannot be Reactivated : %s ", getReactivateUserREQ( ).getUsername( ) ) );
            return null;
        }

        JamiiDebug.warning( String.format( "Account Reactivation is successful : %s ", getReactivateUserREQ( ).getUsername( ) ) );
        return null;
    }

    @Override
    public ResponseEntity<String> getResponse() {
        return null;
    }

    @Override
    public void reset() {
        setReactivateUserREQ( null );
        accountReactivationSuccessful = false;
    }
}
