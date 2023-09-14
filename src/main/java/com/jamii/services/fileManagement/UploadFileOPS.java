package com.jamii.services.fileManagement;


import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiFileUtils;
import com.jamii.Utils.JamiiRandomKeyToolGen;
import com.jamii.configs.FileServerConfigs;
import com.jamii.jamiidb.controllers.DeviceInformationCONT;
import com.jamii.jamiidb.controllers.FileDirectoryCONT;
import com.jamii.jamiidb.controllers.FileTableOwnerCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.fileManagement.UploadREQ;
import com.jamii.responses.fileManagement.UploadFileRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UploadFileOPS extends fileManagementAbstract {


    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private DeviceInformationCONT deviceInformationCONT;
    @Autowired
    private FileTableOwnerCONT fileTableOwnerCONT;
    @Autowired
    private FileDirectoryCONT fileDirectoryCONT;

    protected UploadREQ uploadREQ;
    protected boolean fileUploadSuccessful = false;

    public UploadREQ getUploadREQ( ) {
        return uploadREQ;
    }

    public void setUploadREQ( UploadREQ uploadREQ ) {
        this.uploadREQ = uploadREQ;
    }

    @Override
    public void reset( ) {
        super.reset( );
        this.fileUploadSuccessful = false;
        setUploadREQ( null );
    }

    @Override
    public void processRequest() throws IOException {


        Optional<UserLoginTBL> user = this.userLoginCONT.fetchWithUserKey( this.uploadREQ.getUser_key( ) ) ;
        if( user.isEmpty( ) ){
            JamiiDebug.warning( "This user key does not exists : " + getUploadREQ( ).getUser_key( ) );
            this.jamiiErrorsMessagesRESP.setUploadFileOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        Optional<DeviceInformationTBL> deviceinformation = this.deviceInformationCONT.fetchByUserandDeviceKey( user.get( ), this.uploadREQ.getDevice_key( ) );

        if( deviceinformation.isEmpty( ) ){
            JamiiDebug.warning( "This device key does not exists : " + getUploadREQ( ).getDevice_key( ));
            this.jamiiErrorsMessagesRESP.setUploadFileOPS_NoMatchingDeviceKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        String sysFileName = generateFileKey( );
        FileTableOwnerTBL fileTableOwnerTBL = this.fileTableOwnerCONT.add( getFileOwnerRecord( user, sysFileName ) );
        this.fileDirectoryCONT.createFileDirectory( user.get( ), fileTableOwnerTBL, "./" );
        saveUploadedFile( sysFileName );

        this.fileUploadSuccessful = true;
    }

    @Override
    public ResponseEntity< String > getResponse() {

        if( this.fileUploadSuccessful ){
            UploadFileRESP uploadFileRESP = new UploadFileRESP( );
            return new ResponseEntity< String >( uploadFileRESP.getJSONRESP( ), HttpStatus.OK);
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
        fileTableOwnerTBL.setFilelocation( FileServerConfigs.USER_IMAGE_STORE );
        fileTableOwnerTBL.setFiletype( this.getUploadREQ( ).getUploadfile( ).getContentType( ) );
        fileTableOwnerTBL.setUserloginid( user.get( ) );
        fileTableOwnerTBL.setFilesize( this.uploadREQ.getUploadfile( ).getSize( ) );
        fileTableOwnerTBL.setSystemFilename( sysFileName );
        fileTableOwnerTBL.setStatus( FileTableOwnerTBL.ACTIVE_STATUS_STORE );
        fileTableOwnerTBL.setDatecreated( LocalDateTime.now( ) );
        return fileTableOwnerTBL;
    }

    protected void saveUploadedFile( String sysFileName ){
        JamiiFileUtils fileOPS = new JamiiFileUtils();
        fileOPS.setDestDirectory( FileServerConfigs.USER_IMAGE_STORE );
        fileOPS.setMultipartFile1( getUploadREQ().getUploadfile( ));
        fileOPS.setSystemFilename( sysFileName );
        fileOPS.save( );
    }

}
