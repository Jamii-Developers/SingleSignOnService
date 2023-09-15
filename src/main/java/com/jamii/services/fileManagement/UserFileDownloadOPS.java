package com.jamii.services.fileManagement;

import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiFileDownloadUtils;
import com.jamii.Utils.JamiiFileUtils;
import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.FileTableOwnerCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.fileManagement.UserFileDownloadREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
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

    protected UserFileDownloadREQ userFileDownloadREQ;
    protected FileTableOwnerTBL requestedFileInformation;
    protected boolean isSuccessful = false;
    protected Resource resource;

    public UserFileDownloadREQ getUserFileDownloadREQ() {
        return userFileDownloadREQ;
    }

    public void setUserFileDownloadREQ(UserFileDownloadREQ userFileDownloadREQ) {
        this.userFileDownloadREQ = userFileDownloadREQ;
    }

    @Override
    public void reset( ){
        super.reset( );
        this.setUserFileDownloadREQ( null ) ;
        this.requestedFileInformation = null;
        this.resource = (null);
        this.isSuccessful = false;
    }

    @Override
    public void processRequest() throws IOException {

        Optional<UserLoginTBL> user = this.userLoginCONT.fetchWithUserKey( this.userFileDownloadREQ.getUserkey( ) ) ;
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "This user key does not exists : " + getUserFileDownloadREQ( ).getUserkey( ) );
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<DeviceInformationTBL> deviceInformation = this.deviceInformationCONT.fetchByUserandDeviceKey( user.get( ), this.userFileDownloadREQ.getDevicekey( ) );
        if( deviceInformation.isEmpty( ) ){
            JamiiDebug.warning( "This device key does not exists : " + getUserFileDownloadREQ( ).getDevicekey( ));
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoMatchingDeviceKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<FileTableOwnerTBL> fileInformation = this.fileTableOwnerCONT.getFileByUserLoginIdAndName( user.get( ) ,getUserFileDownloadREQ( ).getFileName( ) );
        if( fileInformation.isEmpty( ) ){
            JamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + getUserFileDownloadREQ( ).getDevicekey( ));
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoActiveFileFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        this.requestedFileInformation = fileInformation.get( );

        JamiiFileDownloadUtils downloadUtil = new JamiiFileDownloadUtils( );

        try {

            String fileLocation = this.requestedFileInformation.getFilelocation( );
            String systemFilename = this.requestedFileInformation.getSystemfilename( );
            String fileExtension = JamiiFileUtils.getFileExtension( this.requestedFileInformation.getFiletype( ) );
            this.resource = downloadUtil.getFileAsResource( fileLocation, systemFilename, fileExtension );
        }catch( Exception e ){
            e.printStackTrace( );
            JamiiDebug.warning( "Error creating resource : " + getUserFileDownloadREQ( ).getDevicekey( ) );
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_OopsWeCannotFindThisFile( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        isSuccessful = true;
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);

        }

        return super.getResponse( );
    }

}
