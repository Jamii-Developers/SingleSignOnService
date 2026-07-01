package com.jamii.jSupport.services;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jSupport.peer.ClientCommunication;
import com.jamii.jUser.peer.UserLogin;
import com.jamii.jSupport.model.ClientCommunicationTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSupport.requests.ContactSupportServicesREQ;
import com.jamii.jSupport.responses.ContactSupportRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ContactSupportOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private ClientCommunication clientCommunication;
    
    protected ContactSupportServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link ContactSupportServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new ContactSupportServicesREQ();
        req = (ContactSupportServicesREQ) JamiiMapperUtils.mapObject(getRequest(), ContactSupportServicesREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
    }

    @Override
    public void processRequest()
            throws Exception
    {

        if (!getIsSuccessful()) {
            return;
        }

        // Request is already mapped in setUserRequestData()
        this.userLogin.data = this.userLogin.fetch(req.getEmailaddress(), req.getUsername(), UserLogin.ACTIVE_ON).orElse(null);

        if (this.userLogin.data == null) {
            jamiiDebug.warning(String.format("This username or email address does not exist %s|%s ", req.getUsername(), req.getEmailaddress()));
            this.jamiiErrorsMessagesRESP.setContactUsOPS_UserNotFound();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            return;
        }

        this.clientCommunication.data = new ClientCommunicationTBL();
        this.clientCommunication.data.setUserloginid(this.userLogin.data);
        this.clientCommunication.data.setClientthoughts(req.getClient_thoughts());
        this.clientCommunication.data.setTypeofthought(ClientCommunication.TYPE_OF_THOUGHT_CONTACT_SUPPORT);
        this.clientCommunication.data.setDateofthought(LocalDateTime.now());
        this.clientCommunication.save();
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {
            return new ResponseEntity<>(new ContactSupportRESP().getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
