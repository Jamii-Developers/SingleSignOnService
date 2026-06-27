package com.jamii.jUser.services;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jUser.controller.UserData;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jUser.model.UserDataTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.ViewUserProfileServicesREQ;
import com.jamii.jSocial.responses.ViewUserProfileRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ViewUserProfileOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserData userData;

    @Override
    public void validateCookie()
            throws Exception
    {
        ViewUserProfileServicesREQ req = (ViewUserProfileServicesREQ) JamiiMapperUtils.mapObject(getRequest(), ViewUserProfileServicesREQ.class);
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

        ViewUserProfileServicesREQ req = (ViewUserProfileServicesREQ) JamiiMapperUtils.mapObject(getRequest(), ViewUserProfileServicesREQ.class);

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

        this.userData.data = new UserDataTBL();
        this.userData.data = this.userData.fetch(this.userLogin.otherUser, UserData.CURRENT_STATUS_ON).orElse(null);
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {

            return new ResponseEntity<>(new ViewUserProfileRESP(this.userLogin.otherUser, this.userData.data).getJSONRESP(), HttpStatus.OK);
        }
        return super.getResponse();
    }
}
