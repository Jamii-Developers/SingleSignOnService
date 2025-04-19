package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.controllers.UserBlockList;
import com.jamii.jamiidb.controllers.UserData;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.operations.userServices.social.Utils.SocialHelper;
import com.jamii.requests.userServices.socialREQ.GetBlockUserListServicesREQ;
import com.jamii.responses.userResponses.socialResponses.GetFollowListRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetBlockUserListOPS extends AbstractUserServicesOPS {

    private HashMap< String, SocialHelper.RelationShipResults > relationshipResults = new HashMap<>( );

    @Autowired
    private UserBlockList userBlockList;
    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserData userData;

    @Override
    public void validateCookie( ) throws Exception{
        GetBlockUserListServicesREQ req = ( GetBlockUserListServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), GetBlockUserListServicesREQ.class );
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

        GetBlockUserListServicesREQ req = ( GetBlockUserListServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), GetBlockUserListServicesREQ.class );

        // Check if both users exist in the system
        this.userLogin.data = this.userLogin.fetchByUserKey( req.getUserKey( ), UserLogin.ACTIVE_ON ).orElse( null );
        if( this.userLogin.data == null  ){
            this.jamiiErrorsMessagesRESP.setAcceptFriendRequest_GenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        // Get friends from relationship table
        this.userBlockList.dataList.addAll( userBlockList.fetch( this.userLogin.data, this.userLogin.otherUser, UserBlockList.STATUS_ACTIVE ) );


        //Get the necessary relationships and fetch the user information
        for( UserBlockListTBL blockeduser : this.userBlockList.dataList ){

            SocialHelper.RelationShipResults obj = new SocialHelper.RelationShipResults( );
            UserLoginTBL user = blockeduser.getBlockedid();

            this.userData.data = this.userData.fetch( user, UserData.CURRENT_STATUS_ON ).orElse( null );
            if( this.userData.data != null ){

                obj.setUSERNAME( user.getUsername( ) );
                obj.setUSER_KEY( user.getUserKey( ) );
                obj.setFIRSTNAME( this.userData.data.getFirstname( ) );
                obj.setLASTNAME( this.userData.data.getLastname( ) );

                this.relationshipResults.put( user.getUserKey( ), obj );
            }else{
                obj.setUSERNAME( user.getUsername( ) );
                obj.setUSER_KEY( user.getUserKey( ) );
                obj.setFIRSTNAME( "N/A" );
                obj.setLASTNAME( "N/A" );

                this.relationshipResults.put( user.getUserKey( ), obj );
            }
        }

        if( this.relationshipResults.isEmpty( ) ){
            this.jamiiErrorsMessagesRESP.setGetBlockUserList_NoBlockedUsers( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){

            List< String > response = new ArrayList<>( );
            for( Map.Entry< String , SocialHelper.RelationShipResults > entry : this.relationshipResults.entrySet( ) ){
                GetFollowListRESP resp = new GetFollowListRESP( );
                resp.setUSERNAME( entry.getValue( ).getUSERNAME( ) );
                resp.setUSER_KEY( entry.getValue( ).getUSER_KEY( ) );
                resp.setFIRSTNAME( entry.getValue( ).getFIRSTNAME( ) );
                resp.setLASTNAME( entry.getValue( ).getLASTNAME( ) );
                response.add( resp.getJSONRESP( ) );
            }


            return  new ResponseEntity< >( JamiiStringUtils.separateWithDelimiter( response, ","), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.relationshipResults = new HashMap<>( );
    }
}
