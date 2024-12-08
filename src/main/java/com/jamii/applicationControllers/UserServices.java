package com.jamii.applicationControllers;

import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.operations.userServices.clientCommunication.ReviewUsOPS;
import com.jamii.operations.userServices.fileManagement.UserFileDeleteOPS;
import com.jamii.operations.userServices.fileManagement.UserFileDirectoryUpdateOPS;
import com.jamii.operations.userServices.fileManagement.UserFileDownloadOPS;
import com.jamii.operations.userServices.fileManagement.UserFileUploadOPS;
import com.jamii.operations.userServices.userProfile.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServices {

    @Autowired
    ReviewUsOPS reviewUsOPS;
    @Autowired
    UserFileUploadOPS userFileUploadOPS;
    @Autowired
    UserFileDirectoryUpdateOPS userFileDirectoryUpdateOPS;
    @Autowired
    UserFileDeleteOPS userFileDeleteOPS;
    @Autowired
    UserFileDownloadOPS userFileDownloadOPS;
    @Autowired
    ChangePasswordOPS changePasswordOPS;
    @Autowired
    EditUserDataOPS editUserDataOPS;
    @Autowired
    FetchUserDataOPS fetchUserDataOPS;
    @Autowired
    DeactivateUserOPS deactivateUserOPS;
    @Autowired
    UserLogoffOPS userLogoffOPS;

    private final JamiiDebug jamiiDebug = new JamiiDebug( this.getClass() );
    private final Map<String, Object> directoryMap = new HashMap<>();

    @PostConstruct
    private void initPathing() {
        directoryMap.put( "reviewus", reviewUsOPS);
        directoryMap.put( "userfileupload", userFileUploadOPS);
        directoryMap.put( "userdirupd", userFileDirectoryUpdateOPS );
        directoryMap.put( "userfiledel", userFileDeleteOPS );
        directoryMap.put( "userfiledwnld", userFileDownloadOPS );
        directoryMap.put( "chngpassword", changePasswordOPS );
        directoryMap.put( "editprofile", editUserDataOPS );
        directoryMap.put( "fetchprofile", fetchUserDataOPS);
        directoryMap.put( "deactivateuser", deactivateUserOPS);
        directoryMap.put( "userlogoff", userLogoffOPS );
    }


    public ResponseEntity<?> processRequest( String operation, Object requestPayload ) throws Exception{

        jamiiDebug.info("Received request for operation: " + operation);

        // Lookup the handler
        Object handler = directoryMap.get(operation);

        if ( handler instanceof ReviewUsOPS){
            ( (ReviewUsOPS) handler).reset( );
            return ( (ReviewUsOPS) handler).run( requestPayload );
        }

        if ( handler instanceof UserFileDirectoryUpdateOPS){
            ( (UserFileDirectoryUpdateOPS) handler).reset( );
            return ( (UserFileDirectoryUpdateOPS) handler).run( requestPayload );
        }

        if ( handler instanceof UserFileDeleteOPS){
            ( (UserFileDeleteOPS) handler).reset( );
            return ( (UserFileDeleteOPS) handler).run( requestPayload );
        }

        if ( handler instanceof UserFileDownloadOPS){
            ( (UserFileDownloadOPS) handler).reset( );
            return ( (UserFileDownloadOPS) handler).run( requestPayload );
        }

        if( handler instanceof  ChangePasswordOPS ){
            ( (ChangePasswordOPS) handler).reset( );
            return ( (ChangePasswordOPS) handler).run( requestPayload );
        }

        if( handler instanceof  EditUserDataOPS ){
            ( (EditUserDataOPS) handler).reset( );
            return ( ( EditUserDataOPS ) handler).run( requestPayload );
        }

        if( handler instanceof  FetchUserDataOPS ){
            ( (FetchUserDataOPS) handler).reset( );
            return ( ( FetchUserDataOPS ) handler).run( requestPayload );
        }

        if( handler instanceof DeactivateUserOPS){
            ( (DeactivateUserOPS) handler).reset( );
            return ( ( DeactivateUserOPS ) handler).run( requestPayload );
        }

        if( handler instanceof UserLogoffOPS){
            ( (UserLogoffOPS) handler).reset( );
            return ( ( UserLogoffOPS ) handler).run( requestPayload );
        }

        throw  new Exception( operation );
    }

    public ResponseEntity<?> processMultipartRequest(String operation, String userKey, String deviceKey, String sessionKey, MultipartFile file) throws Exception {

        jamiiDebug.info("Received request for operation: " + operation);

        // Lookup the handler
        Object handler = directoryMap.get(operation);

        if ( handler instanceof UserFileUploadOPS){
            ( (UserFileUploadOPS) handler).reset( );
            return ( (UserFileUploadOPS) handler).run(JamiiMapperUtils.mapUploadFileObject( userKey, deviceKey, sessionKey, file ) );
        }

        throw new Exception( operation );
    }
}
