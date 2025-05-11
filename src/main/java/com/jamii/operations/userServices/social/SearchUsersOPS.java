package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiRelationshipUtils;
import com.jamii.jamiidb.controllers.UserData;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.operations.userServices.social.Utils.SearchResultsHelper;
import com.jamii.requests.userServices.socialREQ.SearchUserServicesREQ;
import com.jamii.responses.userResponses.socialResponses.SearchUserRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchUsersOPS extends AbstractUserServicesOPS {

    private HashMap< String, SearchResultsHelper.SearchResults > searchResults = new HashMap<>( );

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserData userData;
    @Autowired
    private JamiiRelationshipUtils jamiiRelationshipUtils;

    @Override
    public void validateCookie( ) throws Exception{
        SearchUserServicesREQ req = ( SearchUserServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), SearchUserServicesREQ.class );
        setDeviceKey( req.getDeviceKey( ) );
        setUserKey( req.getUserKey( ) );
        setSessionKey( req.getSessionKey() );
        super.validateCookie( );
    }

    @Override
    public void processRequest( ) throws Exception {

        if( !getIsSuccessful( ) ){
            return;
        }

        SearchUserServicesREQ req = ( SearchUserServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), SearchUserServicesREQ.class );

        this.userLogin.data = new UserLoginTBL( );
        this.userLogin.data = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON ).orElse( null );
        if( this.userLogin.data == null ){
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
            return;
        }

        //Fetch list of users based of User Login Information
        this.userLogin.dataList = new ArrayList< >( );
        this.userLogin.dataList.addAll( this.userLogin.searchUserUsername( req.getSearchstring( ) ) );
        this.userLogin.dataList.addAll( this.userLogin.searchUserEmailAddress( req.getSearchstring( ) ) );
        for( UserLoginTBL user : this.userLogin.dataList ){

            if( Objects.equals( this.userLogin.data.getId( ), user.getId( ) ) ){
                continue;
            }

            SearchResultsHelper.SearchResults obj = new SearchResultsHelper.SearchResults( );
            Optional<UserDataTBL> userdata = this.userData.fetch( user, UserData.CURRENT_STATUS_ON );

            jamiiRelationshipUtils.setSender( this.userLogin.data );
            jamiiRelationshipUtils.setReceiver( user );
            jamiiRelationshipUtils.initRelationShip( ) ;

            if( jamiiRelationshipUtils.checkIfUserIsBlocked( )){
                continue;
            }

            if( jamiiRelationshipUtils.checkIfUserHasBlockedReceiver( ) ){
                continue;
            }

            if( jamiiRelationshipUtils.checkIfUsersAreFriends(  )){
                obj.setFriend( true );
            }

            if( jamiiRelationshipUtils.checkIfUserIsFollowing(  )){
                obj.setFollowing( true );
            }

            if( jamiiRelationshipUtils.checkIfUserHasPendingFriendRequest(  )){
                obj.setHasPendingFriendRequest( true );
            }

            if( jamiiRelationshipUtils.checkIfUserHasPendingFollowRequest(  )){
                obj.setHasPendingFollowingRequest( true );
            }

            if( !this.searchResults.containsKey( user.getUserKey( ) ) ){

                if( userdata.isPresent( ) ){

                    obj.setUSERNAME( user.getUsername( ) );
                    obj.setUSER_KEY( user.getUserKey( ) );
                    obj.setFIRSTNAME( userdata.get( ).getFirstname( ) );
                    obj.setLASTNAME( userdata.get( ).getLastname( ) );

                    this.searchResults.put( user.getUserKey( ), obj );
                }else{
                    obj.setUSERNAME( user.getUsername( ) );
                    obj.setUSER_KEY( user.getUserKey( ) );
                    obj.setFIRSTNAME( "N/A" );
                    obj.setLASTNAME( "N/A" );

                    this.searchResults.put( user.getUserKey( ), obj );
                }
            }
        }

        //Fetch list based of User Data Information
        this.userData.dataList = new ArrayList< >( );
        this.userData.dataList.addAll( this.userData.searchUserFirstname( req.getSearchstring( ) ) );
        this.userData.dataList.addAll( this.userData.searchUserMiddlename( req.getSearchstring( ) ) );
        this.userData.dataList.addAll( this.userData.searchUserLastname( req.getSearchstring( ) ) );

        for( UserDataTBL userdata : this.userData.dataList ){

            UserLoginTBL user = this.userLogin.fetchByUserKey( userdata.getUserloginid().getUserKey( ) , UserLogin.ACTIVE_ON ).orElse( null );
            if( user == null ){
                continue;
            }

            if( Objects.equals(this.userLogin.data.getId( ), user.getId( ) ) ){
                continue;
            }

            SearchResultsHelper.SearchResults obj = new SearchResultsHelper.SearchResults();

            jamiiRelationshipUtils.setSender( this.userLogin.data );
            jamiiRelationshipUtils.setReceiver( user );
            jamiiRelationshipUtils.initRelationShip( ) ;

            if( jamiiRelationshipUtils.checkIfUserIsBlocked( )){
                continue;
            }

            if( jamiiRelationshipUtils.checkIfUserHasBlockedReceiver( ) ){
                continue;
            }

            if( jamiiRelationshipUtils.checkIfUsersAreFriends(  )){
                obj.setFriend( true );
            }

            if( jamiiRelationshipUtils.checkIfUserIsFollowing(  )){
                obj.setFollowing( true );
            }

            if( jamiiRelationshipUtils.checkIfUserHasPendingFriendRequest(  )){
                obj.setHasPendingFriendRequest( true );
            }

            if( jamiiRelationshipUtils.checkIfUserHasPendingFollowRequest(  )){
                obj.setHasPendingFollowingRequest( true );
            }

            if( !this.searchResults.containsKey( user.getUserKey( ) ) ){

                obj.setUSERNAME( user.getUsername( ) );
                obj.setUSER_KEY( user.getUserKey( ) );
                obj.setFIRSTNAME( userdata.getFirstname( ) );
                obj.setLASTNAME( userdata.getLastname( ) );

                this.searchResults.put( user.getUserKey( ), obj );
            }
        }

        setIsSuccessful( true );
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( getIsSuccessful( ) ){

            SearchUserRESP resp = new SearchUserRESP( );
            for( Map.Entry< String , SearchResultsHelper.SearchResults > entry : this.searchResults.entrySet( ) ){
                SearchUserRESP.Results res = new SearchUserRESP.Results( );
                res.setUsername( entry.getValue( ).getUSERNAME( ) );
                res.setUserKey( entry.getValue( ).getUSER_KEY( ) );
                res.setFirstname( entry.getValue( ).getFIRSTNAME( ) );
                res.setLastname( entry.getValue( ).getLASTNAME( ) );
                res.setFriend( entry.getValue().isFriend( ) );
                res.setFollowing( entry.getValue().isFollowing( ) );
                res.setHasPendingFriendRequest( entry.getValue().isHasPendingFriendRequest( ) );
                res.setHasPendingFollowingRequest( entry.getValue( ).isHasPendingFollowingRequest( ));
                resp.getResults().add( res );
            }

            return  new ResponseEntity< >( resp , HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }

    @Override
    public void reset( ){
        super.reset( );
        this.searchResults = new HashMap<>( );
    }

}
