package com.jamii.jSocial.services.relationshipManagement;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jSocial.controllers.UserRelationship;
import com.jamii.jSocial.controllers.UserRequest;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jSocial.model.UserRelationshipTBL;
import com.jamii.jSocial.model.UserRequestsTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.AcceptFriendRequestServicesREQ;
import com.jamii.jSocial.responses.AcceptFriendRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Service for accepting friend requests from other users.
 * 
 * <p>This service allows authenticated users to accept friend requests,
 * creating a mutual friendship relationship between the users. The operation requires
 * valid session authentication and an existing active friend request.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Verify both users exist and are active</li>
 *   <li>Fetch and validate the friend request exists</li>
 *   <li>Deactivate the friend request</li>
 *   <li>Create the friendship relationship</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>Users not found or inactive</li>
 *   <li>No active friend request found</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see AcceptFriendRequestServicesREQ
 */
@Service
public class AcceptFriendRequestOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserRelationship userRelationship;
    @Autowired private UserRequest userRequest;
    
    /** Request object containing friend request acceptance data */
    protected AcceptFriendRequestServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link AcceptFriendRequestServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new AcceptFriendRequestServicesREQ();
        req = (AcceptFriendRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), AcceptFriendRequestServicesREQ.class);
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
        this.userLogin.otherUser = this.userLogin.fetchByUserKey(req.getTargetUserKey(), UserLogin.ACTIVE_ON).orElse(null);
        if (this.userLogin.data == null || this.userLogin.otherUser == null) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
        }

        //Fetch friend request to user
        this.userRequest.dataList = new ArrayList<>();
        this.userRequest.dataList.addAll(userRequest.fetch(this.userLogin.otherUser, this.userLogin.data, UserRequest.TYPE_FRIEND, UserRequest.STATUS_ACTIVE));

        //Check if friend request exists
        Optional<UserRequestsTBL> validFriendRequest = this.userRequest.dataList.stream().filter(x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE) && x.getReceiverid() == this.userLogin.data).findFirst();
        if (validFriendRequest.isPresent()) {

            // Deactivate Follow Request
            this.userRequest.data = validFriendRequest.get();
            this.userRequest.data.setStatus(UserRequest.STATUS_INACTIVE);
            this.userRequest.data.setDateupdated(LocalDateTime.now());
            this.userRequest.save();

            //Create Relationship
            this.userRelationship.data = new UserRelationshipTBL();
            this.userRelationship.data.setReceiverid(this.userLogin.data);
            this.userRelationship.data.setSenderid(this.userLogin.otherUser);
            this.userRelationship.data.setType(UserRelationship.TYPE_FRIEND);
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

        if (this.isSuccessful && this.userLogin.otherUser != null) {
            AcceptFriendRequestRESP acceptFriendRequestRESP = new AcceptFriendRequestRESP(this.userLogin.otherUser);
            return new ResponseEntity<>(acceptFriendRequestRESP.getJSONRESP(), HttpStatus.OK);
        }
        else {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
        }

        return super.getResponse();
    }
}
