package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRelationship;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.UnFriendServicesREQ;
import com.jamii.responses.userResponses.socialResponses.UnFriendRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UnFriendOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserRelationship userRelationship;

    @Override
    public void validateCookie( ) throws Exception{
        UnFriendServicesREQ req = (UnFriendServicesREQ) JamiiMapperUtils.mapObject( getRequest( ), UnFriendServicesREQ.class );
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

        UnFriendServicesREQ req = (UnFriendServicesREQ) JamiiMapperUtils.mapObject( getRequest( ), UnFriendServicesREQ.class );

        // Check if both users exist in the system
        this.userLogin.data = new UserLoginTBL( );
        this.userLogin.otherUser = new UserLoginTBL( );
        this.userLogin.data = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON ).orElse( null );
        this.userLogin.otherUser = this.userLogin.fetchByUserKey( req.getTargetUserKey( ), UserLogin.ACTIVE_ON ).orElse( null );
        if( this.userLogin.data == null  || this.userLogin.otherUser == null ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Find the Friend Relationship in the system
        this.userRelationship.dataList = new ArrayList< >( );
        this.userRelationship.dataList.addAll( this.userRelationship.fetch( this.userLogin.otherUser, this.userLogin.data, UserRelationship.TYPE_FRIEND, UserRelationship.STATUS_ACTIVE ) );
        this.userRelationship.dataList.addAll( this.userRelationship.fetch( this.userLogin.data, this.userLogin.otherUser, UserRelationship.TYPE_FRIEND, UserRelationship.STATUS_ACTIVE ) );

        //Deactivate all possible relationships
        if( this.userRelationship.data != null ){
            for( UserRelationshipTBL relationship : this.userRelationship.dataList ){
                relationship.setStatus( UserRelationship.STATUS_INACTIVE );
            }
            this.userRelationship.saveAll( );
        }else{
            this.jamiiErrorsMessagesRESP.set_UnFriend_NotFriends( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful( false );
        }

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( getIsSuccessful( ) ){
            return new ResponseEntity< >( new UnFriendRESP( this.userLogin.otherUser ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }
}
