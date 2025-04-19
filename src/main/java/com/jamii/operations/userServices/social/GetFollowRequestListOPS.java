package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.controllers.UserData;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRequest;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.operations.userServices.social.Utils.SocialHelper;
import com.jamii.requests.userServices.socialREQ.GetFollowerRequestListServicesREQ;
import com.jamii.responses.userResponses.socialResponses.GetFollowRequestListRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetFollowRequestListOPS extends AbstractUserServicesOPS {

    private HashMap< String, SocialHelper.RelationShipResults> relationshipResults = new HashMap<>( );

    @Autowired
    private UserRequest userRequest;
    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserData userData;

    @Override
    public void validateCookie( ) throws Exception{
        GetFollowerRequestListServicesREQ req = ( GetFollowerRequestListServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), GetFollowerRequestListServicesREQ.class );
        setDeviceKey( req.getDeviceKey( ) );
        setUserKey( req.getUserKey( ) );
        setSessionKey( req.getSessionKey() );
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !getIsSuccessful() ){
            return;
        }

        GetFollowerRequestListServicesREQ req = ( GetFollowerRequestListServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), GetFollowerRequestListServicesREQ.class );

        // Check if both users exist in the system
        this.userLogin.data = this.userLogin.fetchByUserKey( req.getUserKey( ), UserLogin.ACTIVE_ON ).orElse( null );
        if( this.userLogin.data == null  ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        // Get friends from relationship table
        this.userRequest.dataList.addAll( userRequest.fetchRequests( this.userLogin.data , UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE ) );


        //Get the necessary relationships and fetch the user information
        for( UserRequestsTBL request : this.userRequest.dataList ){

            SocialHelper.RelationShipResults obj = new SocialHelper.RelationShipResults( );
            UserLoginTBL user = request.getSenderid( );

            Optional<UserDataTBL> userdata = this.userData.fetch( user, UserData.CURRENT_STATUS_ON );
            if( userdata.isPresent( ) ){

                obj.setUSERNAME( user.getUsername( ) );
                obj.setUSER_KEY( user.getUserKey( ) );
                obj.setFIRSTNAME( userdata.get( ).getFirstname( ) );
                obj.setLASTNAME( userdata.get( ).getLastname( ) );

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
            this.jamiiErrorsMessagesRESP.setGetFollowRequestList_NoNewFollowRequests( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){

            List< String > response = new ArrayList<>( );
            for( Map.Entry< String , SocialHelper.RelationShipResults > entry : this.relationshipResults.entrySet( ) ){
                GetFollowRequestListRESP resp = new GetFollowRequestListRESP( );
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
