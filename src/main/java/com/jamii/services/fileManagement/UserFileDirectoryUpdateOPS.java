package com.jamii.services.fileManagement;

import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.FileDirectoryCONT;
import com.jamii.jamiidb.controllers.FileTableOwnerCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.FileDirectoryTBL;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.fileManagement.UserFileDirectoryUpdateREQ;
import com.jamii.responses.fileManagement.UserFileDirectoryUpdateRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserFileDirectoryUpdateOPS extends fileManagementAbstract {

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private DeviceInformationCONT deviceInformationCONT;
    @Autowired
    private FileTableOwnerCONT fileTableOwnerCONT;
    @Autowired
    private FileDirectoryCONT fileDirectoryCONT;

    private UserFileDirectoryUpdateREQ userFileDirectoryUpdateREQ;
    protected Boolean isSuccessful = true ;

    public UserFileDirectoryUpdateREQ getUserFileDirectoryUpdateREQ() {
        return userFileDirectoryUpdateREQ;
    }

    public void setUserFileDirectoryUpdateREQ(UserFileDirectoryUpdateREQ userFileDirectoryUpdateREQ) {
        this.userFileDirectoryUpdateREQ = userFileDirectoryUpdateREQ;
    }

    @Override
    public void reset( ){
        super.reset( );
        this.isSuccessful = false ;
        this.userFileDirectoryUpdateREQ = null ;
    }

    @Override
    public void processRequest() throws IOException {

        Optional<UserLoginTBL> user = this.userLoginCONT.fetchWithUserKey( this.userFileDirectoryUpdateREQ.getUserKey( ) ) ;
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "This user key does not exists : " + getUserFileDirectoryUpdateREQ( ).getUserKey( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<DeviceInformationTBL> deviceInformation = this.deviceInformationCONT.fetchByUserandDeviceKey( user.get( ), this.userFileDirectoryUpdateREQ.getDeviceKey( ) );
        if( deviceInformation.isEmpty( ) ){
            JamiiDebug.warning( "This device key does not exists : " + getUserFileDirectoryUpdateREQ( ).getDeviceKey( ));
            this.jamiiErrorsMessagesRESP.setUserFileDirectory_NoMatchingDeviceKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<FileTableOwnerTBL> fileInformation = this.fileTableOwnerCONT.getFileByUserLoginIdAndName( user.get( ) ,getUserFileDirectoryUpdateREQ( ).getFileName( ) );
        if( fileInformation.isEmpty( ) ){
            JamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + getUserFileDirectoryUpdateREQ( ).getFileName( ));
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryOPS_FileIsInTrash( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<FileDirectoryTBL> fileDirectory = this.fileDirectoryCONT.fetch( user.get( ), fileInformation.get( ) );
        if(JamiiStringUtils.equals( getUserFileDirectoryUpdateREQ().getDirectoryUpdate( ) , fileDirectory.get( ).getUidirectory() ) ){
            JamiiDebug.warning( "File is already in said location: " + getUserFileDirectoryUpdateREQ( ).getFileName( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDirectoryUpdateOPS_FileIsAlreadyInThisDirectory( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }


        fileDirectory.get( ).setUidirectory( getUserFileDirectoryUpdateREQ( ).getDirectoryUpdate( ) );
        fileDirectory.get( ).setLastupdated( LocalDateTime.now( ));
        this.fileDirectoryCONT.update( fileDirectory.get( ) );
        this.isSuccessful = true ;
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){
            UserFileDirectoryUpdateRESP userFileDirectoryUpdateRESP = new UserFileDirectoryUpdateRESP( );
            return new ResponseEntity<>( userFileDirectoryUpdateRESP.getJSONRESP( ), HttpStatus.OK );
        }

        return super.getResponse( );
    }
}
