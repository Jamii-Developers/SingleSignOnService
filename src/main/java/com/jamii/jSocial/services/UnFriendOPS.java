package com.jamii.jSocial.services;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jSocial.controllers.UserRelationship;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jSocial.model.UserRelationshipTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.UnFriendServicesREQ;
import com.jamii.jSocial.responses.UnFriendRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class UnFriendOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserRelationship userRelationship;

    @Override
    public void validateCookie()
            throws Exception
    {
        UnFriendServicesREQ req = (UnFriendServicesREQ) JamiiMapperUtils.mapObject(getRequest(), UnFriendServicesREQ.class);
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

        UnFriendServicesREQ req = (UnFriendServicesREQ) JamiiMapperUtils.mapObject(getRequest(), UnFriendServicesREQ.class);

        // Check if both jUser exist in the system
        this.userLogin.data = new UserLoginTBL();
        this.userLogin.otherUser = new UserLoginTBL();
        this.userLogin.data = this.userLogin.fetchByUserKey(UserKey, UserLogin.ACTIVE_ON).orElse(null);
        this.userLogin.otherUser = this.userLogin.fetchByUserKey(req.getTargetUserKey(), UserLogin.ACTIVE_ON).orElse(null);
        if (this.userLogin.data == null || this.userLogin.otherUser == null) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        //Find the Friend Relationship in the system
        this.userRelationship.dataList = new ArrayList<>();
        this.userRelationship.dataList.addAll(this.userRelationship.fetch(this.userLogin.otherUser, this.userLogin.data, UserRelationship.TYPE_FRIEND, UserRelationship.STATUS_ACTIVE));
        this.userRelationship.dataList.addAll(this.userRelationship.fetch(this.userLogin.data, this.userLogin.otherUser, UserRelationship.TYPE_FRIEND, UserRelationship.STATUS_ACTIVE));

        //Deactivate all possible relationships
        if (this.userRelationship.data != null) {
            for (UserRelationshipTBL relationship : this.userRelationship.dataList) {
                relationship.setStatus(UserRelationship.STATUS_INACTIVE);
                relationship.setDateupdated(LocalDateTime.now());
            }
            this.userRelationship.saveAll();
        }
        else {
            this.jamiiErrorsMessagesRESP.set_UnFriend_NotFriends();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {
            return new ResponseEntity<>(new UnFriendRESP(this.userLogin.otherUser).getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
