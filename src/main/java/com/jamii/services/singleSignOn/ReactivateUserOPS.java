package com.jamii.services.singleSignOn;

import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.activeDirectory.ReactivateUserREQ;
import com.jamii.responses.activeDirectory.ReactivateUserRESP;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReactivateUserOPS extends activeDirectoryAbstract {

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
        Optional<UserLoginTBL> user = userLoginCONT.fetch( getReactivateUserREQ( ).getEmailaddress( ), getReactivateUserREQ( ).getUsername( ),  getReactivateUserREQ( ).getActive( ) );
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "No deactivated user matches the information shared " + getReactivateUserREQ( ).getUsername( ) );
            this.jamiiErrorsMessagesRESP.setReactivateUser_UsernameOrEmailAddressDoesNotExist( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        //Check if the password is valid
        if( !userLoginCONT.isPasswordValid( getReactivateUserREQ( ).getPassword( ), user.get( ) ) ){
            JamiiDebug.warning( "Password is incorrect " + getReactivateUserREQ( ).getUsername( ) );
            this.jamiiErrorsMessagesRESP.setReactivateUser_PasswordsNotMatching( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        //Reactivate user
        userLoginCONT.reactivateUser( user.get( ) );
        accountReactivationSuccessful = true;

    }

    @Override
    public ResponseEntity<String> getResponse() {


        if( accountReactivationSuccessful ){
            StringBuilder response = new StringBuilder( );
            JamiiDebug.warning( String.format( "This is account has been Reactivated : %s ", getReactivateUserREQ( ).getUsername( ) ) );
            ReactivateUserRESP reactivateUserRESP = new ReactivateUserRESP( );
            response.append( reactivateUserRESP.getJSONRESP( ) );
            return new ResponseEntity<>( response.toString( ), HttpStatus.OK );
        }

        JamiiDebug.warning( String.format( "Account Reactivation is unsuccessful : %s ", getReactivateUserREQ( ).getUsername( ) ) );
        return super.getResponse( );
    }

    @Override
    public void reset( ) {
        super.reset( );
        setReactivateUserREQ( null );
        accountReactivationSuccessful = false;
    }
}
