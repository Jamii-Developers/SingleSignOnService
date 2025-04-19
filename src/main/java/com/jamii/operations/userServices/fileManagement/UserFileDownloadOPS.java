package com.jamii.operations.userServices.fileManagement;

import com.jamii.Utils.JamiiFileDownloadUtils;
import com.jamii.Utils.JamiiFileUtils;
import com.jamii.Utils.JamiiLoggingUtils;
import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.FileTableOwner;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.fileManagementREQ.UserFileDownloadREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserFileDownloadOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLogin userLogin;
    @Autowired
    protected FileTableOwner fileTableOwner;
    @Autowired
    JamiiLoggingUtils jamiiLoggingUtils;


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

        this.userLogin.data = this.userLogin.fetchByUserKey( req.getUserKey( ), UserLogin.ACTIVE_ON ).orElse( null ) ;
        if( this.userLogin.data == null ){
            jamiiDebug.warning( "This user key does not exists : " + req.getUserKey( ) );
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        this.fileTableOwner.data = this.fileTableOwner.fetch( this.userLogin.data ,req.getFileName( ) ).orElse( null );
        if( this.fileTableOwner == null ){
            jamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + req.getDeviceKey( ));
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoActiveFileFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
            return ;
        }

        JamiiFileDownloadUtils downloadUtil = new JamiiFileDownloadUtils( );

        try {

            assert this.fileTableOwner.data != null;
            String fileLocation = this.fileTableOwner.data.getFilelocation( );
            String systemFilename = this.fileTableOwner.data.getSystemfilename( );
            String fileExtension = JamiiFileUtils.getFileExtension( this.fileTableOwner.data.getFiletype( ) );
            setResource( downloadUtil.getFileAsResource( fileLocation, systemFilename, fileExtension ) );

        }catch( Exception e ){
            jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , this.userLogin.data ) ;
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
