package com.jamii.operations.userServices.clientCommunication;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.databaseconfig.controllers.ClientCommunication;
import com.jamii.databaseconfig.controllers.UserLogin;
import com.jamii.databaseconfig.model.ClientCommunicationTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.clientCommunicationREQ.ContactSupportServicesREQ;
import com.jamii.responses.userResponses.clientCommunication.ContactSupportRESP;
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

    @Override
    public void validateCookie()
            throws Exception
    {
        ContactSupportServicesREQ req = (ContactSupportServicesREQ) JamiiMapperUtils.mapObject(getRequest(), ContactSupportServicesREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
        super.validateCookie();
    }

    @Override
    public void processRequest()
            throws Exception
    {

        if (!getIsSuccessful()) {
            return;
        }

        ContactSupportServicesREQ req = (ContactSupportServicesREQ) JamiiMapperUtils.mapObject(getRequest(), ContactSupportServicesREQ.class);
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
        this.clientCommunication.data.setTypeofthought(ClientCommunication.TYPE_OF_THOUGHT_REVIEW);
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
