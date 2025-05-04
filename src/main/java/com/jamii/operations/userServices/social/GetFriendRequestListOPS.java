package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.UserData;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRequest;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.operations.userServices.social.Utils.SearchResultsHelper;
import com.jamii.requests.userServices.socialREQ.GetFriendRequestListServicesREQ;
import com.jamii.responses.userResponses.socialResponses.GetFriendRequestListRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class GetFriendRequestListOPS extends AbstractUserServicesOPS {

    private HashMap< String, SearchResultsHelper.RelationShipResults> relationshipResults = new HashMap<>( );

    @Autowired
    private UserRequest userRequest;
    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserData userData;

    @Override
    public void validateCookie( ) throws Exception{
        GetFriendRequestListServicesREQ req = ( GetFriendRequestListServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), GetFriendRequestListServicesREQ.class );
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

        GetFriendRequestListServicesREQ req = ( GetFriendRequestListServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), GetFriendRequestListServicesREQ.class );

        // Check if both users exist in the system
        this.userLogin.data = this.userLogin.fetchByUserKey( req.getUserKey( ), UserLogin.ACTIVE_ON ).orElse( null );
        if( this.userLogin.data == null  ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        // Get friends from relationship table
        this.userRequest.dataList = new ArrayList< >( );
        this.userRequest.dataList.addAll( userRequest.fetchRequests( this.userLogin.data, UserRequest.TYPE_FRIEND, UserRequest.STATUS_ACTIVE ) );


        //Get the necessary relationships and fetch the user information
        for( UserRequestsTBL request : this.userRequest.dataList){

            SearchResultsHelper.RelationShipResults obj = new SearchResultsHelper.RelationShipResults( );
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
            this.jamiiErrorsMessagesRESP.setGetFriendRequestList_NoNewFriendRequests( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( getIsSuccessful() ){

            GetFriendRequestListRESP response = new GetFriendRequestListRESP( );
            for( Map.Entry< String , SearchResultsHelper.RelationShipResults > entry : this.relationshipResults.entrySet( ) ){
                GetFriendRequestListRESP.Results resp = new GetFriendRequestListRESP.Results();
                resp.setUsername( entry.getValue( ).getUSERNAME( ) );
                resp.setUserKey( entry.getValue( ).getUSER_KEY( ) );
                resp.setFirstname( entry.getValue( ).getFIRSTNAME( ) );
                resp.setLastname( entry.getValue( ).getLASTNAME( ) );
                response.getResults( ).add( resp );
            }

            return new ResponseEntity< >( response, HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.relationshipResults = new HashMap<>( );
    }
}
