package com.jamii.webapi.activeDirectory;

import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.activeDirectory.DeactivateUserREQ;
import com.jamii.responses.activeDirectory.DeactivateUserRESP;
import com.jamii.webapi.jamiidb.controllers.UserLoginCONT;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public void setDeactivateUserREQ( DeactivateUserREQ deactivateUserREQ) {
        this.deactivateUserREQ = deactivateUserREQ;
    }

    public DeactivateUserOPS( ) { }

    @Override
    public void processRequest( ) throws Exception {

        //Check if the user exists as active
        Optional< UserLoginTBL > user = userLoginCONT.fetch( getDeactivateUserREQ( ).getEmailaddress( ), getDeactivateUserREQ( ).getUsername( ), getDeactivateUserREQ( ).getActive( ) );
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "No activated user matches this information " + getDeactivateUserREQ( ).getUsername( ) );
            this.jamiiErrorsMessagesRESP.setDeactivateUser_UsernameOrEmailAddressDoesNotExist( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        //Check if the password is valid
        if( !userLoginCONT.isPasswordValid( getDeactivateUserREQ( ).getPassword( ), user.get( ) ) ){
            JamiiDebug.warning( "Password is incorrect " + getDeactivateUserREQ( ).getUsername( ) );
            this.jamiiErrorsMessagesRESP.setDeactivateUser_PasswordsNotMatching( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        //Deactivate user
        userLoginCONT.deactivateUser( user.get( ) );
        accountDeactivationSuccessful = true;
    }

    @Override
    public ResponseEntity< String > getResponse() {

        if( accountDeactivationSuccessful ){
            JamiiDebug.warning( String.format( "This account has been deactivated : %s ", getDeactivateUserREQ( ).getUsername( ) ) );
            StringBuilder response = new StringBuilder( );
            DeactivateUserRESP deactivateUserRESP = new DeactivateUserRESP( );
            response.append( deactivateUserRESP.getJSONRESP( ) );
            return new ResponseEntity<>( response.toString( ), HttpStatus.OK );
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ) {
        super.reset( );
        setDeactivateUserREQ( null );
        accountDeactivationSuccessful = false;
    }
}
