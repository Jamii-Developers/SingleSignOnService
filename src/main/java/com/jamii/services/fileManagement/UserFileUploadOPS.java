package com.jamii.services.fileManagement;


import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiFileUtils;
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
import com.jamii.requests.fileManagement.UserFileUploadREQ;
import com.jamii.responses.fileManagement.UserFileUploadRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
    protected boolean fileUploadSuccessful = false;

    public UserFileUploadREQ getUploadREQ( ) {
        return userFileUploadREQ;
    }

    public void setUploadREQ( UserFileUploadREQ userFileUploadREQ) {
        this.userFileUploadREQ = userFileUploadREQ;
    }

    @Override
    public void reset( ) {
        super.reset( );
        this.fileUploadSuccessful = false;
        setUploadREQ( null );
    }

    @Override
    public void processRequest() throws IOException {


        Optional<UserLoginTBL> user = this.userLoginCONT.fetchWithUserKey( this.userFileUploadREQ.getUser_key( ) ) ;
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "This user key does not exists : " + getUploadREQ( ).getUser_key( ) );
            this.jamiiErrorsMessagesRESP.setUploadFileOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<DeviceInformationTBL> deviceinformation = this.deviceInformationCONT.fetchByUserandDeviceKey( user.get( ), this.userFileUploadREQ.getDevice_key( ) );

        if( deviceinformation.isEmpty( ) ){
            JamiiDebug.warning( "This device key does not exists : " + getUploadREQ( ).getDevice_key( ));
            this.jamiiErrorsMessagesRESP.setUploadFileOPS_NoMatchingDeviceKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        String sysFileName = generateFileKey( );
        FileTableOwnerTBL fileTableOwnerTBL = this.fileTableOwnerCONT.add( getFileOwnerRecord( user, sysFileName ) );
        this.fileDirectoryCONT.createFileDirectory( user.get( ), fileTableOwnerTBL, "./" );
        saveUploadedFile( user.get( ).getIdAsString( ), sysFileName );

        this.fileUploadSuccessful = true;
    }

    @Override
    public ResponseEntity< ? > getResponse() {

        if( this.fileUploadSuccessful ){
            UserFileUploadRESP userFileUploadRESP = new UserFileUploadRESP( );
            return new ResponseEntity<  >( userFileUploadRESP.getJSONRESP( ), HttpStatus.OK);
        }
        return  super.getResponse( );
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
        fileTableOwnerTBL.setFiletype( this.getUploadREQ( ).getUploadfile( ).getContentType( ) );
        fileTableOwnerTBL.setUserloginid( user.get( ) );
        fileTableOwnerTBL.setFilesize( this.userFileUploadREQ.getUploadfile( ).getSize( ) );
        fileTableOwnerTBL.setSystemfilename( sysFileName );
        fileTableOwnerTBL.setStatus( FileTableOwnerTBL.ACTIVE_STATUS_STORE );
        fileTableOwnerTBL.setDatecreated( LocalDateTime.now( ) );
        return fileTableOwnerTBL;
    }

    protected void saveUploadedFile( String userId,String sysFileName ){
        JamiiUploadFileUtils fileOPS = new JamiiUploadFileUtils( );
        fileOPS.setDestDirectory( FileServerConfigs.USER_IMAGE_STORE + File.separator + userId );
        fileOPS.setMultipartFile1( getUploadREQ().getUploadfile( ));
        fileOPS.setSystemFilename( sysFileName );
        fileOPS.save( );
    }

}