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
import com.jamii.requests.userServices.socialREQ.GetFollowerListServicesREQ;
import com.jamii.responses.userResponses.socialResponses.GetFollowListRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class GetFollowerListOPS
        extends AbstractUserServicesOPS
{

    private ArrayList<SearchResultsHelper.FollowRelationShipResults> relationshipResults = new ArrayList<>();

    @Autowired private UserRelationship userRelationship;
    @Autowired private UserLogin userLogin;
    @Autowired private UserData userData;

    @Override
    public void validateCookie()
            throws Exception
    {
        GetFollowerListServicesREQ req = (GetFollowerListServicesREQ) JamiiMapperUtils.mapObject(getRequest(), GetFollowerListServicesREQ.class);
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

        GetFollowerListServicesREQ req = (GetFollowerListServicesREQ) JamiiMapperUtils.mapObject(getRequest(), GetFollowerListServicesREQ.class);

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
        this.userRelationship.dataList.addAll(userRelationship.fetch(this.userLogin.data, UserRelationship.STATUS_ACTIVE, UserRelationship.TYPE_FOLLOW));

        //Get the necessary relationships and fetch the user information
        for (UserRelationshipTBL relationship : this.userRelationship.dataList) {

            SearchResultsHelper.FollowRelationShipResults obj = new SearchResultsHelper.FollowRelationShipResults();

            UserLoginTBL user;

            if (Objects.equals(relationship.getSenderid().getId(), this.userLogin.data.getId())) {
                user = relationship.getReceiverid();
                obj.setTypeOfFollower("following");
            }
            else {
                user = relationship.getSenderid();
                obj.setTypeOfFollower("follower");
            }

            Optional<UserDataTBL> userdata = this.userData.fetch(user, UserData.CURRENT_STATUS_ON);
            if (userdata.isPresent()) {

                obj.setUSERNAME(user.getUsername());
                obj.setUSER_KEY(user.getUserKey());
                obj.setFIRSTNAME(userdata.get().getFirstname());
                obj.setLASTNAME(userdata.get().getLastname());

                this.relationshipResults.add(obj);
            }
            else {
                obj.setUSERNAME(user.getUsername());
                obj.setUSER_KEY(user.getUserKey());
                obj.setFIRSTNAME("N/A");
                obj.setLASTNAME("N/A");

                this.relationshipResults.add(obj);
            }
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
