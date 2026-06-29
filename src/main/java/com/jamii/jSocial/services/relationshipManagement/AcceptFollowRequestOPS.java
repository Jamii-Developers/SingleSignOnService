package com.jamii.jSocial.services.relationshipManagement;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jSocial.controllers.UserRelationship;
import com.jamii.jSocial.controllers.UserRequest;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jSocial.model.UserRelationshipTBL;
import com.jamii.jSocial.model.UserRequestsTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.AcceptFollowRequestServicesREQ;
import com.jamii.jSocial.responses.AcceptFollowRequestRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Service for accepting follow requests from other users.
 * 
 * <p>This service allows authenticated users to accept follow requests,
 * creating a follower relationship between the users. The operation requires
 * valid session authentication and an existing active follow request.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Verify both users exist and are active</li>
 *   <li>Fetch and validate the follow request exists</li>
 *   <li>Deactivate the follow request</li>
 *   <li>Create the follower relationship</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>Users not found or inactive</li>
 *   <li>No active follow request found</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see AcceptFollowRequestServicesREQ
 */
@Service
public class AcceptFollowRequestOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserRelationship userRelationship;
    @Autowired private UserRequest userRequest;
    
    /** Request object containing follow request acceptance data */
    protected AcceptFollowRequestServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link AcceptFollowRequestServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new AcceptFollowRequestServicesREQ();
        req = (AcceptFollowRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), AcceptFollowRequestServicesREQ.class);
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
