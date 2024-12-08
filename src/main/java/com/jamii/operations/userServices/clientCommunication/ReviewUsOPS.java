package com.jamii.operations.userServices.clientCommunication;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.ClientCommunicationCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.ClientCommunicationTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.clientCommunicationREQ.ReviewUsServicesREQ;
import com.jamii.responses.userResponses.clientCommunication.ContactUsRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReviewUsOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private ClientCommunicationCONT clientCommunicationCONT ;

    private ContactUsRESP contactUsRESP;
    private ContactUsRESP getContactUsRESP() {
        return contactUsRESP;
    }
    public void setContactUsRESP( ContactUsRESP contactUsRESP) {
        this.contactUsRESP = contactUsRESP;
    }

    @Override
    public void reset( ){
        super.reset();
    }

    @Override
    public void validateCookie( ) throws Exception{
        ReviewUsServicesREQ req = ( ReviewUsServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), ReviewUsServicesREQ.class );
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

        ReviewUsServicesREQ req = ( ReviewUsServicesREQ ) JamiiMapperUtils.mapObject( getRequest( ), ReviewUsServicesREQ.class );
        Optional<UserLoginTBL> user = this.userLoginCONT.fetch( req.getEmailaddress( ), req.getUsername( ), UserLoginTBL.ACTIVE_ON ) ;
        if( user.isEmpty( ) ){
            jamiiDebug.warning( String.format( "This username or email address does not exist %s|%s ", req.getUsername( ), req.getEmailaddress() ) );
            this.jamiiErrorsMessagesRESP.setContactUsOPS_UserNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        ClientCommunicationTBL contactus = new ClientCommunicationTBL( );
        contactus.setUserloginid( user.get( ) );
        contactus.setClientthoughts( req.getClient_thoughts( ) );
        contactus.setTypeofthought( ClientCommunicationTBL.TYPE_OF_THOUGHT_CONTACT_US );
        contactus.setDateofthought( LocalDateTime.now( ) );
        this.clientCommunicationCONT.save( contactus );

    }

    @Override
    public ResponseEntity<?> getResponse( ){

        if( getIsSuccessful( ) ){
            setContactUsRESP( new ContactUsRESP( ) );
            return  new ResponseEntity< >( getContactUsRESP( ).getJSONRESP( ), HttpStatus.OK ) ;
        }

        return super.getResponse();
    }
}
