package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.UserBlockList;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.UnBlockUserServicesREQ;
import com.jamii.responses.userResponses.socialResponses.UnBlockUserRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class UnBlockUserOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserBlockList blockList;

    @Override
    public void validateCookie()
            throws Exception
    {
        UnBlockUserServicesREQ req = (UnBlockUserServicesREQ) JamiiMapperUtils.mapObject(getRequest(), UnBlockUserServicesREQ.class);
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

        UnBlockUserServicesREQ req = (UnBlockUserServicesREQ) JamiiMapperUtils.mapObject(getRequest(), UnBlockUserServicesREQ.class);

        // Check if both users exist in the system
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

        this.blockList.dataList = new ArrayList<>();
        this.blockList.dataList.addAll(this.blockList.fetch(this.userLogin.data, this.userLogin.otherUser, UserBlockList.STATUS_ACTIVE));

        if (this.blockList.dataList != null) {
            for (UserBlockListTBL blocked : this.blockList.dataList) {
                blocked.setStatus(UserBlockList.STATUS_INACTIVE);
                blocked.setDateupdated(LocalDateTime.now());
            }
            this.blockList.saveAll();
        }
        else {
            this.jamiiErrorsMessagesRESP.setUnBlockUserOPS();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {

            return new ResponseEntity<>(new UnBlockUserRESP(this.userLogin.otherUser).getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
