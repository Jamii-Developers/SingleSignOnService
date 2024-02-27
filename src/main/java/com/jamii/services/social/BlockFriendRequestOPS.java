package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.social.BlockFriendRequestREQ;
import com.jamii.responses.social.BlockFriendRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlockFriendRequestOPS extends socialAbstract{

    private BlockFriendRequestREQ blockFriendRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;

    public void setBlockFriendRequestREQ(BlockFriendRequestREQ blockFriendRequestREQ) {
        this.blockFriendRequestREQ = blockFriendRequestREQ;
    }

    public BlockFriendRequestREQ getBlockFriendRequestREQ() {
        return blockFriendRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

        DeviceKey = getBlockFriendRequestREQ( ).getDevicekey( );
        UserKey = getBlockFriendRequestREQ( ).getUserkey( );

        super.processRequest( );

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetch( getBlockFriendRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setRemoveFollowRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
        }

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){
            BlockFriendRequestRESP blockFriendRequestRESP = new BlockFriendRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( blockFriendRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.receiver = Optional.empty( );
    }
}
