package com.jamii.webapi.activeDirectory;

import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.DeactivateUserREQ;
import com.jamii.webapi.jamiidb.controllers.UserLoginCONT;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class DeactivateUserOPS extends activeDirectoryAbstract {

    @Autowired
    private UserLoginCONT userLoginCONT;

    private DeactivateUserREQ deactivateUserREQ;

    private Boolean accountDeactivationSuccessful = false;

    public DeactivateUserREQ getDeactivateUserREQ() {
        return deactivateUserREQ;
    }

    public void setDeactivateUserREQ(DeactivateUserREQ deactivateUserREQ) {
        this.deactivateUserREQ = deactivateUserREQ;
    }

    public DeactivateUserOPS( ) { }

    @Override
    public void processRequest( ) throws Exception {

        //Check if the user exists as active
        Optional< UserLoginTBL > user = userLoginCONT.fetch( getDeactivateUserREQ( ).getEmailaddress( ), getDeactivateUserREQ( ).getUsername( ), getDeactivateUserREQ( ).getUserkey( ), getDeactivateUserREQ( ).getActive( ) );
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "No activated user matches this information " + getDeactivateUserREQ( ).getUsername( ) );
            return;
        }

        //Check if the password is valid
        if( !userLoginCONT.isPasswordValid( getDeactivateUserREQ( ).getPassword( ), user.get( ) ) ){
            JamiiDebug.warning( "Password is incorrect " + getDeactivateUserREQ( ).getUsername( ) );
            return;
        }

        //Deactivate user
        userLoginCONT.deactivateUser( user.get( ) );

        accountDeactivationSuccessful = true;
    }

    @Override
    public ResponseEntity<HashMap<String, String>> response() {
        if( !accountDeactivationSuccessful ){
            JamiiDebug.warning( String.format( "This is account cannot be deactivated : %s ", getDeactivateUserREQ( ).getUsername( ) ) );
            return null;
        }
        JamiiDebug.warning( String.format( "Account deactivation is successful : %s ", getDeactivateUserREQ( ).getUsername( ) ) );
        return null;
    }

    @Override
    public ResponseEntity<String> getResponse() {
        return null;
    }

    @Override
    public void reset() {
        setDeactivateUserREQ( null );
        accountDeactivationSuccessful = false;
    }
}
