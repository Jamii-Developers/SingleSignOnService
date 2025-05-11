package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.UserBlockList;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRelationship;
import com.jamii.jamiidb.controllers.UserRequest;
import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.BlockUserRequestServicesREQ;
import com.jamii.responses.userResponses.socialResponses.BlockUserRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class BlockUserOPS extends AbstractUserServicesOPS {


    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserRelationship userRelationship;
    @Autowired
    private UserRequest userRequest;
    @Autowired
    private UserBlockList userBlockList;

    // BlockUserRequestServicesREQ
    @Override
    public void validateCookie( ) throws Exception{
        BlockUserRequestServicesREQ req = ( BlockUserRequestServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), BlockUserRequestServicesREQ.class );
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

        BlockUserRequestServicesREQ req = ( BlockUserRequestServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), BlockUserRequestServicesREQ.class );

        // Check if both users exist in the system
        this.userLogin.data = new UserLoginTBL( );
        this.userLogin.otherUser = new UserLoginTBL( );
        this.userLogin.data = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON ).orElse( null );
        this.userLogin.otherUser = this.userLogin.fetchByUserKey( req.getTargetUserKey( ), UserLogin.ACTIVE_ON ).orElse( null );
        if( this.userLogin.data == null  || this.userLogin.otherUser == null ){
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        //Fetch requests between users
        this.userRequest.dataList = new ArrayList< >( );
        this.userRequest.dataList.addAll( userRequest.fetch( this.userLogin.otherUser, this.userLogin.data, UserRequest.TYPE_FRIEND, UserRequest.STATUS_ACTIVE ) );
        this.userRequest.dataList.addAll( userRequest.fetch( this.userLogin.data, this.userLogin.otherUser, UserRequest.TYPE_FRIEND, UserRequest.STATUS_ACTIVE ) );
        this.userRequest.dataList.addAll( userRequest.fetch( this.userLogin.otherUser, this.userLogin.data, UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE ) );
        this.userRequest.dataList.addAll( userRequest.fetch( this.userLogin.data, this.userLogin.otherUser, UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE ) );

        //Fetch Block List
        this.userBlockList.dataList = new ArrayList<>( );
        this.userBlockList.dataList.addAll( userBlockList.fetch( this.userLogin.data, this.userLogin.otherUser, UserBlockList.STATUS_ACTIVE ) );

        //Fetch Relationships
        this.userRelationship.dataList = new ArrayList<>( );
        this.userRelationship.dataList.addAll( userRelationship.fetch( this.userLogin.data , this.userLogin.otherUser, UserRelationship.TYPE_FRIEND,UserRelationship.STATUS_ACTIVE ) );
        this.userRelationship.dataList.addAll( userRelationship.fetch( this.userLogin.otherUser, this.userLogin.data , UserRelationship.TYPE_FRIEND,UserRelationship.STATUS_ACTIVE ) );
        this.userRelationship.dataList.addAll( userRelationship.fetch( this.userLogin.data , this.userLogin.otherUser, UserRelationship.TYPE_FOLLOW,UserRelationship.STATUS_ACTIVE ) );
        this.userRelationship.dataList.addAll( userRelationship.fetch( this.userLogin.otherUser, this.userLogin.data , UserRelationship.TYPE_FOLLOW,UserRelationship.STATUS_ACTIVE ) );


        //Deactivate any active relationships
        if( !this.userRelationship.dataList.isEmpty( ) ){
            for( UserRelationshipTBL rlshp : this.userRelationship.dataList ){
                rlshp.setStatus( UserRelationship.STATUS_INACTIVE );
                rlshp.setDateupdated( LocalDateTime.now( ) );
            }
            this.userRelationship.saveAll( );
        }

        //Deactivate any requests
        if( !this.userRequest.dataList.isEmpty( ) ){
            for( UserRequestsTBL userReq : this.userRequest.dataList ) {
                userReq.setStatus( UserRequest.STATUS_INACTIVE );
                userReq.setDateupdated(LocalDateTime.now());
            }
            this.userRequest.saveAll( );
        }

        //Set any blocked ID for the other User ID back to active
        if( !this.userBlockList.dataList.isEmpty( ) ){
            for( UserBlockListTBL blcklst : this.userBlockList.dataList ){
                blcklst.setStatus( UserBlockList.STATUS_ACTIVE );
                blcklst.setDateupdated( LocalDateTime.now( ) );
            }
            this.userBlockList.saveAll( );
        }else{
            // If there is no blocked record available then create a new one and block the user
            this.userBlockList.data = new UserBlockListTBL( );
            this.userBlockList.data.setUserid( this.userLogin.data );
            this.userBlockList.data.setBlockedid( this.userLogin.otherUser);
            this.userBlockList.data.setStatus( UserBlockList.STATUS_ACTIVE );
            this.userBlockList.data.setDateupdated( LocalDateTime.now( ) );
            this.userBlockList.save( );
        }

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful && this.userLogin.otherUser != null  ){
            BlockUserRequestRESP blockUserRequestRESP = new BlockUserRequestRESP( this.userLogin.otherUser);
            return  new ResponseEntity< >( blockUserRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }else{
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
        }

        return super.getResponse( );
    }
}
