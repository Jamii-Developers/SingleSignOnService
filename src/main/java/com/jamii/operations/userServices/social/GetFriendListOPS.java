package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.controllers.UserDataCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRelationshipCONT;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.operations.userServices.social.Utils.SocialHelper;
import com.jamii.requests.userServices.socialREQ.GetFriendListServicesREQ;
import com.jamii.responses.userResponses.socialResponses.GetFriendListRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetFriendListOPS extends AbstractSocial {

    private GetFriendListServicesREQ getFriendListREQ;
    private HashMap< String, SocialHelper.RelationShipResults> relationshipResults = new HashMap<>( );

    public GetFriendListServicesREQ getGetFriendListREQ() {
        return getFriendListREQ;
    }

    public void setGetFriendListREQ(GetFriendListServicesREQ getFriendListREQ) {
        this.getFriendListREQ = getFriendListREQ;
    }

    @Autowired
    private UserRelationshipCONT userRelationshipCONT;
    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserDataCONT userDataCONT;

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getGetFriendListREQ().getDeviceKey();
        UserKey = getGetFriendListREQ().getUserKey();
        SessionKey = getGetFriendListREQ().getSessionKey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetchByUserKey( UserKey, UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        // Get friends from relationship table
        List<UserRelationshipTBL> relationships = new ArrayList<>( );
        relationships.addAll( userRelationshipCONT.fetch( sender.get( ), UserRelationshipTBL.TYPE_FRIEND, UserRelationshipTBL.STATUS_ACTIVE ) );


        //Get the necessary relationships and fetch the user information
        for( UserRelationshipTBL relationship : relationships){

            SocialHelper.RelationShipResults obj = new SocialHelper.RelationShipResults( );
            UserLoginTBL user = null;

            if( Objects.equals( relationship.getSenderid( ).getId( ), sender.get( ).getId( ) ) ) {
                user = relationship.getReceiverid( );
            }else if( Objects.equals( relationship.getReceiverid( ).getId( ), sender.get( ).getId( ) ) ){
                user = relationship.getSenderid( );
            }

            Optional<UserDataTBL> userdata = this.userDataCONT.fetch( user, UserDataTBL.CURRENT_STATUS_ON );
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

        if(this.relationshipResults.isEmpty( ) ){
            this.jamiiErrorsMessagesRESP.setGetFriendList_NoFriends( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){

            List< String > response = new ArrayList<>( );
            for( Map.Entry< String , SocialHelper.RelationShipResults > entry : this.relationshipResults.entrySet( ) ){
                GetFriendListRESP resp = new GetFriendListRESP( );
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
