package com.jamii.operations.activedirectory.functionOPS;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.activedirectory.AbstractPublicDirectory;
import com.jamii.requests.activeDirectory.FunctionREQ.ReactivateUserREQ;
import com.jamii.responses.activeDirectory.FunctionRESP.ReactivateUserRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReactivateUserOPS extends AbstractPublicDirectory {

    @Autowired
    private UserLoginCONT userLoginCONT;
    private Boolean accountReactivationSuccessful = false;

    @Override
    public void processRequest() throws Exception {

        ReactivateUserREQ req = (ReactivateUserREQ) JamiiMapperUtils.mapObject( getRequest( ), ReactivateUserREQ.class );

        //Check if the user exists as active
        Optional<UserLoginTBL> user = userLoginCONT.fetch( req.getEmailaddress( ), req.getUsername( ),  req.getActive( ) );
        if( user.isEmpty( ) ){
            jamiiDebug.warning( "No deactivated user matches the information shared " + req.getUsername( ) );
            this.jamiiErrorsMessagesRESP.setReactivateUser_UsernameOrEmailAddressDoesNotExist( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        //Check if the password is valid
        if( !userLoginCONT.isPasswordValid( req.getPassword( ), user.get( ) ) ){
            jamiiDebug.warning( "Password is incorrect " + req.getUsername( ) );
            this.jamiiErrorsMessagesRESP.setReactivateUser_PasswordsNotMatching( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        //Reactivate user
        userLoginCONT.reactivateUser( user.get( ) );
        accountReactivationSuccessful = true;

    }

    @Override
    public ResponseEntity< ? > getResponse() {

        ReactivateUserREQ req = (ReactivateUserREQ) JamiiMapperUtils.mapObject( getRequest( ), ReactivateUserREQ.class );

        if( accountReactivationSuccessful ){
            StringBuilder response = new StringBuilder( );
            jamiiDebug.info( String.format( "This is account has been Reactivated : %s ", req.getUsername( ) ) );
            ReactivateUserRESP reactivateUserRESP = new ReactivateUserRESP( );
            response.append( reactivateUserRESP.getJSONRESP( ) );
            return new ResponseEntity<>( response.toString( ), HttpStatus.OK );
        }

        jamiiDebug.warning( String.format( "Account Reactivation is unsuccessful : %s ", req.getUsername( ) ) );
        return super.getResponse( );
    }

    @Override
    public void reset( ) {
        super.reset( );
        accountReactivationSuccessful = false;
    }
}
