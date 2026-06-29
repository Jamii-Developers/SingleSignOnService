package com.jamii.jSocial.services.relationshipManagement;

import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.controllers.UserRelationship;
import com.jamii.jSocial.model.UserRelationshipTBL;
import com.jamii.jSocial.requests.GetFriendListServicesREQ;
import com.jamii.jSocial.responses.GetFriendListRESP;
import com.jamii.jSocial.services.utils.SearchResultsHelper;
import com.jamii.jUser.controller.UserData;
import com.jamii.jUser.controller.UserLogin;
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
 * Service for retrieving the list of friends for an authenticated user.
 * 
 * <p>This service allows authenticated users to retrieve their complete friend list,
 * showing all users with whom they have mutual friendship relationships along with
 * their profile information. The operation requires valid session authentication.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Verify user exists and is active</li>
 *   <li>Fetch active friendship relationships from database</li>
 *   <li>Retrieve profile information for each friend</li>
 *   <li>Compile and return results</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found or inactive</li>
 *   <li>No friends found</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see GetFriendListServicesREQ
 */
@Service
public class GetFriendListOPS
        extends AbstractUserServicesOPS
{

    private HashMap<String, SearchResultsHelper.RelationShipResults> relationshipResults = new HashMap<>();

    @Autowired private UserRelationship userRelationship;
    @Autowired private UserLogin userLogin;
    @Autowired private UserData userData;
    
    /** Request object containing friend list request data */
    protected GetFriendListServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link GetFriendListServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new GetFriendListServicesREQ();
        req = (GetFriendListServicesREQ) JamiiMapperUtils.mapObject(getRequest(), GetFriendListServicesREQ.class);
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
        this.userRelationship.dataList.addAll(userRelationship.fetch(this.userLogin.data, UserRelationship.TYPE_FRIEND, UserRelationship.STATUS_ACTIVE));

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
            SearchResultsHelper.RelationShipResults obj = new SearchResultsHelper.RelationShipResults();
            UserLoginTBL user;
            
            if (Objects.equals(relationship.getSenderid().getId(), this.userLogin.data.getId())) {
                user = relationship.getReceiverid();
            } else {
                user = relationship.getSenderid();
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
            
            this.relationshipResults.put(user.getUserKey(), obj);
        }

        if (this.relationshipResults.isEmpty()) {
            this.jamiiErrorsMessagesRESP.setGetFriendList_NoFriends();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {

            GetFriendListRESP response = new GetFriendListRESP();
            for (Map.Entry<String, SearchResultsHelper.RelationShipResults> entry : this.relationshipResults.entrySet()) {
                GetFriendListRESP.Results resp = new GetFriendListRESP.Results();
                resp.setUsername(entry.getValue().getUSERNAME());
                resp.setUserKey(entry.getValue().getUSER_KEY());
                resp.setFirstname(entry.getValue().getFIRSTNAME());
                resp.setLastname(entry.getValue().getLASTNAME());
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
        this.relationshipResults = new HashMap<>();
    }
}
