package com.jamii.operations.fileManagement.FunctionOPS;

import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiFileDownloadUtils;
import com.jamii.Utils.JamiiFileUtils;
import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.FileTableOwnerCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.fileManagement.FunctionREQ.UserFileDownloadREQ;
import com.jamii.responses.fileManagement.UserFileDownloadsRESP;
import com.jamii.operations.fileManagement.fileManagementAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserFileDownloadOPS extends fileManagementAbstract {

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    protected DeviceInformationCONT deviceInformationCONT;
    @Autowired
    protected FileTableOwnerCONT fileTableOwnerCONT;

    private UserFileDownloadREQ userFileDownloadREQ;
    private UserFileDownloadsRESP userFileDownloadsRESP ;
    private FileTableOwnerTBL requestedFileInformation;
    private boolean isSuccessful = false;
    private Resource resource;

    public UserFileDownloadREQ getUserFileDownloadREQ() {
        return userFileDownloadREQ;
    }

    public void setUserFileDownloadREQ(UserFileDownloadREQ userFileDownloadREQ) {
        this.userFileDownloadREQ = userFileDownloadREQ;
    }

    public UserFileDownloadsRESP getUserFileDownloadsRESP() {
        return userFileDownloadsRESP;
    }

    public void setUserFileDownloadsRESP(UserFileDownloadsRESP userFileDownloadsRESP) {
        this.userFileDownloadsRESP = userFileDownloadsRESP;
    }

    public FileTableOwnerTBL getRequestedFileInformation() {
        return requestedFileInformation;
    }

    public void setRequestedFileInformation(FileTableOwnerTBL requestedFileInformation) {
        this.requestedFileInformation = requestedFileInformation;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void reset( ){
        super.reset( );
        setUserFileDownloadREQ( null ) ;
        setRequestedFileInformation( null ) ;
        setResource( null ) ;
        setSuccessful( false ) ;
    }

    @Override
    public void processRequest() throws IOException {

        Optional<UserLoginTBL> user = this.userLoginCONT.fetch( getUserFileDownloadREQ( ).getUserKey(), UserLoginTBL.ACTIVE ) ;
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "This user key does not exists : " + getUserFileDownloadREQ( ).getUserKey( ) );
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<DeviceInformationTBL> deviceInformation = this.deviceInformationCONT.fetch( user.get( ), getUserFileDownloadREQ( ).getDeviceKey( ) );
        if( deviceInformation.isEmpty( ) ){
            JamiiDebug.warning( "This device key does not exists : " + getUserFileDownloadREQ( ).getDeviceKey( ));
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoMatchingDeviceKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<FileTableOwnerTBL> fileInformation = this.fileTableOwnerCONT.getFileByUserLoginIdAndName( user.get( ) ,getUserFileDownloadREQ( ).getFileName( ) );
        if( fileInformation.isEmpty( ) ){
            JamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + getUserFileDownloadREQ( ).getDeviceKey( ));
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoActiveFileFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
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
            JamiiDebug.warning( "Error creating resource : " + getUserFileDownloadREQ( ).getDeviceKey( ) );
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_OopsWeCannotFindThisFile( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        isSuccessful = true;
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( isSuccessful( ) ){
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + getResource( ).getFilename() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body( getResource( ) );

        }

        return super.getResponse( );
    }

}
