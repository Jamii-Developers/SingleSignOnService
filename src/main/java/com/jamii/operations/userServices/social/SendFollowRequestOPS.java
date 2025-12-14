package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiRelationshipUtils;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRelationship;
import com.jamii.jamiidb.controllers.UserRequest;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.SendFollowRequestServicesREQ;
import com.jamii.responses.userResponses.socialResponses.SendFollowRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class SendFollowRequestOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserRelationship userRelationship;
    @Autowired private UserRequest userRequest;
    @Autowired private JamiiRelationshipUtils jamiiRelationshipUtils;

    private Integer followerType;

    @Override
    public void validateCookie()
            throws Exception
    {
        SendFollowRequestServicesREQ req = (SendFollowRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), SendFollowRequestServicesREQ.class);
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

        SendFollowRequestServicesREQ req = (SendFollowRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), SendFollowRequestServicesREQ.class);

        // Check if both users exist in the system
        this.userLogin.data = new UserLoginTBL();
        this.userLogin.otherUser = new UserLoginTBL();
        this.userLogin.data = this.userLogin.fetchByUserKey(UserKey, UserLogin.ACTIVE_ON).orElse(null);
        this.userLogin.otherUser = this.userLogin.fetchByUserKey(req.getFollowKey(), UserLogin.ACTIVE_ON).orElse(null);
        if (this.userLogin.data == null || this.userLogin.otherUser == null) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
        }

        jamiiRelationshipUtils.setSender(this.userLogin.data);
        jamiiRelationshipUtils.setReceiver(this.userLogin.otherUser);
        jamiiRelationshipUtils.initRelationShip();

        //Check if the user has been blocked by the receiver
        if (jamiiRelationshipUtils.checkIfUserIsBlocked()) {
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_BlockedUserVagueResponse();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        //Check is user has blocked this receiver
        if (jamiiRelationshipUtils.checkIfUserHasBlockedReceiver()) {
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_YouHaveBlockedThisUser();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        // Check if the user has a pending follow request
        if (jamiiRelationshipUtils.checkIfUserHasPendingFollowRequest()) {
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_PendingFollowRequest();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        //Check if user is already following this user
        if (jamiiRelationshipUtils.checkIfUserIsFollowing()) {
            this.jamiiErrorsMessagesRESP.setSendFollowRequestOPS_AlreadyFollowingTheUser();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        if (Objects.equals(this.userLogin.otherUser.getPrivacy(), UserLogin.PRIVACY_ON)) {
            followerType = 0;
            this.userRequest.data = new UserRequestsTBL();
            this.userRequest.data.setSenderid(this.userLogin.data);
            this.userRequest.data.setReceiverid(this.userLogin.otherUser);
            this.userRequest.data.setType(UserRequest.TYPE_FOLLOW);
            this.userRequest.data.setStatus(UserRequest.STATUS_ACTIVE);
            this.userRequest.data.setDateupdated(LocalDateTime.now());
            this.userRequest.save();
        }
        else {
            followerType = 1;
            this.userRelationship.data = new UserRelationshipTBL();
            this.userRelationship.data.setSenderid(this.userLogin.data);
            this.userRelationship.data.setReceiverid(this.userLogin.otherUser);
            this.userRelationship.data.setType(UserRelationship.TYPE_FOLLOW);
            this.userRelationship.data.setStatus(UserRelationship.STATUS_ACTIVE);
            this.userRelationship.data.setDateupdated(LocalDateTime.now());
            this.userRelationship.save();
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {
            SendFollowRequestRESP sendFollowRequestRESP = new SendFollowRequestRESP();
            sendFollowRequestRESP.init(followerType, this.userLogin.otherUser);
            return new ResponseEntity<>(sendFollowRequestRESP.getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
