package com.jamii.services.social;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.controllers.UserDataCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.social.SearchUserREQ;
import com.jamii.responses.social.SearchUserRESP;
import com.jamii.services.social.utils.SocialHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchUsersOPS extends socialAbstract{

    private SearchUserREQ searchUserREQ;
    private HashMap< String, SocialHelper.SearchResults > searchResults = new HashMap<>( );

    public SearchUserREQ getSearchUserREQ() {
        return searchUserREQ;
    }

    public void setSearchUserREQ(SearchUserREQ searchUserREQ) {
        this.searchUserREQ = searchUserREQ;
    }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserDataCONT userDataCONT;


    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getSearchUserREQ( ).getDevicekey( );
        UserKey = getSearchUserREQ( ).getUserkey( );
        super.validateCookie( );
    }

    /**
     * @throws Exception
     */
    @Override
    public void processRequest( ) throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        //Fetch list of users based of User Login Information
        List< UserLoginTBL > userLogins = new ArrayList<>( );
        userLogins.addAll( this.userLoginCONT.searchUserUsername( this.searchUserREQ.getSearchstring( ) ) );
        userLogins.addAll( this.userLoginCONT.searchUserEmailAddress( this.searchUserREQ.getSearchstring( ) ) );
        for( UserLoginTBL user : userLogins ){

            SocialHelper.SearchResults obj = new SocialHelper.SearchResults( );
            Optional<UserDataTBL> userdata = this.userDataCONT.fetch( user, UserDataTBL.CURRENT_STATUS_ON );

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
        List< UserDataTBL > userDatas = new ArrayList<>( );
        userDatas.addAll( this.userDataCONT.searchUserFirstname( this.searchUserREQ.getSearchstring( ) ) );
        userDatas.addAll( this.userDataCONT.searchUserMiddlename( this.searchUserREQ.getSearchstring( ) ) );
        userDatas.addAll( this.userDataCONT.searchUserLastname( this.searchUserREQ.getSearchstring( ) ) );
        for( UserDataTBL userdata : userDatas ){

            Optional<UserLoginTBL> user = this.userLoginCONT.fetch( userdata.getId( ), UserLoginTBL.ACTIVE_ON );
            if( !user.isPresent( ) ){
                continue;
            }

            if( !this.searchResults.containsKey( user.get( ).getUserKey( ) ) ){
                SocialHelper.SearchResults obj = new SocialHelper.SearchResults();

                obj.setUSERNAME( user.get( ).getUsername( ) );
                obj.setUSER_KEY( user.get( ).getUserKey( ) );
                obj.setFIRSTNAME( userdata.getFirstname( ) );
                obj.setLASTNAME( userdata.getLastname( ) );

                this.searchResults.put( user.get( ).getUserKey( ), obj );
            }
        }
    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( this.isSuccessful ){

            List< String > response = new ArrayList<>( );
            for( Map.Entry< String , SocialHelper.SearchResults > entry : this.searchResults.entrySet( ) ){
                SearchUserRESP resp = new SearchUserRESP( );
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
        this.searchResults = new HashMap<>( );
    }


}
