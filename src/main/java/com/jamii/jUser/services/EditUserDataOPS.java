package com.jamii.jUser.services;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jUser.controller.UserData;
import com.jamii.jUser.controller.UserDataHistory;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jUser.requests.EditUserDataServicesREQ;
import com.jamii.jUser.responses.EditUserDataRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditUserDataOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserData userData;
    @Autowired private UserDataHistory userDataHistory;

    public EditUserDataOPS() {}

    @Override
    public void validateCookie()
            throws Exception
    {
        EditUserDataServicesREQ req = (EditUserDataServicesREQ) JamiiMapperUtils.mapObject(getRequest(), EditUserDataServicesREQ.class);
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

        EditUserDataServicesREQ req = (EditUserDataServicesREQ) JamiiMapperUtils.mapObject(getRequest(), EditUserDataServicesREQ.class);
        Optional<UserLoginTBL> user = this.userLogin.fetchByUserKey(req.getUserKey(), UserLogin.ACTIVE_ON);

        //Adds the latest userData to the database
        this.userData.add(user.get( ), req);

        //Create a record of the new update
        this.userDataHistory.copyUserData( this.userData.data );

        //Updates Privacy Settings
        user.get().setPrivacy(req.getPrivacy());
        this.userLogin.add(user.get());

        setIsSuccessful(true);
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {
            EditUserDataRESP editUserDataRESP = new EditUserDataRESP();
            return new ResponseEntity<>(String.valueOf(editUserDataRESP.getJSONRESP()), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
