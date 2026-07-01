package com.jamii.jSocial.services.relationshipManagement;

import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.peer.UserBlockList;
import com.jamii.jSocial.model.UserBlockListTBL;
import com.jamii.jSocial.requests.GetBlockUserListServicesREQ;
import com.jamii.jSocial.responses.GetBlockUserListRESP;
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
import java.util.Optional;

/**
 * Service for retrieving the list of blocked users for an authenticated user.
 * 
 * <p>This service allows authenticated users to retrieve their block list,
 * showing all users they have blocked along with their profile information.
 * The operation requires valid session authentication.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Verify user exists and is active</li>
 *   <li>Fetch block list entries from database</li>
 *   <li>Retrieve profile information for each blocked user</li>
 *   <li>Compile and return results</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found or inactive</li>
 *   <li>No blocked users found</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see GetBlockUserListServicesREQ
 */
@Service
public class GetBlockUserListOPS
        extends AbstractUserServicesOPS
{

    private HashMap<String, SearchResultsHelper.RelationShipResults> relationshipResults = new HashMap<>();

    @Autowired private UserBlockList userBlockList;
    @Autowired private UserLogin userLogin;
    @Autowired private UserData userData;
    
    /** Request object containing block list request data */
    protected GetBlockUserListServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link GetBlockUserListServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new GetBlockUserListServicesREQ();
        req = (GetBlockUserListServicesREQ) JamiiMapperUtils.mapObject(getRequest(), GetBlockUserListServicesREQ.class);
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

        // Get friends from userblock list table

        this.userBlockList.dataList = new ArrayList<>();
        this.userBlockList.dataList.addAll(userBlockList.fetch(this.userLogin.data, this.userLogin.otherUser, UserBlockList.STATUS_ACTIVE));

        //Get the necessary relationships and fetch the user information
        // Optimize by collecting all users first, then batch fetch user data
        List<UserLoginTBL> blockedUsers = this.userBlockList.dataList.stream()
                .map(UserBlockListTBL::getBlockedid)
                .toList();
        
        // Create a map for quick lookup of user data
        Map<UserLoginTBL, Optional<UserDataTBL>> userDataMap = new HashMap<>();
        for (UserLoginTBL user : blockedUsers) {
            userDataMap.put(user, this.userData.fetch(user, UserData.CURRENT_STATUS_ON));
        }
        
        // Process results with batch-fetched data
        for (UserBlockListTBL blockeduser : this.userBlockList.dataList) {
            SearchResultsHelper.RelationShipResults obj = new SearchResultsHelper.RelationShipResults();
            UserLoginTBL user = blockeduser.getBlockedid();
            Optional<UserDataTBL> userDataOpt = userDataMap.get(user);
            
            if (userDataOpt != null && userDataOpt.isPresent()) {
                UserDataTBL userData = userDataOpt.get();
                obj.setUSERNAME(user.getUsername());
                obj.setUSER_KEY(user.getUserKey());
                obj.setFIRSTNAME(userData.getFirstname());
                obj.setLASTNAME(userData.getLastname());
            } else {
                obj.setUSERNAME(user.getUsername());
                obj.setUSER_KEY(user.getUserKey());
                obj.setFIRSTNAME("N/A");
                obj.setLASTNAME("N/A");
            }
            
            this.relationshipResults.put(user.getUserKey(), obj);
        }

        if (this.relationshipResults.isEmpty()) {
            this.jamiiErrorsMessagesRESP.setGetBlockUserList_NoBlockedUsers();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {

            GetBlockUserListRESP response = new GetBlockUserListRESP();
            for (Map.Entry<String, SearchResultsHelper.RelationShipResults> entry : this.relationshipResults.entrySet()) {
                GetBlockUserListRESP.Results resp = new GetBlockUserListRESP.Results();
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
