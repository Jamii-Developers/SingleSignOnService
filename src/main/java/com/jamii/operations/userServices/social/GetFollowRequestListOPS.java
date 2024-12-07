package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.controllers.UserDataCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.controllers.UserRequestCONT;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.operations.userServices.social.Utils.SocialHelper;
import com.jamii.requests.userServices.socialREQ.GetFollowerRequestListServicesREQ;
import com.jamii.responses.userResponses.socialResponses.GetFollowRequestListRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetFollowRequestListOPS extends AbstractSocial {

    private GetFollowerRequestListServicesREQ getFollowerRequestListREQ;
    private HashMap< String, SocialHelper.RelationShipResults> relationshipResults = new HashMap<>( );

    public GetFollowerRequestListServicesREQ getGetFollowerRequestListREQ() {
        return getFollowerRequestListREQ;
    }

    public void setGetFollowerRequestListREQ(GetFollowerRequestListServicesREQ getFollowerRequestListREQ) {
        this.getFollowerRequestListREQ = getFollowerRequestListREQ;
    }

    @Autowired
    private UserRequestCONT userRequestCONT;
    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserDataCONT userDataCONT;

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getGetFollowerRequestListREQ().getDeviceKey();
        UserKey = getGetFollowerRequestListREQ().getUserKey();
        SessionKey = getGetFollowerRequestListREQ().getSessionkey();
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
        List<UserRequestsTBL> requests = new ArrayList<>( );
        requests.addAll( userRequestCONT.fetchRequests( sender.get(), UserRequestsTBL.TYPE_FOLLOW, UserRequestsTBL.STATUS_ACTIVE ) );


        //Get the necessary relationships and fetch the user information
        for( UserRequestsTBL request : requests){

            SocialHelper.RelationShipResults obj = new SocialHelper.RelationShipResults( );
            UserLoginTBL user = request.getSenderid( );

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
