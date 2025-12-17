package com.jamii.operations.userServices.userProfile;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiStringUtils;
import com.jamii.databaseconfig.controllers.UserData;
import com.jamii.databaseconfig.controllers.UserLogin;
import com.jamii.databaseconfig.model.UserDataTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.FetchUserDataREQ;
import com.jamii.responses.userResponses.profileResponses.FetchUserProfileRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FetchUserDataOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserData userData;

    private FetchUserProfileRESP fetchUserProfileRESP;

    public FetchUserDataOPS() {}

    @Override
    public void validateCookie()
            throws Exception
    {
        FetchUserDataREQ req = (FetchUserDataREQ) JamiiMapperUtils.mapObject(getRequest(), FetchUserDataREQ.class);
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

        FetchUserDataREQ req = (FetchUserDataREQ) JamiiMapperUtils.mapObject(getRequest(), FetchUserDataREQ.class);

        Optional<UserLoginTBL> sender = this.userLogin.fetchByUserKey(req.getUserKey(), UserLogin.ACTIVE_ON);

        if (sender.isPresent()) {
            Optional<UserDataTBL> userdata = this.userData.fetch(sender.get(), UserData.CURRENT_STATUS_ON);
            fetchUserProfileRESP = new FetchUserProfileRESP();
            if (userdata.isPresent()) {
                fetchUserProfileRESP.setFirstname(JamiiStringUtils.getSafeString(userdata.get().getFirstname()));
                fetchUserProfileRESP.setLastname(JamiiStringUtils.getSafeString(userdata.get().getLastname()));
                fetchUserProfileRESP.setMiddlename(JamiiStringUtils.getSafeString(userdata.get().getMiddlename()));
                fetchUserProfileRESP.setAddress1(JamiiStringUtils.getSafeString(userdata.get().getAddress1()));
                fetchUserProfileRESP.setAddress2(JamiiStringUtils.getSafeString(userdata.get().getAddress2()));
                fetchUserProfileRESP.setCity(JamiiStringUtils.getSafeString(userdata.get().getCity()));
                fetchUserProfileRESP.setState(JamiiStringUtils.getSafeString(userdata.get().getState()));
                fetchUserProfileRESP.setProvince(JamiiStringUtils.getSafeString(userdata.get().getProvince()));
                fetchUserProfileRESP.setCountry(JamiiStringUtils.getSafeString(userdata.get().getCountry()));
                fetchUserProfileRESP.setZipcode(JamiiStringUtils.getSafeString(userdata.get().getZipcode()));
                fetchUserProfileRESP.setPrivacy(sender.get().getPrivacy());
            }
            else {
                this.jamiiErrorsMessagesRESP.setFetchUserData_NoData();
                this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
                setIsSuccessful(false);
            }
        }
        else {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (this.isSuccessful) {
            return new ResponseEntity<>(this.fetchUserProfileRESP.getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
