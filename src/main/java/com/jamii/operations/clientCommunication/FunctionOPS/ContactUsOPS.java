package com.jamii.operations.clientCommunication.FunctionOPS;

import com.jamii.Utils.JamiiDebug;
import com.jamii.jamiidb.controllers.ClientCommunicationCONT;
import com.jamii.jamiidb.controllers.UserLoginCONT;
import com.jamii.jamiidb.model.ClientCommunicationTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.clientCommunication.FunctionREQ.ContactUsREQ;
import com.jamii.responses.clientCommunication.ContactUsRESP;
import com.jamii.operations.clientCommunication.clientCommunicationAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ContactUsOPS extends clientCommunicationAbstract {

    public ContactUsOPS() {
    }

    @Autowired
    private UserLoginCONT userLoginCONT;
    @Autowired
    private ClientCommunicationCONT clientCommunicationCONT ;

    private ContactUsREQ contactUsREQ;
    private ContactUsRESP contactUsRESP;
    protected static Boolean isSuccessful = false;

    public ContactUsREQ getContactUsREQ() {
        return contactUsREQ;
    }

    public void setContactUsREQ(ContactUsREQ contactUsREQ) {
        this.contactUsREQ = contactUsREQ;
    }

    public static Boolean getIsSuccessful() {
        return isSuccessful;
    }

    public static void setIsSuccessful(Boolean isSuccessful) {
        ContactUsOPS.isSuccessful = isSuccessful;
    }

    public ContactUsRESP getContactUsRESP() {
        return contactUsRESP;
    }

    public void setContactUsRESP(ContactUsRESP contactUsRESP) {
        this.contactUsRESP = contactUsRESP;
    }

    @Override
    public void reset( ){
        super.reset();
        setIsSuccessful( false );
        setContactUsREQ( null );
        setContactUsRESP( null );
    }

    @Override
    public void processRequest() throws IOException {

        Optional<UserLoginTBL> user = this.userLoginCONT.fetch( getContactUsREQ( ).getEmailaddress( ), getContactUsREQ( ).getUsername( ), UserLoginTBL.ACTIVE_ON ) ;
        if( user.isEmpty( ) ){
            JamiiDebug.warning( String.format( "This username or email address does not exist %s|%s ", getContactUsREQ( ).getUsername( ), getContactUsREQ( ).getEmailaddress() ) );
            this.jamiiErrorsMessagesRESP.setContactUsOPS_UserNotFound( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
            setIsSuccessful(false);
            return ;
        }

        ClientCommunicationTBL contactus = new ClientCommunicationTBL( );
        contactus.setUserloginid( user.get( ) );
        contactus.setClientthoughts( getContactUsREQ().getClient_thoughts( ) );
        contactus.setTypeofthought( ClientCommunicationTBL.TYPE_OF_THOUGHT_CONTACT_US );
        contactus.setDateofthought( LocalDateTime.now( ) );
        this.clientCommunicationCONT.save( contactus );

        setIsSuccessful( true );
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
