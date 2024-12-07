package com.jamii.operations.fileManagement.FunctionOPS;


import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiRandomKeyToolGen;
import com.jamii.Utils.JamiiUploadFileUtils;
import com.jamii.configs.FileServerConfigs;
import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.FileDirectoryCONT;
import com.jamii.jamiidb.controllers.FileTableOwnerCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.fileManagement.FunctionREQ.UserFileUploadREQ;
import com.jamii.responses.fileManagement.UserFileUploadRESP;
import com.jamii.operations.fileManagement.fileManagementAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserFileUploadOPS extends fileManagementAbstract {

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private DeviceInformationCONT deviceInformationCONT;
    @Autowired
    private FileTableOwnerCONT fileTableOwnerCONT;
    @Autowired
    private FileDirectoryCONT fileDirectoryCONT;

    protected UserFileUploadREQ userFileUploadREQ;
    protected UserFileUploadRESP userFileUploadRESP;
    protected boolean fileUploadSuccessful = false;

    public UserFileUploadREQ getUserFileUploadREQ() {
        return userFileUploadREQ;
    }

    public void setUserFileUploadREQ(UserFileUploadREQ userFileUploadREQ) {
        this.userFileUploadREQ = userFileUploadREQ;
    }

    public UserFileUploadRESP getUserFileUploadRESP() {
        return userFileUploadRESP;
    }

    public void setUserFileUploadRESP(UserFileUploadRESP userFileUploadRESP) {
        this.userFileUploadRESP = userFileUploadRESP;
    }

    public boolean isFileUploadSuccessful() {
        return fileUploadSuccessful;
    }

    public void setFileUploadSuccessful(boolean fileUploadSuccessful) {
        this.fileUploadSuccessful = fileUploadSuccessful;
    }

    @Override
    public void reset( ) {
        super.reset( );
        setFileUploadSuccessful( false );
        setUserFileUploadREQ( null );
        setUserFileUploadRESP( null );
    }

    @Override
    public void processRequest() throws IOException {


        Optional<UserLoginTBL> user = this.userLoginCONT.fetch( getUserFileUploadREQ( ).getUserKey( ), UserLoginTBL.ACTIVE) ;
        if( user.isEmpty( ) ){
            jamiiDebug.warning( "This user key does not exists : " + getUserFileUploadREQ( ).getUserKey( ));
            this.jamiiErrorsMessagesRESP.setUploadFileOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<DeviceInformationTBL> deviceinformation = this.deviceInformationCONT.fetch( user.get( ), getUserFileUploadREQ( ).getDeviceKey( ) );
        if( deviceinformation.isEmpty( ) ){
            jamiiDebug.warning( "This device key does not exists : " + getUserFileUploadREQ( ).getDeviceKey( ) );
            this.jamiiErrorsMessagesRESP.setUploadFileOPS_NoMatchingDeviceKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        String sysFileName = generateFileKey( );
        FileTableOwnerTBL fileTableOwnerTBL = this.fileTableOwnerCONT.add( getFileOwnerRecord( user, sysFileName ) );
        this.fileDirectoryCONT.createFileDirectory( user.get( ), fileTableOwnerTBL, "./" );
        saveUploadedFile( user.get( ).getIdAsString( ), sysFileName );

        setFileUploadSuccessful( true );
    }

    @Override
    public ResponseEntity< ? > getResponse() {

        if( isFileUploadSuccessful( ) ){
            UserFileUploadRESP userFileUploadRESP = new UserFileUploadRESP( );
            return new ResponseEntity<  >( userFileUploadRESP.getJSONRESP( ), HttpStatus.OK );
        }
        return super.getResponse( );
    }

    protected static String generateFileKey( ){
        JamiiRandomKeyToolGen keyToolGen = new JamiiRandomKeyToolGen( );
        keyToolGen.setLen( 25 );
        keyToolGen.setInclude_letters( true );
        keyToolGen.setInclude_numbers( true );
        return keyToolGen.generate( );
    }

    protected FileTableOwnerTBL getFileOwnerRecord( Optional <UserLoginTBL> user, String  sysFileName ){
        FileTableOwnerTBL fileTableOwnerTBL = new FileTableOwnerTBL( );
        fileTableOwnerTBL.setFilelocation( FileServerConfigs.USER_IMAGE_STORE + File.separator + user.get( ).getId( ) );
        fileTableOwnerTBL.setFiletype( getUserFileUploadREQ( ).getUploadfile( ).getContentType( ) );
        fileTableOwnerTBL.setUserloginid( user.get( ) );
        fileTableOwnerTBL.setFilesize( this.userFileUploadREQ.getUploadfile( ).getSize( ) );
        fileTableOwnerTBL.setSystemfilename( sysFileName );
        fileTableOwnerTBL.setStatus( FileTableOwnerTBL.ACTIVE_STATUS_STORE );
        fileTableOwnerTBL.setDatecreated( LocalDateTime.now( ) );
        fileTableOwnerTBL.setLastupdated( LocalDateTime.now( ) );
        return fileTableOwnerTBL;
    }

    protected void saveUploadedFile( String userId,String sysFileName ){
        JamiiUploadFileUtils fileOPS = new JamiiUploadFileUtils( );
        fileOPS.setDestDirectory( FileServerConfigs.USER_IMAGE_STORE + File.separator + userId );
        fileOPS.setMultipartFile1( getUserFileUploadREQ( ).getUploadfile( ));
        fileOPS.setSystemFilename( sysFileName );
        fileOPS.save( );
    }

}
