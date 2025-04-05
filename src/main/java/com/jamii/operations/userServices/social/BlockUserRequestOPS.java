package com.jamii.operations.userServices.social;

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
import java.util.List;
import java.util.Optional;

@Service
public class BlockUserRequestOPS extends AbstractUserServicesOPS {

    private BlockUserRequestServicesREQ blockUserRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserRelationship userRelationship;
    @Autowired
    private UserRequest userRequest;
    @Autowired
    private UserBlockList userBlockList;

    public void setBlockUserRequestREQ(BlockUserRequestServicesREQ blockUserRequestREQ) {
        this.blockUserRequestREQ = blockUserRequestREQ;
    }

    public BlockUserRequestServicesREQ getBlockUserRequestREQ() {
        return blockUserRequestREQ;
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getBlockUserRequestREQ( ).getDeviceKey( );
        UserKey = getBlockUserRequestREQ( ).getUserKey( );
        SessionKey = getBlockUserRequestREQ().getSessionKey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON );
        receiver = this.userLogin.fetchByUserKey( getBlockUserRequestREQ( ).getReceiveruserkey( ), UserLogin.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setBlockUserRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Fetch requests to user
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequest.fetch( sender.get( ), receiver.get( ) ) );
        requests.addAll( userRequest.fetch( receiver.get( ), sender.get( ) ) );
        //Fetch Block List
        List<UserBlockListTBL> blockList = new ArrayList<>( );
        blockList.addAll( userBlockList.fetch( sender.get( ), receiver.get( ), UserBlockList.STATUS_ACTIVE ) );
        //Fetch Relationships
        List<UserRelationshipTBL> relationship = new ArrayList<>( );
        relationship.addAll( userRelationship.fetch( sender.get( ), receiver.get( )  ) );
        relationship.addAll( userRelationship.fetch(  receiver.get( ), sender.get( ) ) );

        //Deactivate any active relationship
        if( !relationship.isEmpty( ) ){
            for( UserRelationshipTBL rlshp : relationship ){
                rlshp.setStatus( UserRelationship.STATUS_INACTIVE );
                rlshp.setDateupdated( LocalDateTime.now( ) );
                userRelationship.update( rlshp );
            }

        }

        //Deactivate any requests
        if( !requests.isEmpty( ) ){
            for( UserRequestsTBL req : requests ) {
                req.setStatus( UserRequest.STATUS_INACTIVE );
                req.setDateupdated(LocalDateTime.now());
                userRequest.update( req );
            }
        }

        //Set any follow relationships the receiver has with the sender
        if( !blockList.isEmpty( ) ){
            for( UserBlockListTBL blcklst : blockList ){
                blcklst.setStatus( UserBlockList.STATUS_ACTIVE );
                blcklst.setDateupdated( LocalDateTime.now( ) );
                userBlockList.update( blcklst );
            }
        }else{
            userBlockList.add( sender.get( ), receiver.get( ), UserBlockList.STATUS_ACTIVE );
        }

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful && receiver.isPresent( ) ){
            BlockUserRequestRESP blockUserRequestRESP = new BlockUserRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( blockUserRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }else{
            this.jamiiErrorsMessagesRESP.setBlockUserRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.receiver = Optional.empty( );
    }
}
