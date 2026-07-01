package com.jamii.jSocial.services.relationshipManagement;

import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.peer.UserRelationship;
import com.jamii.jSocial.model.UserRelationshipTBL;
import com.jamii.jSocial.requests.GetFollowerListServicesREQ;
import com.jamii.jSocial.responses.GetFollowListRESP;
import com.jamii.jSocial.services.utils.SearchResultsHelper;
import com.jamii.jUser.peer.UserData;
import com.jamii.jUser.peer.UserLogin;
import com.jamii.jUser.model.UserDataTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.utils.JamiiMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Service for retrieving the list of followers and following users for an authenticated user.
 * 
 * <p>This service allows authenticated users to retrieve their complete follow network,
 * showing both users they follow and users who follow them along with their profile
 * information and relationship type. The operation requires valid session authentication.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Verify user exists and is active</li>
 *   <li>Fetch active follow relationships from database</li>
 *   <li>Determine relationship type (follower/following) for each</li>
 *   <li>Retrieve profile information for each user</li>
 *   <li>Compile and return results</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found or inactive</li>
 *   <li>No followers or following relationships found</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see GetFollowerListServicesREQ
 */
@Service
public class GetFollowerListOPS
        extends AbstractUserServicesOPS
{

    private ArrayList<SearchResultsHelper.FollowRelationShipResults> relationshipResults = new ArrayList<>();

    @Autowired private UserRelationship userRelationship;
    @Autowired private UserLogin userLogin;
    @Autowired private UserData userData;
    
    /** Request object containing follower list request data */
    protected GetFollowerListServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link GetFollowerListServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new GetFollowerListServicesREQ();
        req = (GetFollowerListServicesREQ) JamiiMapperUtils.mapObject(getRequest(), GetFollowerListServicesREQ.class);
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
        this.userLogin.data = this.userLogin.fetchByUserKey(req.getUserKey(), UserLogin.ACTIVE_ON).orElse(null);
        if (this.userLogin.data == null) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
        }

        // Get friends from relationship table
        this.userRelationship.dataList = new ArrayList<>();
        this.userRelationship.dataList.addAll(userRelationship.fetch(this.userLogin.data, UserRelationship.STATUS_ACTIVE, UserRelationship.TYPE_FOLLOW));

        //Get the necessary relationships and fetch the user information
        // Optimize by collecting all users first, then batch fetch user data
        List<UserLoginTBL> relationshipUsers = this.userRelationship.dataList.stream()
                .map(relationship -> {
                    if (Objects.equals(relationship.getSenderid().getId(), this.userLogin.data.getId())) {
                        return relationship.getReceiverid();
                    } else {
                        return relationship.getSenderid();
                    }
                })
                .toList();
        
        // Create a map for quick lookup of user data
        Map<UserLoginTBL, Optional<UserDataTBL>> userDataMap = new HashMap<>();
        for (UserLoginTBL user : relationshipUsers) {
            userDataMap.put(user, this.userData.fetch(user, UserData.CURRENT_STATUS_ON));
        }
        
        // Process results with batch-fetched data
        for (UserRelationshipTBL relationship : this.userRelationship.dataList) {
            SearchResultsHelper.FollowRelationShipResults obj = new SearchResultsHelper.FollowRelationShipResults();
            
            UserLoginTBL user;
            if (Objects.equals(relationship.getSenderid().getId(), this.userLogin.data.getId())) {
                user = relationship.getReceiverid();
                obj.setTypeOfFollower("following");
            } else {
                user = relationship.getSenderid();
                obj.setTypeOfFollower("follower");
            }
            
            Optional<UserDataTBL> userdataOpt = userDataMap.get(user);
            if (userdataOpt != null && userdataOpt.isPresent()) {
                UserDataTBL userdata = userdataOpt.get();
                obj.setUSERNAME(user.getUsername());
                obj.setUSER_KEY(user.getUserKey());
                obj.setFIRSTNAME(userdata.getFirstname());
                obj.setLASTNAME(userdata.getLastname());
            } else {
                obj.setUSERNAME(user.getUsername());
                obj.setUSER_KEY(user.getUserKey());
                obj.setFIRSTNAME("N/A");
                obj.setLASTNAME("N/A");
            }
            
            this.relationshipResults.add(obj);
        }

        if (this.relationshipResults.isEmpty()) {
            this.jamiiErrorsMessagesRESP.setGetFollowList_NoFollowers();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {

            GetFollowListRESP response = new GetFollowListRESP();
            for (SearchResultsHelper.FollowRelationShipResults res : this.relationshipResults) {
                GetFollowListRESP.Results resp = new GetFollowListRESP.Results();
                resp.setUsername(res.getUSERNAME());
                resp.setUserKey(res.getUSER_KEY());
                resp.setFirstname(res.getFIRSTNAME());
                resp.setLastname(res.getLASTNAME());
                resp.setTypeOfFollow(res.getTypeOfFollower());
                response.getResults().add(resp);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return super.getResponse();
    }

    @Override
    public void reset()
    {
        super.reset();
        this.relationshipResults = new ArrayList<>();
    }
}
