package com.jamii.services.social;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.controllers.UserBlockListCONT;
import com.jamii.jamiidb.controllers.UserDataCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.social.GetBlockUserListREQ;
import com.jamii.responses.social.GetFollowListRESP;
import com.jamii.services.social.utils.SocialHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetBlockUserListOPS extends socialAbstract{

    private GetBlockUserListREQ getBlockUserListREQ;
    private HashMap< String, SocialHelper.RelationShipResults > relationshipResults = new HashMap<>( );

    public GetBlockUserListREQ getGetBlockUserListREQ() {
        return getBlockUserListREQ;
    }

    public void setGetBlockUserListREQ(GetBlockUserListREQ getBlockUserListREQ) {
        this.getBlockUserListREQ = getBlockUserListREQ;
    }

    @Autowired
    private UserBlockListCONT userBlockListCONT;
    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserDataCONT userDataCONT;

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getGetBlockUserListREQ().getDevicekey();
        UserKey = getGetBlockUserListREQ().getUserkey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetch( UserKey, UserLoginTBL.ACTIVE_ON );
        if( sender.isEmpty( ) ){
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_GenerateGenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

        // Get friends from relationship table
        List<UserBlockListTBL> blockedUsers = new ArrayList<>( );
        blockedUsers.addAll( userBlockListCONT.fetchBlockedList( sender.get( ), UserBlockListTBL.STATUS_ACTIVE ) );


        //Get the necessary relationships and fetch the user information
        for( UserBlockListTBL blockeduser : blockedUsers){

            SocialHelper.RelationShipResults obj = new SocialHelper.RelationShipResults( );
            UserLoginTBL user = blockeduser.getBlockedid();

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
