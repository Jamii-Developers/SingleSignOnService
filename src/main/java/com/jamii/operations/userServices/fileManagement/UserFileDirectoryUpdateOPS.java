package com.jamii.operations.userServices.fileManagement;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.controllers.FileDirectory;
import com.jamii.jamiidb.controllers.FileTableOwner;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.fileManagementREQ.UserFileDirectoryUpdateREQ;
import com.jamii.responses.userResponses.fileManagement.UserFileDirectoryUpdateRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class UserFileDirectoryUpdateOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private FileTableOwner fileTableOwner;
    @Autowired
    private FileDirectory fileDirectory;

    @Override
    public void validateCookie( ) throws Exception{
        UserFileDirectoryUpdateREQ req = ( UserFileDirectoryUpdateREQ ) JamiiMapperUtils.mapObject( getRequest( ), UserFileDirectoryUpdateREQ.class );
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

        UserFileDirectoryUpdateREQ req = (UserFileDirectoryUpdateREQ) JamiiMapperUtils.mapObject( getRequest( ), UserFileDirectoryUpdateREQ.class );

        this.userLogin.data = this.userLogin.fetchByUserKey( req.getUserKey( ), UserLogin.ACTIVE_ON ).orElse( null ) ;
        if( this.userLogin.data == null ){
            jamiiDebug.warning( "This user key does not exists : " + req.getUserKey( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        this.fileTableOwner.data = this.fileTableOwner.fetch( this.userLogin.data ,req.getFileName( ) ).orElse( null );
        if( this.fileTableOwner.data == null ){
            jamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + req.getFileName( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryOPS_FileIsInTrash( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        ArrayList< Integer > checkAvailability = new ArrayList<>(Arrays.asList( FileTableOwner.ACTIVE_STATUS_DELETED, FileTableOwner.ACTIVE_STATUS_IN_TRASH ) );
        if( this.fileTableOwner.checkStatus( checkAvailability )){
            jamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + req.getFileName( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryOPS_FileIsInTrash( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        this.fileDirectory.data = this.fileDirectory.fetch( this.userLogin.data, this.fileTableOwner.data ).orElse( null );
        if( this.fileDirectory.data != null && JamiiStringUtils.equals( req.getDirectoryUpdate( ) , this.fileDirectory.data.getUidirectory() ) ){
            jamiiDebug.warning( "File is already in said location: " + req.getFileName( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryUpdateOPS_FileIsAlreadyInThisDirectory( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        this.fileDirectory.data.setUidirectory( req.getDirectoryUpdate( ) );
        this.fileDirectory.data.setLastupdated( LocalDateTime.now( ));
        this.fileDirectory.save( );
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( getIsSuccessful( ) ){
            UserFileDirectoryUpdateRESP response = new UserFileDirectoryUpdateRESP( );
            return new ResponseEntity<>( response.getJSONRESP( ), HttpStatus.OK );
        }

        return super.getResponse( );
    }
}
