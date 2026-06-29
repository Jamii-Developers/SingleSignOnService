package com.jamii.jSocial.services.relationshipManagement;

import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.controllers.UserRequest;
import com.jamii.jSocial.model.UserRequestsTBL;
import com.jamii.jSocial.requests.GetFollowerRequestListServicesREQ;
import com.jamii.jSocial.responses.GetFollowRequestListRESP;
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
import java.util.Optional;

/**
 * Service for retrieving the list of pending follow requests for an authenticated user.
 * 
 * <p>This service allows authenticated users to retrieve all pending follow requests
 * they have received, showing the users who want to follow them along with their
 * profile information. The operation requires valid session authentication.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Verify user exists and is active</li>
 *   <li>Fetch pending follow requests from database</li>
 *   <li>Retrieve profile information for each requester</li>
 *   <li>Compile and return results</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found or inactive</li>
 *   <li>No pending follow requests found</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see GetFollowerRequestListServicesREQ
 */
@Service
public class GetFollowRequestListOPS
        extends AbstractUserServicesOPS
{

    private HashMap<String, SearchResultsHelper.RelationShipResults> relationshipResults = new HashMap<>();

    @Autowired private UserRequest userRequest;
    @Autowired private UserLogin userLogin;
    @Autowired private UserData userData;
    
    /** Request object containing follow request list data */
    protected GetFollowerRequestListServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link GetFollowerRequestListServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new GetFollowerRequestListServicesREQ();
        req = (GetFollowerRequestListServicesREQ) JamiiMapperUtils.mapObject(getRequest(), GetFollowerRequestListServicesREQ.class);
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

        // Get followers from relationship table
        this.userRequest.dataList = new ArrayList<>();
        this.userRequest.dataList.addAll(userRequest.fetchRequests(this.userLogin.data, UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE));

        //Get the necessary relationships and fetch the user information
        // Optimize by collecting all users first, then batch fetch user data
        List<UserLoginTBL> requestUsers = this.userRequest.dataList.stream()
                .map(UserRequestsTBL::getSenderid)
                .toList();
        
        // Create a map for quick lookup of user data
        Map<UserLoginTBL, Optional<UserDataTBL>> userDataMap = new HashMap<>();
        for (UserLoginTBL user : requestUsers) {
            userDataMap.put(user, this.userData.fetch(user, UserData.CURRENT_STATUS_ON));
        }
        
        // Process results with batch-fetched data
        for (UserRequestsTBL request : this.userRequest.dataList) {
            SearchResultsHelper.RelationShipResults obj = new SearchResultsHelper.RelationShipResults();
            UserLoginTBL user = request.getSenderid();
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
            this.jamiiErrorsMessagesRESP.setGetFollowRequestList_NoNewFollowRequests();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {

            GetFollowRequestListRESP response = new GetFollowRequestListRESP();
            for (Map.Entry<String, SearchResultsHelper.RelationShipResults> entry : this.relationshipResults.entrySet()) {
                GetFollowRequestListRESP.Results resp = new GetFollowRequestListRESP.Results();
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
