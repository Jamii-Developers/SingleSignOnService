package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.databaseconfig.controllers.UserLogin;
import com.jamii.databaseconfig.controllers.UserRelationship;
import com.jamii.databaseconfig.controllers.UserRequest;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.databaseconfig.model.UserRelationshipTBL;
import com.jamii.databaseconfig.model.UserRequestsTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.AcceptFollowRequestServicesREQ;
import com.jamii.responses.userResponses.socialResponses.AcceptFollowRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class AcceptFollowRequestOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserRelationship userRelationship;
    @Autowired private UserRequest userRequest;

    @Override
    public void validateCookie()
            throws Exception
    {
        AcceptFollowRequestServicesREQ req = (AcceptFollowRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), AcceptFollowRequestServicesREQ.class);
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

        AcceptFollowRequestServicesREQ req = (AcceptFollowRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), AcceptFollowRequestServicesREQ.class);

        // Check if both users exist in the system
        this.userLogin.data = new UserLoginTBL();
        this.userLogin.otherUser = new UserLoginTBL();
        this.userLogin.data = this.userLogin.fetchByUserKey(UserKey, UserLogin.ACTIVE_ON).orElse(null);
        this.userLogin.otherUser = this.userLogin.fetchByUserKey(req.getTargetUserKey(), UserLogin.ACTIVE_ON).orElse(null);
        if (this.userLogin.data == null || this.userLogin.otherUser == null) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        //Fetch follow request from the sender
        this.userRequest.dataList = new ArrayList<>();
        this.userRequest.dataList.addAll(userRequest.fetch(this.userLogin.otherUser, this.userLogin.data, UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE));

        Optional<UserRequestsTBL> validFollowRequest = this.userRequest.dataList.stream().filter(x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE) && x.getReceiverid() == this.userLogin.data).findFirst();

        //Check if follow request exists
        if (validFollowRequest.isPresent()) {

            // Deactivate Follow Request
            this.userRequest.data = validFollowRequest.get();
            this.userRequest.data.setStatus(UserRequest.STATUS_INACTIVE);
            this.userRequest.data.setDateupdated(LocalDateTime.now());
            this.userRequest.save();

            //Create Relationship
            this.userRelationship.data = new UserRelationshipTBL();
            this.userRelationship.data.setReceiverid(this.userLogin.data);
            this.userRelationship.data.setSenderid(this.userLogin.otherUser);
            this.userRelationship.data.setType(UserRelationship.TYPE_FOLLOW);
            this.userRelationship.data.setStatus(UserRelationship.STATUS_ACTIVE);
            this.userRelationship.data.setDateupdated(LocalDateTime.now());
            this.userRelationship.save();
        }
        else {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful() && this.userLogin.otherUser != null) {
            AcceptFollowRequestRESP acceptFollowRequestRESP = new AcceptFollowRequestRESP(this.userLogin.otherUser);
            return new ResponseEntity<>(acceptFollowRequestRESP.getJSONRESP(), HttpStatus.OK);
        }
        else {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
        }

        return super.getResponse();
    }
}
