package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.controllers.UserData;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRelationship;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.GetFollowerListServicesREQ;
import com.jamii.responses.userResponses.socialResponses.GetFollowListRESP;
import com.jamii.operations.userServices.social.Utils.SocialHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetFollowerListOPS extends AbstractUserServicesOPS {

    private GetFollowerListServicesREQ getFollowerListREQ;
    private HashMap< String, SocialHelper.RelationShipResults > relationshipResults = new HashMap<>( );

    public GetFollowerListServicesREQ getGetFollowerListREQ() {
        return getFollowerListREQ;
    }

    public void setGetFollowerListREQ(GetFollowerListServicesREQ getFollowerListREQ) {
        this.getFollowerListREQ = getFollowerListREQ;
    }

    @Autowired
    private UserRelationship userRelationship;
    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserData userData;

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getGetFollowerListREQ().getDeviceKey();
        UserKey = getGetFollowerListREQ().getUserKey();
        SessionKey = getGetFollowerListREQ().getSessionKey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON );
        if( sender.isEmpty( ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        // Get friends from relationship table
        List<UserRelationshipTBL> relationships = new ArrayList<>( );
        relationships.addAll( userRelationship.fetchFollowers( sender.get( ), UserRelationship.TYPE_FOLLOW, UserRelationship.STATUS_ACTIVE ) );


        //Get the necessary relationships and fetch the user information
        for( UserRelationshipTBL relationship : relationships){

            SocialHelper.RelationShipResults obj = new SocialHelper.RelationShipResults( );
            UserLoginTBL user = relationship.getSenderid();

            Optional<UserDataTBL> userdata = this.userData.fetch( user, UserData.CURRENT_STATUS_ON );
            if( userdata.isPresent( ) ){

                obj.setUSERNAME( user.getUsername( ) );
                obj.setUSER_KEY( user.getUserKey( ) );
                obj.setFIRSTNAME( userdata.get( ).getFirstname( ) );
                obj.setLASTNAME( userdata.get( ).getLastname( ) );

                this.relationshipResults.put( sender.get( ).getUserKey( ), obj );
            }else{
                obj.setUSERNAME( user.getUsername( ) );
                obj.setUSER_KEY( user.getUserKey( ) );
                obj.setFIRSTNAME( "N/A" );
                obj.setLASTNAME( "N/A" );

                this.relationshipResults.put( user.getUserKey( ), obj );
            }
        }

        if( this.relationshipResults.isEmpty( ) ){
            this.jamiiErrorsMessagesRESP.setGetFollowList_NoFollowers( );
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
