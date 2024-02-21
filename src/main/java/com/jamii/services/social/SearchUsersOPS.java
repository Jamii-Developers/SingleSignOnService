package com.jamii.services.social;

import com.jamii.Utils.JamiiCookieProcessor;
import com.jamii.jamiidb.controllers.UserDataCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.social.SearchUserREQ;
import com.jamii.responses.social.SearchUserRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchUsersOPS extends socialAbstract{

    private SearchUserREQ searchUserREQ;
    private SearchUserRESP searchUserRESP;
    private Boolean isSuccessful = false;
    private HashMap< UserLoginTBL, UserDataTBL > searchResults = new HashMap<>();

    public SearchUserREQ getSearchUserREQ() {
        return searchUserREQ;
    }

    public void setSearchUserREQ(SearchUserREQ searchUserREQ) {
        this.searchUserREQ = searchUserREQ;
    }

    public SearchUserRESP getSearchUserRESP() {
        return searchUserRESP;
    }

    public void setSearchUserRESP(SearchUserRESP searchUserRESP) {
        this.searchUserRESP = searchUserRESP;
    }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserDataCONT userDataCONT;
    @Autowired
    private JamiiCookieProcessor cookie;

    /**
     * @throws Exception
     */
    @Override
    public void processRequest( ) throws Exception {

        //Check if cookie information is available
        if(  this.searchUserREQ.getDevicekey( )==null || this.searchUserREQ.getUserkey( )==null ){
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        if(  this.searchUserREQ.getDevicekey( ).isEmpty( ) || this.searchUserREQ.getUserkey( ).isEmpty( ) ){
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        //Check if user cookie is valid
        cookie.setUSER_KEY( this.searchUserREQ.getUserkey( ) );
        cookie.setDEVICE_KEY( this.searchUserREQ.getDevicekey( ) );

        if( !cookie.checkCookieIsValid( ) ){
            this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return;
        }

        //Fetch list of users based of User Login Information
        List< UserLoginTBL > userLogins = new ArrayList<>( );
        userLogins.addAll( this.userLoginCONT.searchUserUsername( this.searchUserREQ.getSearchstring( ) ) );
        userLogins.addAll( this.userLoginCONT.searchUserEmailAddress( this.searchUserREQ.getSearchstring( ) ) );
        for( UserLoginTBL user : userLogins ){

            Optional<UserDataTBL> userdata = this.userDataCONT.fetch( user, UserDataTBL.CURRENT_STATUS_ON );
            if( !searchResults.containsKey( user ) ){
                if( userdata.isPresent( ) ){
                    searchResults.put( user, userdata.get( ) );
                }else{
                    searchResults.put( user, null );
                }

            }
        }

        //Fetch list based of User Data Information
        List< UserDataTBL > userDatas = new ArrayList<>( );this.userDataCONT.searchUser( this.searchUserREQ.getSearchstring( ) );
        for( UserDataTBL userdata : userDatas ){

            Optional<UserLoginTBL> user = this.userLoginCONT.fetch( userdata.getId(), UserLoginTBL.ACTIVE_ON );
            if( !searchResults.containsKey( user ) ){
                searchResults.put( user.get( ), userdata );
            }
        }

        this.isSuccessful = true;
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){

            if( searchResults.isEmpty( ) ){
                super.getResponse( );
            }

            for( Map.Entry< UserLoginTBL, UserDataTBL> entry :searchResults.entrySet( ) ){
                getSearchUserRESP( ).getUSER_KEY( ).add( entry.getKey( ).getUserKey( ) );
                getSearchUserRESP( ).getFIRSTNAME( ).add( entry.getValue( ).getFirstname( ) );
                getSearchUserRESP( ).getLASTNAME( ).add( entry.getValue( ).getLastname( ) );
                getSearchUserRESP( ).getUSERNAME( ).add( entry.getKey( ).getUsername( ) );
            }

            return  new ResponseEntity< >( getSearchUserRESP( ).getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse( );
    }
}
