package com.jamii.operations.userServices.userProfile;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.databaseconfig.controllers.DeviceInformation;
import com.jamii.databaseconfig.controllers.UserCookies;
import com.jamii.databaseconfig.controllers.UserLogin;
import com.jamii.databaseconfig.model.DeviceInformationTBL;
import com.jamii.databaseconfig.model.UserCookiesTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.profileREQ.UserLogoffREQ;
import com.jamii.responses.userResponses.profileResponses.UserLogoffRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserLogoffOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private DeviceInformation deviceInformation;
    @Autowired private UserCookies userCookies;

    @Override
    public void validateCookie()
            throws Exception
    {
        UserLogoffREQ req = (UserLogoffREQ) JamiiMapperUtils.mapObject(getRequest(), UserLogoffREQ.class);
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

        UserLogoffREQ req = (UserLogoffREQ) JamiiMapperUtils.mapObject(getRequest(), UserLogoffREQ.class);

        Optional<UserLoginTBL> user = this.userLogin.fetchByUserKey(req.getUserKey(), UserLogin.ACTIVE_ON);

        if (user.isEmpty()) {
            this.jamiiErrorsMessagesRESP.setLoginError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        Optional<DeviceInformationTBL> device = this.deviceInformation.fetch(user.get(), req.getDeviceKey());

        if (device.isEmpty()) {
            this.jamiiErrorsMessagesRESP.setLoginError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }
        else {
            device.get().setActive(DeviceInformation.ACTIVE_STATUS_DISABLED);
            this.deviceInformation.update(device.get());
        }

        Optional<UserCookiesTBL> cookie = this.userCookies.fetch(user.get(), device.get(), req.getSessionKey(), UserCookies.ACTIVE_STATUS_ENABLED);

        if (cookie.isEmpty()) {
            this.jamiiErrorsMessagesRESP.setLoginError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
        }
        else {
            cookie.get().setActive(UserCookies.ACTIVE_STATUS_DISABLED);
            this.userCookies.update(cookie.get());
        }

        setIsSuccessful(true);
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {

            UserLogoffRESP userLogoffRESP = new UserLogoffRESP();
            return new ResponseEntity<>(String.valueOf(userLogoffRESP.getJSONRESP()), HttpStatus.OK);
        }
        return super.getResponse();
    }
}
