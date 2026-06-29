package com.jamii.jSocial.services.relationshipManagement;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.utils.JamiiRelationshipUtils;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jSocial.controllers.UserRequest;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jSocial.model.UserRequestsTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.SendFriendRequestServicesREQ;
import com.jamii.jSocial.responses.SendFriendRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service for sending friend requests to other users.
 * 
 * <p>This service allows authenticated users to send friend requests,
 * creating pending friend relationships that can be accepted or rejected.
 * The operation requires valid session authentication and handles various
 * relationship scenarios including blocks and existing relationships.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Verify both users exist and are active</li>
 *   <li>Check for blocking relationships</li>
 *   <li>Check for existing friend requests/relationships</li>
 *   <li>Create friend request</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>Users not found or inactive</li>
 *   <li>User is blocked or has blocked target</li>
 *   <li>Existing friend request or relationship</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see SendFriendRequestServicesREQ
 */
@Service
public class SendFriendRequestOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserRequest userRequest;
    @Autowired private JamiiRelationshipUtils jamiiRelationshipUtils;
    
    /** Request object containing friend request data */
    protected SendFriendRequestServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link SendFriendRequestServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new SendFriendRequestServicesREQ();
        req = (SendFriendRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), SendFriendRequestServicesREQ.class);
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

        // Request parameters are already mapped in setUserRequestData()

        // Check if both jUser exist in the system
        this.userLogin.data = new UserLoginTBL();
        this.userLogin.otherUser = new UserLoginTBL();

        this.userLogin.data = this.cookie.getValidatedUser();
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

        //Check if the jUser are already friends
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
