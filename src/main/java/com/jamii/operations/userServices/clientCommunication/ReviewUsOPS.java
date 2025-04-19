package com.jamii.operations.userServices.clientCommunication;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.ClientCommunication;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.clientCommunicationREQ.ReviewUsServicesREQ;
import com.jamii.responses.userResponses.clientCommunication.ContactUsRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewUsOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private ClientCommunication clientCommunication;

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
        this.userLogin.data = this.userLogin.fetch( req.getEmailaddress( ), req.getUsername( ), UserLogin.ACTIVE_ON ).orElse( null ) ;

        if( this.userLogin.data == null ){
            jamiiDebug.warning( String.format( "This username or email address does not exist %s|%s ", req.getUsername( ), req.getEmailaddress() ) );
            this.jamiiErrorsMessagesRESP.setContactUsOPS_UserNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            return ;
        }

        this.clientCommunication.data.setUserloginid( this.userLogin.data );
        this.clientCommunication.data.setClientthoughts( req.getClient_thoughts( ) );
        this.clientCommunication.data.setTypeofthought( ClientCommunication.TYPE_OF_THOUGHT_REVIEW );
        this.clientCommunication.data.setDateofthought( LocalDateTime.now( ) );
        this.clientCommunication.save( );

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
