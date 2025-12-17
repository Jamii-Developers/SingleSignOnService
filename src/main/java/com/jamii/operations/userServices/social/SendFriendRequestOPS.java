package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiRelationshipUtils;
import com.jamii.databaseconfig.controllers.UserLogin;
import com.jamii.databaseconfig.controllers.UserRequest;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.databaseconfig.model.UserRequestsTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.SendFriendRequestServicesREQ;
import com.jamii.responses.userResponses.socialResponses.SendFriendRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SendFriendRequestOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserRequest userRequest;
    @Autowired private JamiiRelationshipUtils jamiiRelationshipUtils;

    @Override
    public void validateCookie()
            throws Exception
    {
        SendFriendRequestServicesREQ req = (SendFriendRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), SendFriendRequestServicesREQ.class);
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

        SendFriendRequestServicesREQ req = (SendFriendRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), SendFriendRequestServicesREQ.class);

        // Check if both users exist in the system
        this.userLogin.data = new UserLoginTBL();
        this.userLogin.otherUser = new UserLoginTBL();

        this.userLogin.data = this.userLogin.fetchByUserKey(UserKey, UserLogin.ACTIVE_ON).orElse(null);
        this.userLogin.otherUser = this.userLogin.fetchByUserKey(req.getFriendKey(), UserLogin.ACTIVE_ON).orElse(null);
        if (this.userLogin.data == null || this.userLogin.otherUser == null) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
        }

        jamiiRelationshipUtils.setSender(this.userLogin.data);
        jamiiRelationshipUtils.setReceiver(this.userLogin.otherUser);
        jamiiRelationshipUtils.initRelationShip();

        //Check if a friend request has been by the user sent to the receiver
        if (jamiiRelationshipUtils.checkIfUserHasPendingFriendRequest()) {
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_FriendRequestIsAlreadyAvailable();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        //Check if a friend request has already been sent by the receiver
        if (jamiiRelationshipUtils.checkifUserHasAPendingRequestFriendFromReceiver()) {
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_FriendRequestHasBeenSentByTheReceiver();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        //Check if the users are already friends
        if (jamiiRelationshipUtils.checkIfUsersAreFriends()) {
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_AreAlreadyFriends();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        //Check if sender has been blocked
        if (jamiiRelationshipUtils.checkIfUserIsBlocked()) {
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_BlockedUserVagueResponse();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        //Check is sender blocked this receiver
        if (jamiiRelationshipUtils.checkIfUserHasBlockedReceiver()) {
            this.jamiiErrorsMessagesRESP.setSendFriendRequestOPS_YouHaveBlockedThisUser();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Create User Request
        this.userRequest.data = new UserRequestsTBL();
        this.userRequest.data.setSenderid(this.userLogin.data);
        this.userRequest.data.setReceiverid(this.userLogin.otherUser);
        this.userRequest.data.setType(UserRequest.TYPE_FRIEND);
        this.userRequest.data.setStatus(UserRequest.STATUS_ACTIVE);
        this.userRequest.data.setDateupdated(LocalDateTime.now());
        this.userRequest.save();
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {
            SendFriendRequestRESP sendFriendRequestRESP = new SendFriendRequestRESP(this.userLogin.otherUser);
            return new ResponseEntity<>(sendFriendRequestRESP.getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
