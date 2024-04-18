package com.jamii.operations.fileManagement.FunctionOPS;

import com.jamii.Utils.JamiiDebug;
import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.FileTableOwnerCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.fileManagement.FunctionREQ.UserFileDeleteREQ;
import com.jamii.responses.fileManagement.UserFileDeleteRESP;
import com.jamii.operations.fileManagement.fileManagementAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserFileDeleteOPS extends fileManagementAbstract {

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private DeviceInformationCONT deviceInformationCONT;
    @Autowired
    private FileTableOwnerCONT fileTableOwnerCONT;

    protected UserFileDeleteREQ userFileDeleteREQ;
    protected Boolean isSuccessful;

    public UserFileDeleteREQ getUserFileDeleteREQ() {
        return userFileDeleteREQ;
    }

    public void setUserFileDeleteREQ(UserFileDeleteREQ userFileDeleteREQ) {
        this.userFileDeleteREQ = userFileDeleteREQ;
    }

    @Override
    public void reset( ){
        super.reset();
        this.isSuccessful = false ;
        this.userFileDeleteREQ = null;
    }
    @Override
    public void processRequest() throws IOException {

        Optional<UserLoginTBL> user = this.userLoginCONT.fetch( this.userFileDeleteREQ.getUserKey( ), UserLoginTBL.ACTIVE ) ;
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "This user key does not exists : " + getUserFileDeleteREQ( ).getUserKey( ) );
            this.jamiiErrorsMessagesRESP.setUserFileDeleteOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<DeviceInformationTBL> deviceInformation = this.deviceInformationCONT.fetch( user.get( ), this.userFileDeleteREQ.getDeviceKey( ) );
        if( deviceInformation.isEmpty( ) ){
            JamiiDebug.warning( "This device key does not exists : " + getUserFileDeleteREQ( ).getDeviceKey( ));
            this.jamiiErrorsMessagesRESP.setUserFileDeleteOPS_NoMatchingDeviceKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<FileTableOwnerTBL> fileInformation = this.fileTableOwnerCONT.getFileByUserLoginIdAndName( user.get( ) ,getUserFileDeleteREQ( ).getFileName( ) );
        if( fileInformation.isEmpty( ) ){
            JamiiDebug.warning( "This the file is in trash or has been deleted from the system: " + getUserFileDeleteREQ( ).getFileName( ));
            this.jamiiErrorsMessagesRESP.setUserFileDeleteOPS_FileIsInTrash( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        fileInformation.get().setStatus( FileTableOwnerTBL.ACTIVE_STATUS_IN_TRASH );
        this.fileTableOwnerCONT.update( fileInformation.get( ) );

        isSuccessful = true ;
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){
            UserFileDeleteRESP userFileDeleteRESP = new UserFileDeleteRESP( );
            return  new ResponseEntity< >( userFileDeleteRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }
}
