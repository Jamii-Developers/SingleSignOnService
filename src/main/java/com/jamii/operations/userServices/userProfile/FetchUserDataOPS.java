package com.jamii.operations.userServices.userProfile;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.controllers.UserDataCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServices;
import com.jamii.requests.userServices.socialREQ.FetchUserDataREQ;
import com.jamii.responses.userResponses.profileResponses.FetchUserProfileRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FetchUserDataOPS extends AbstractUserServices {

    public FetchUserDataOPS( ) { }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserDataCONT userDataCONT;


    private FetchUserDataREQ fetchUserDataREQ;

    private FetchUserProfileRESP fetchUserProfileRESP;

    public FetchUserDataREQ getFetchUserDataREQ() {
        return fetchUserDataREQ;
    }

    public void setFetchUserDataREQ(FetchUserDataREQ fetchUserDataREQ) {
        this.fetchUserDataREQ = fetchUserDataREQ;
    }

    public FetchUserProfileRESP getFetchUserDataRESP() {
        return fetchUserProfileRESP;
    }

    public void setFetchUserDataRESP(FetchUserProfileRESP fetchUserProfileRESP) {
        this.fetchUserProfileRESP = fetchUserProfileRESP;
    }

    @Override
    public ResponseEntity<?> run(Object requestPayload) throws Exception {
        jamiiDebug.info("Received request" );
        FetchUserDataREQ req = (FetchUserDataREQ) requestPayload;
        return super.run( requestPayload );
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getFetchUserDataREQ().getDeviceKey();
        UserKey = getFetchUserDataREQ().getUserKey();
        SessionKey = getFetchUserDataREQ().getSessionkey();
        super.validateCookie( );
    }

    @Override
    public void processRequest() throws Exception {

        if( !this.isSuccessful ){
            return;
        }

        Optional<UserLoginTBL> sender = this.userLoginCONT.fetchByUserKey( UserKey, UserLoginTBL.ACTIVE_ON );

        if( sender.isPresent( ) ){

            Optional<UserDataTBL> userdata = this.userDataCONT.fetch(sender.get(), UserDataTBL.CURRENT_STATUS_ON);

            fetchUserProfileRESP = new FetchUserProfileRESP( );
            if (userdata.isPresent( ) ) {
                fetchUserProfileRESP.setFirstname( JamiiStringUtils.getSafeString( userdata.get().getFirstname( ) ) );
                fetchUserProfileRESP.setLastname( JamiiStringUtils.getSafeString( userdata.get().getLastname( ) ) );
                fetchUserProfileRESP.setMiddlename( JamiiStringUtils.getSafeString( userdata.get().getMiddlename( ) ) );
                fetchUserProfileRESP.setAddress1( JamiiStringUtils.getSafeString( userdata.get().getAddress1( ) ) );
                fetchUserProfileRESP.setAddress2( JamiiStringUtils.getSafeString( userdata.get().getAddress2( ) ) );
                fetchUserProfileRESP.setCity( JamiiStringUtils.getSafeString( userdata.get().getCity( ) ) );
                fetchUserProfileRESP.setState( JamiiStringUtils.getSafeString( userdata.get().getState( ) ) );
                fetchUserProfileRESP.setProvince( JamiiStringUtils.getSafeString( userdata.get().getProvince( ) ) );
                fetchUserProfileRESP.setCountry( JamiiStringUtils.getSafeString( userdata.get().getCountry( ) ) );
                fetchUserProfileRESP.setZipcode( JamiiStringUtils.getSafeString( userdata.get().getZipcode( ) ) );
                fetchUserProfileRESP.setPrivacy( sender.get( ).getPrivacy( ) );
            }else {
                this.jamiiErrorsMessagesRESP.setFetchUserData_NoData( );
                this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
                this.isSuccessful = false;
            }

        }else {
            this.jamiiErrorsMessagesRESP.setFetchUserData_GenericError( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            this.isSuccessful = false;
        }

    }

    @Override
    public ResponseEntity< ? > getResponse( ) {

        if( this.isSuccessful ){
            return new ResponseEntity<>( this.getFetchUserDataRESP().getJSONRESP(), HttpStatus.OK );
        }

        return super.getResponse( );
    }

    @Override
    public void reset() {
        super.reset( );
        setFetchUserDataRESP( null );
    }


}
