package com.jamii.jSocial.services.relationshipManagement;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jSocial.controllers.UserRelationship;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jSocial.model.UserRelationshipTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.UnfollowServicesREQ;
import com.jamii.jSocial.responses.UnFollowRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Service for unfollowing users from the authenticated user's follow list.
 * 
 * <p>This service allows authenticated users to unfollow users they are currently
 * following, deactivating the follow relationship. The operation requires valid
 * session authentication and an existing active follow relationship.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Verify both users exist and are active</li>
 *   <li>Fetch and validate the follow relationship exists</li>
 *   <li>Deactivate the follow relationship</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>Users not found or inactive</li>
 *   <li>No active follow relationship found</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see UnfollowServicesREQ
 */
@Service
public class UnfollowOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserRelationship userRelationship;
    
    /** Request object containing unfollow request data */
    protected UnfollowServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link UnfollowServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new UnfollowServicesREQ();
        req = (UnfollowServicesREQ) JamiiMapperUtils.mapObject(getRequest(), UnfollowServicesREQ.class);
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

        //Find the Follower Relationship in the system
        this.userRelationship.dataList = new ArrayList<>();
        this.userRelationship.dataList.addAll(this.userRelationship.fetch(this.userLogin.data, this.userLogin.otherUser, UserRelationship.TYPE_FOLLOW, UserRelationship.STATUS_ACTIVE));

        if (this.userRelationship.data != null) {
            for (UserRelationshipTBL relationship : this.userRelationship.dataList) {
                relationship.setStatus(UserRelationship.STATUS_INACTIVE);
                relationship.setDateupdated(LocalDateTime.now());
            }
            this.userRelationship.saveAll();
        }
        else {
            this.jamiiErrorsMessagesRESP.set_Unfollow_NotFollowing();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {

            return new ResponseEntity<>(new UnFollowRESP(this.userLogin.otherUser).getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
