package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.databaseconfig.controllers.UserLogin;
import com.jamii.databaseconfig.controllers.UserRequest;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.databaseconfig.model.UserRequestsTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.RejectFollowRequestServicesREQ;
import com.jamii.responses.userResponses.socialResponses.RejectFollowRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class RejectFollowRequestOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserRequest userRequest;

    @Override
    public void validateCookie()
            throws Exception
    {
        RejectFollowRequestServicesREQ req = (RejectFollowRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), RejectFollowRequestServicesREQ.class);
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

        RejectFollowRequestServicesREQ req = (RejectFollowRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), RejectFollowRequestServicesREQ.class);

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

        //Fetch requests to user
        this.userRequest.dataList = new ArrayList<>();
        this.userRequest.dataList.addAll(userRequest.fetch(this.userLogin.otherUser, this.userLogin.data, UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE));

        //Check if friend request exists
        Optional<UserRequestsTBL> validFollowRequest = this.userRequest.dataList.stream().filter(x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE) && x.getReceiverid() == this.userLogin.data).findFirst();
        if (validFollowRequest.isPresent()) {

            // Deactivate the request
            this.userRequest.data = validFollowRequest.get();
            this.userRequest.data.setStatus(UserRequest.STATUS_INACTIVE);
            this.userRequest.data.setDateupdated(LocalDateTime.now());
            this.userRequest.save();
        }
        else {
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful() && this.userLogin.otherUser != null) {
            RejectFollowRequestRESP rejectFollowRequestRESP = new RejectFollowRequestRESP(this.userLogin.otherUser);
            return new ResponseEntity<>(rejectFollowRequestRESP.getJSONRESP(), HttpStatus.OK);
        }
        else {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
        }

        return super.getResponse();
    }
}
