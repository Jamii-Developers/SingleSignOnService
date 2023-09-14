package com.jamii.services.fileManagement;

import com.jamii.Utils.JamiiDebug;
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
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        this.isSuccessful = false;
    }

    @Override
    public void processRequest() throws IOException {

        Optional<UserLoginTBL> user = this.userLoginCONT.fetchWithUserKey( this.userFileDownloadREQ.getUser_key( ) ) ;
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "This user key does not exists : " + getUserFileDownloadREQ( ).getUser_key( ) );
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<DeviceInformationTBL> deviceInformation = this.deviceInformationCONT.fetchByUserandDeviceKey( user.get( ), this.userFileDownloadREQ.getDevice_key( ) );
        if( deviceInformation.isEmpty( ) ){
            JamiiDebug.warning( "This device key does not exists : " + getUserFileDownloadREQ( ).getDevice_key( ));
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoMatchingDeviceKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<FileTableOwnerTBL> fileInformation = this.fileTableOwnerCONT.getFileByUserLoginIdAndName( user.get( ) ,getUserFileDownloadREQ( ).getFileName( ) );
        if( fileInformation.isEmpty( ) ){
            JamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + getUserFileDownloadREQ( ).getDevice_key( ));
            this.jamiiErrorsMessagesRESP.setDownloadFileOPS_NoActiveFileFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        this.requestedFileInformation = fileInformation.get( );

        Path path = Paths.get(getFilePath());
        Resource resource = null;
            try {
                resource = new UrlResource( path.toUri( ) );
            }catch( Exception e){
                e.printStackTrace( );
                JamiiDebug.warning( "Error creating resource : " + getUserFileDownloadREQ( ).getDevice_key( ) );
                this.jamiiErrorsMessagesRESP.setDownloadFileOPS_OopsWeCannotFindThisFile( );
                this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
                return ;
            }

        isSuccessful = true;
    }

    @Override
    public ResponseEntity<  String > getResponse( ){

        if( this.isSuccessful ){

        }

        return super.getResponse( );
    }

    protected String getFilePath( ){
        StringBuilder filePath = new StringBuilder( );
        filePath.append( this.requestedFileInformation.getFilelocation( ) );
        filePath.append( File.separator );
        filePath.append( this.requestedFileInformation.getSystemFilename( ) );
        filePath.append( JamiiFileUtils.getFileExtension( this.requestedFileInformation.getFiletype( ) ) ) ;
        return filePath.toString( );
    }
}
