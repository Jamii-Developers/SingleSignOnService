package com.jamii.operations.userServices.fileManagement;


import com.jamii.Utils.JamiiRandomKeyToolGen;
import com.jamii.Utils.JamiiUploadFileUtils;
import com.jamii.configs.FileServerConfigs;
import com.jamii.jamiidb.controllers.FileDirectory;
import com.jamii.jamiidb.controllers.FileTableOwner;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.fileManagementREQ.UserFileUploadServicesREQ;
import com.jamii.responses.userResponses.fileManagement.UserFileUploadRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserFileUploadOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private FileTableOwner fileTableOwner;
    @Autowired
    private FileDirectory fileDirectory;
    @Autowired
    private JamiiUploadFileUtils jamiiUploadFileUtils;

    protected UserFileUploadRESP userFileUploadRESP;
    public UserFileUploadRESP getUserFileUploadRESP() {
        return userFileUploadRESP;
    }
    public void setUserFileUploadRESP(UserFileUploadRESP userFileUploadRESP) {this.userFileUploadRESP = userFileUploadRESP;}

    @Override
    public void reset( ) {
        super.reset( );
        setUserFileUploadRESP( null );
    }

    @Override
    public void validateCookie( ) throws Exception{
        UserFileUploadServicesREQ req = (UserFileUploadServicesREQ) getRequest( );
        setDeviceKey( req.getDeviceKey( ) );
        setUserKey( req.getUserKey( ) );
        setSessionKey( req.getSessionKey() );
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !getIsSuccessful( ) ){
            return;
        }

        UserFileUploadServicesREQ req = ( UserFileUploadServicesREQ ) getRequest( ) ;
        Optional<UserLoginTBL> user = this.userLogin.fetchByUserKey( req.getUserKey( ), UserLogin.ACTIVE_ON ) ;
        if( user.isEmpty( ) ){
            jamiiDebug.warning( "This user key does not exists : " + req.getUserKey( ));
            this.jamiiErrorsMessagesRESP.setUploadFileOPS_NoMatchingUserKey( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        String sysFileName = generateFileKey( );
        FileTableOwnerTBL fileTableOwnerTBL = this.fileTableOwner.add( getFileOwnerRecord( user, sysFileName ) );
        this.fileDirectory.createFileDirectory( user.get( ), fileTableOwnerTBL, "./" );
        saveUploadedFile( user.get( ).getIdAsString( ), sysFileName );

        setIsSuccessful( true );
    }

    @Override
    public ResponseEntity< ? > getResponse() {

        if( getIsSuccessful( ) ){
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
        UserFileUploadServicesREQ req = ( UserFileUploadServicesREQ ) getRequest( );
        FileTableOwnerTBL fileTableOwnerTBL = new FileTableOwnerTBL( );
        fileTableOwnerTBL.setFilelocation( FileServerConfigs.USER_IMAGE_STORE + File.separator + user.get( ).getId( ) );
        fileTableOwnerTBL.setFiletype( req.getUploadfile( ).getContentType( ) );
        fileTableOwnerTBL.setUserloginid( user.get( ) );
        fileTableOwnerTBL.setFilesize( req.getUploadfile( ).getSize( ) );
        fileTableOwnerTBL.setOriginalfilename( req.uploadFile.getOriginalFilename( ) );
        fileTableOwnerTBL.setSystemfilename( sysFileName );
        fileTableOwnerTBL.setStatus( FileTableOwner.ACTIVE_STATUS_STORE );
        fileTableOwnerTBL.setDatecreated( LocalDateTime.now( ) );
        fileTableOwnerTBL.setLastupdated( LocalDateTime.now( ) );
        return fileTableOwnerTBL;
    }

    protected void saveUploadedFile( String userId,String sysFileName ) throws Exception {
        UserFileUploadServicesREQ req = ( UserFileUploadServicesREQ ) getRequest( ) ;
        this.jamiiUploadFileUtils.setDestDirectory( FileServerConfigs.USER_IMAGE_STORE + File.separator + userId );
        this.jamiiUploadFileUtils.setMultipartFile1( req.getUploadfile( ) );
        this.jamiiUploadFileUtils.setSystemFilename( sysFileName );
        this.jamiiUploadFileUtils.save( );
    }

}
