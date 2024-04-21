package com.jamii.operations.activedirectory.FetchOPS;

import com.jamii.Utils.JamiiStringUtils;
import com.jamii.jamiidb.controllers.UserDataCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.activedirectory.AbstractFetchOPS;
import com.jamii.requests.activeDirectory.FetchREQ.FetchUserDataREQ;
import com.jamii.responses.activeDirectory.FetchRESP.FetchUserDataRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FetchUserDataOPS extends AbstractFetchOPS {

    public FetchUserDataOPS( ) { }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private UserDataCONT userDataCONT;


    private FetchUserDataREQ fetchUserDataREQ;

    private FetchUserDataRESP fetchUserDataRESP;

    public FetchUserDataREQ getFetchUserDataREQ() {
        return fetchUserDataREQ;
    }

    public void setFetchUserDataREQ(FetchUserDataREQ fetchUserDataREQ) {
        this.fetchUserDataREQ = fetchUserDataREQ;
    }

    public FetchUserDataRESP getFetchUserDataRESP() {
        return fetchUserDataRESP;
    }

    public void setFetchUserDataRESP(FetchUserDataRESP fetchUserDataRESP) {
        this.fetchUserDataRESP = fetchUserDataRESP;
    }

    @Override
    public void validateCookie( ) throws Exception{
        DeviceKey = getFetchUserDataREQ().getDevicekey();
        UserKey = getFetchUserDataREQ().getUserkey();
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

            fetchUserDataRESP = new FetchUserDataRESP( );
            if (userdata.isPresent( ) ) {
                fetchUserDataRESP.setFirstname( JamiiStringUtils.getSafeString( userdata.get().getFirstname( ) ) );
                fetchUserDataRESP.setLastname( JamiiStringUtils.getSafeString( userdata.get().getLastname( ) ) );
                fetchUserDataRESP.setMiddlename( JamiiStringUtils.getSafeString( userdata.get().getMiddlename( ) ) );
                fetchUserDataRESP.setAddress1( JamiiStringUtils.getSafeString( userdata.get().getAddress1( ) ) );
                fetchUserDataRESP.setAddress2( JamiiStringUtils.getSafeString( userdata.get().getAddress2( ) ) );
                fetchUserDataRESP.setCity( JamiiStringUtils.getSafeString( userdata.get().getCity( ) ) );
                fetchUserDataRESP.setState( JamiiStringUtils.getSafeString( userdata.get().getState( ) ) );
                fetchUserDataRESP.setProvince( JamiiStringUtils.getSafeString( userdata.get().getProvince( ) ) );
                fetchUserDataRESP.setCountry( JamiiStringUtils.getSafeString( userdata.get().getCountry( ) ) );
                fetchUserDataRESP.setZipcode( JamiiStringUtils.getSafeString( userdata.get().getZipcode( ) ) );
                fetchUserDataRESP.setPrivacy( sender.get( ).getPrivacy( ) );
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
