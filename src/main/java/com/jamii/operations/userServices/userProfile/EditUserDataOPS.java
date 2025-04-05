package com.jamii.operations.userServices.userProfile;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.UserData;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.profileREQ.EditUserDataServicesREQ;
import com.jamii.responses.userResponses.profileResponses.EditUserDataRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditUserDataOPS extends AbstractUserServicesOPS {

    public EditUserDataOPS( ) { }

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserData userData;

    @Override
    public void validateCookie( ) throws Exception{
        EditUserDataServicesREQ req = (EditUserDataServicesREQ) JamiiMapperUtils.mapObject( getRequest( ), EditUserDataServicesREQ.class );
        setDeviceKey( req.getDeviceKey( ) );
        setUserKey( req.getUserKey( ) );
        setSessionKey( req.getSessionKey( ) );
        super.validateCookie( );
    }

    @Override
    public void processRequest( ) throws Exception {

        if( !getIsSuccessful( ) ){
            return;
        }

        EditUserDataServicesREQ req = (EditUserDataServicesREQ) JamiiMapperUtils.mapObject( getRequest( ), EditUserDataServicesREQ.class );
        Optional<UserLoginTBL> user = this.userLogin.fetchByUserKey( req.getUserKey( ), UserLogin.ACTIVE_ON );

        //Find userData currently active and deactivate them all
        this.userData.markAllPreviousUserDataInActive( user.get( ) );

        //Adds the latest userData to the database
        this.userData.add( user.get( ), req );

        //Updates Privacy Settings
        user.get( ).setPrivacy( req.getPrivacy( ) );
        this.userLogin.add( user.get( ) );

        setIsSuccessful( true );

    }



    @Override
    public ResponseEntity< ? > getResponse( ) {

        if( getIsSuccessful( ) ){
            StringBuilder response = new StringBuilder( );
            jamiiDebug.warning( "Profile has been update successfully" );
            EditUserDataRESP editUserDataRESP = new EditUserDataRESP( );
            response.append( editUserDataRESP.getJSONRESP( ) );
            return new ResponseEntity<>( response.toString( ), HttpStatus.OK );
        }


        return super.getResponse( );
    }
}
