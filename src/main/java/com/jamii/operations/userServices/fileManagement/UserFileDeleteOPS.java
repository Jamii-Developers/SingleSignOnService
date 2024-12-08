package com.jamii.operations.userServices.fileManagement;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.FileTableOwnerCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.fileManagementREQ.UserFileDeleteREQ;
import com.jamii.responses.userResponses.fileManagement.UserFileDeleteRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserFileDeleteOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private FileTableOwnerCONT fileTableOwnerCONT;

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

        Optional<UserLoginTBL> user = this.userLoginCONT.fetchByUserKey( req.getUserKey( ), UserLoginTBL.ACTIVE_ON ) ;
        if( user.isEmpty( ) ){
            jamiiDebug.warning( "This user key does not exists : " + req.getUserKey( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDeleteOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        Optional<FileTableOwnerTBL> fileInformation = this.fileTableOwnerCONT.fetch( user.get( ) ,req.getFileName( ) );
        if( fileInformation.isEmpty( ) ) {
            jamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + req.getFileName( ));
            this.jamiiErrorsMessagesRESP.setUserFileDeleteOPS_FileIsInTrash( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        if( Objects.equals( fileInformation.get( ).getStatus( ), FileTableOwnerTBL.ACTIVE_STATUS_IN_TRASH ) || Objects.equals( fileInformation.get( ).getStatus( ), FileTableOwnerTBL.ACTIVE_STATUS_IN_TRASH ) ) {
            jamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + req.getFileName( ));
            this.jamiiErrorsMessagesRESP.setUserFileDeleteOPS_FileIsInTrash( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        fileInformation.get().setStatus( FileTableOwnerTBL.ACTIVE_STATUS_IN_TRASH );
        this.fileTableOwnerCONT.update( fileInformation.get( ) );

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
