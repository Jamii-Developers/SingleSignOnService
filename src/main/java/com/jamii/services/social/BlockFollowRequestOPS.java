package com.jamii.services.social;

import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.social.BlockFollowRequestREQ;
import com.jamii.responses.social.BlockFollowRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlockFollowRequestOPS extends socialAbstract{

    private BlockFollowRequestREQ blockFollowRequestREQ;
    private Optional<UserLoginTBL> receiver;

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserRelationshipCONT userRelationshipCONT;

    public void setBlockFollowRequestREQ(BlockFollowRequestREQ blockFollowRequestREQ) {
        this.blockFollowRequestREQ = blockFollowRequestREQ;
    }

    public BlockFollowRequestREQ getBlockFollowRequestREQ() {
        return blockFollowRequestREQ;
    }

    @Override
    public void processRequest() throws Exception {

        DeviceKey = getBlockFollowRequestREQ( ).getDevicekey( );
        UserKey = getBlockFollowRequestREQ( ).getUserkey( );

        super.processRequest( );

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        receiver = this.userLoginCONT.fetch( getBlockFollowRequestREQ( ).getReceiveruserkey( ), UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) || receiver.isEmpty( )){
            this.jamiiErrorsMessagesRESP.setRemoveFollowRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
        }

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){
            BlockFollowRequestRESP blockFollowRequestRESP = new BlockFollowRequestRESP( receiver.get( ) );
            return  new ResponseEntity< >( blockFollowRequestRESP.getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.receiver = Optional.empty( );
    }
}
