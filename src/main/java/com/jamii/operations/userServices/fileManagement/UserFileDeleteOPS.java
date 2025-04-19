package com.jamii.operations.userServices.fileManagement;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.FileTableOwner;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.fileManagementREQ.UserFileDeleteREQ;
import com.jamii.responses.userResponses.fileManagement.UserFileDeleteRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class UserFileDeleteOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private FileTableOwner fileTableOwner;

    @Override
    public void validateCookie( ) throws Exception{
        UserFileDeleteREQ req = (UserFileDeleteREQ) JamiiMapperUtils.mapObject( getRequest( ), UserFileDeleteREQ.class );
        setDeviceKey( req.getDeviceKey( ) );
        setUserKey( req.getUserKey( ) );
        setSessionKey( req.getSessionKey() );
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws IOException {

        if( !getIsSuccessful( ) ){
            return;
        }

        UserFileDeleteREQ req = (UserFileDeleteREQ) JamiiMapperUtils.mapObject( getRequest( ), UserFileDeleteREQ.class );

        this.userLogin.data = this.userLogin.fetchByUserKey( req.getUserKey( ), UserLogin.ACTIVE_ON ).orElse( null );
        if( this.userLogin.data == null ){
            jamiiDebug.warning( "This user key does not exists : " + req.getUserKey( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDeleteOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        this.fileTableOwner.data = this.fileTableOwner.fetch( this.userLogin.data ,req.getFileName( ) ).orElse( null );
        if( this.fileTableOwner.data == null ) {
            jamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + req.getFileName( ));
            this.jamiiErrorsMessagesRESP.setUserFileDeleteOPS_FileIsInTrash( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        ArrayList< Integer > checkAvailability = new ArrayList<>(Arrays.asList( FileTableOwner.ACTIVE_STATUS_IN_TRASH, FileTableOwner.ACTIVE_STATUS_DELETED ) );
        if( this.fileTableOwner.checkStatus( checkAvailability ) ) {
            jamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + req.getFileName( ));
            this.jamiiErrorsMessagesRESP.setUserFileDeleteOPS_FileIsInTrash( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        this.fileTableOwner.data.setStatus( FileTableOwner.ACTIVE_STATUS_IN_TRASH );
        this.fileTableOwner.save( );

        setIsSuccessful( true );
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( getIsSuccessful( ) ){
            UserFileDeleteRESP userFileDeleteRESP = new UserFileDeleteRESP( );
            return  new ResponseEntity< >( userFileDeleteRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }
}
