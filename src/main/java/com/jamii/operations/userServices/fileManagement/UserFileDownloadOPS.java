package com.jamii.operations.userServices.fileManagement;

import com.jamii.Utils.JamiiFileDownloadUtils;
import com.jamii.Utils.JamiiFileUtils;
import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.FileTableOwnerCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.fileManagementREQ.UserFileDownloadREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserFileDownloadOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    protected FileTableOwnerCONT fileTableOwnerCONT;

    private FileTableOwnerTBL requestedFileInformation;
    public FileTableOwnerTBL getRequestedFileInformation() {return requestedFileInformation;}
    public void setRequestedFileInformation(FileTableOwnerTBL requestedFileInformation) {this.requestedFileInformation = requestedFileInformation;}

    private Resource resource;
    public Resource getResource() {
        return resource;
    }
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void reset( ){
        super.reset( );
        setRequestedFileInformation( null ) ;
        setResource( null ) ;
    }

    @Override
    public void validateCookie( ) throws Exception{
        UserFileDownloadREQ req = (UserFileDownloadREQ) JamiiMapperUtils.mapObject( getRequest( ), UserFileDownloadREQ.class );
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
        UserFileDownloadREQ req = (UserFileDownloadREQ) JamiiMapperUtils.mapObject( getRequest( ), UserFileDownloadREQ.class );

        Optional<UserLoginTBL> user = this.userLoginCONT.fetchByUserKey( req.getUserKey( ), UserLoginTBL.ACTIVE_ON ) ;
        if( user.isEmpty( ) ){
            jamiiDebug.warning( "This user key does not exists : " + req.getUserKey( ) );
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        Optional<FileTableOwnerTBL> fileInformation = this.fileTableOwnerCONT.fetch( user.get( ) ,req.getFileName( ) );
        if( fileInformation.isEmpty( ) ){
            jamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + req.getDeviceKey( ));
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoActiveFileFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        setRequestedFileInformation( fileInformation.get( ) );
        JamiiFileDownloadUtils downloadUtil = new JamiiFileDownloadUtils( );

        try {

            String fileLocation = getRequestedFileInformation( ).getFilelocation( );
            String systemFilename = getRequestedFileInformation( ).getSystemfilename( );
            String fileExtension = JamiiFileUtils.getFileExtension( getRequestedFileInformation( ).getFiletype( ) );
            setResource( downloadUtil.getFileAsResource( fileLocation, systemFilename, fileExtension ) );

        }catch( Exception e ){
            e.printStackTrace( );
            jamiiDebug.error( "Error creating resource : " + req.getDeviceKey( ) );
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_OopsWeCannotFindThisFile( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        setIsSuccessful( true );
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( getIsSuccessful( ) ){
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + getResource( ).getFilename() + "\"";

            return ResponseEntity.ok()
                    .contentType( MediaType.parseMediaType(contentType) )
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue )
                    .body( getResource( ) );

        }

        return super.getResponse( );
    }

}
