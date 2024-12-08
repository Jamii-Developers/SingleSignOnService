package com.jamii.operations.userServices.fileManagement;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.controllers.FileDirectoryCONT;
import com.jamii.jamiidb.controllers.FileTableOwnerCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.FileDirectoryTBL;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.fileManagementREQ.UserFileDirectoryUpdateREQ;
import com.jamii.responses.userResponses.fileManagement.UserFileDirectoryUpdateRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserFileDirectoryUpdateOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private FileTableOwnerCONT fileTableOwnerCONT;
    @Autowired
    private FileDirectoryCONT fileDirectoryCONT;

    @Override
    public void validateCookie( ) throws Exception{
        UserFileDirectoryUpdateREQ req = (UserFileDirectoryUpdateREQ) JamiiMapperUtils.mapObject( getRequest( ), UserFileDirectoryUpdateREQ.class );
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

        Optional<UserLoginTBL> user = this.userLoginCONT.fetchByUserKey( req.getUserKey( ), UserLoginTBL.ACTIVE_ON ) ;
        if( user.isEmpty( ) ){
            jamiiDebug.warning( "This user key does not exists : " + req.getUserKey( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        Optional<FileTableOwnerTBL> fileInformation = this.fileTableOwnerCONT.fetch( user.get( ) ,req.getFileName( ) );
        if( fileInformation.isEmpty() ){
            jamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + req.getFileName( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryOPS_FileIsInTrash( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        if(Objects.equals(fileInformation.get().getStatus(), FileTableOwnerTBL.ACTIVE_STATUS_DELETED) || Objects.equals(fileInformation.get().getStatus(), FileTableOwnerTBL.ACTIVE_STATUS_IN_TRASH)){
            jamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + req.getFileName( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryOPS_FileIsInTrash( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        Optional<FileDirectoryTBL> fileDirectory = this.fileDirectoryCONT.fetch( user.get( ), fileInformation.get( ) );
        if( fileDirectory.isPresent( ) && JamiiStringUtils.equals( req.getDirectoryUpdate( ) , fileDirectory.get( ).getUidirectory() ) ){
            jamiiDebug.warning( "File is already in said location: " + req.getFileName( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryUpdateOPS_FileIsAlreadyInThisDirectory( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        fileDirectory.get( ).setUidirectory( req.getDirectoryUpdate( ) );
        fileDirectory.get( ).setLastupdated( LocalDateTime.now( ));
        this.fileDirectoryCONT.update( fileDirectory.get( ) );
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
