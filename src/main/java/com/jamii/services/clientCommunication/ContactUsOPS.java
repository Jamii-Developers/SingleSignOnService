package com.jamii.services.clientCommunication;

import com.jamii.requests.clientCommunication.ContactUsREQ;
import com.jamii.responses.clientCommunication.ContactUsRESP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ContactUsOPS extends clientCommunicationAbstract{

    public ContactUsOPS() {
    }

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
