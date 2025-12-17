package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.databaseconfig.controllers.UserData;
import com.jamii.databaseconfig.controllers.UserLogin;
import com.jamii.databaseconfig.controllers.UserRelationship;
import com.jamii.databaseconfig.model.UserDataTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.databaseconfig.model.UserRelationshipTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.operations.userServices.social.Utils.SearchResultsHelper;
import com.jamii.requests.userServices.socialREQ.GetFriendListServicesREQ;
import com.jamii.responses.userResponses.socialResponses.GetFriendListRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class GetFriendListOPS
        extends AbstractUserServicesOPS
{

    private HashMap<String, SearchResultsHelper.RelationShipResults> relationshipResults = new HashMap<>();

    @Autowired private UserRelationship userRelationship;
    @Autowired private UserLogin userLogin;
    @Autowired private UserData userData;

    @Override
    public void validateCookie()
            throws Exception
    {
        GetFriendListServicesREQ req = (GetFriendListServicesREQ) JamiiMapperUtils.mapObject(getRequest(), GetFriendListServicesREQ.class);
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

        GetFriendListServicesREQ req = (GetFriendListServicesREQ) JamiiMapperUtils.mapObject(getRequest(), GetFriendListServicesREQ.class);

        // Check if both users exist in the system
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
        for (UserRelationshipTBL relationship : this.userRelationship.dataList) {

            SearchResultsHelper.RelationShipResults obj = new SearchResultsHelper.RelationShipResults();
            UserLoginTBL user;

            if (Objects.equals(relationship.getSenderid().getId(), this.userLogin.data.getId())) {
                user = relationship.getReceiverid();
            }
            else {
                user = relationship.getSenderid();
            }

            Optional<UserDataTBL> userdata = this.userData.fetch(user, UserData.CURRENT_STATUS_ON);
            if (userdata.isPresent()) {

                obj.setUSERNAME(user.getUsername());
                obj.setUSER_KEY(user.getUserKey());
                obj.setFIRSTNAME(userdata.get().getFirstname());
                obj.setLASTNAME(userdata.get().getLastname());

                this.relationshipResults.put(user.getUserKey(), obj);
            }
            else {
                obj.setUSERNAME(user.getUsername());
                obj.setUSER_KEY(user.getUserKey());
                obj.setFIRSTNAME("N/A");
                obj.setLASTNAME("N/A");

                this.relationshipResults.put(user.getUserKey(), obj);
            }
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
