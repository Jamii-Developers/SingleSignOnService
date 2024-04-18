package com.jamii.operations.social.FunctionOPS;

import com.jamii.jamiidb.controllers.UserBlockListCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.controllers.UserRequestCONT;
import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.requests.social.FunctionREQ.BlockUserRequestREQ;
import com.jamii.responses.social.FunctionRESP.BlockUserRequestRESP;
import com.jamii.operations.social.AbstractSocial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlockUserRequestOPS extends AbstractSocial {

    private BlockUserRequestREQ blockUserRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;
    @Autowired
    private UserRequestCONT userRequestCONT;
    @Autowired
    private UserBlockListCONT userBlockListCONT;

    public void setBlockUserRequestREQ(BlockUserRequestREQ blockUserRequestREQ) {
        this.blockUserRequestREQ = blockUserRequestREQ;
    }

    public BlockUserRequestREQ getBlockUserRequestREQ() {
        return blockUserRequestREQ;
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getBlockUserRequestREQ( ).getDevicekey( );
        UserKey = getBlockUserRequestREQ( ).getUserkey( );
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetchByUserKey( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetchByUserKey( getBlockUserRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setBlockUserRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Fetch requests to user
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequestCONT.fetch( sender.get( ), receiver.get( ) ) );
        requests.addAll( userRequestCONT.fetch( receiver.get( ), sender.get( ) ) );
        //Fetch Block List
        List<UserBlockListTBL> blockList = new ArrayList<>( );
        blockList.addAll( userBlockListCONT.fetch( sender.get( ), receiver.get( ), UserBlockListTBL.STATUS_ACTIVE ) );
        //Fetch Relationships
        List<UserRelationshipTBL> relationship = new ArrayList<>( );
        relationship.addAll( userRelationshipCONT.fetch( sender.get( ), receiver.get( )  ) );
        relationship.addAll( userRelationshipCONT.fetch(  receiver.get( ), sender.get( ) ) );

        //Deactivate any active relationship
        if( !relationship.isEmpty( ) ){
            for( UserRelationshipTBL rlshp : relationship ){
                rlshp.setStatus( UserRelationshipTBL.STATUS_INACTIVE );
                rlshp.setDateupdated( LocalDateTime.now( ) );
                userRelationshipCONT.update( rlshp );
            }

        }

        //Deactivate any requests
        if( !requests.isEmpty( ) ){
            for( UserRequestsTBL req : requests ) {
                req.setStatus( UserRequestsTBL.STATUS_INACTIVE );
                req.setDateupdated(LocalDateTime.now());
                userRequestCONT.update( req );
            }
        }

        //Set any follow relationships the receiver has with the sender
        if( !blockList.isEmpty( ) ){
            for( UserBlockListTBL blcklst : blockList ){
                blcklst.setStatus( UserBlockListTBL.STATUS_ACTIVE );
                blcklst.setDateupdated( LocalDateTime.now( ) );
                userBlockListCONT.update( blcklst );
            }
        }else{
            userBlockListCONT.add( sender.get( ), receiver.get( ), UserBlockListTBL.STATUS_ACTIVE );
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
