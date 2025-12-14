package com.jamii.operations.publicServices;

import com.jamii.Utils.JamiiDateUtils;
import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiRandomKeyToolGen;
import com.jamii.jamiidb.controllers.DeviceInformation;
import com.jamii.jamiidb.controllers.UserCookies;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.UserCookiesTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.publicServices.UserLoginREQ;
import com.jamii.responses.publicResponses.UserLoginRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class UserLoginOPS
        extends AbstractPublicServices
{

    // Fetch all necessary connections
    @Autowired private UserLogin userLogin;
    @Autowired private DeviceInformation deviceInformation;
    @Autowired private UserCookies userCookies;

    @Override
    public void processRequest()
    {
        UserLoginREQ req = (UserLoginREQ) JamiiMapperUtils.mapObject(getRequest(), UserLoginREQ.class);

        // First check if the user information is in our system by validating the username or email address
        this.userLogin.data = new UserLoginTBL();
        this.userLogin.data = this.userLogin.checkAndRetrieveValidLogin(req.getLoginCredential(), req.getLoginPassword());
        if (this.userLogin.data == null) {
            this.jamiiErrorsMessagesRESP.setLoginError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Once found confirm if the submitted password i.e. Key is saved and valid for this user
        boolean checkIfKeyExists = false;
        JamiiRandomKeyToolGen keyToolGen = new JamiiRandomKeyToolGen();
        keyToolGen.setLen(50);
        keyToolGen.setInclude_letters(true);
        keyToolGen.setInclude_numbers(true);
        keyToolGen.setInclude_special_chars(true);
        String key = "";
        while (!checkIfKeyExists) {
            key = keyToolGen.generate();
            checkIfKeyExists = this.deviceInformation.checkIfKeyExisitsInTheDatabase(this.userLogin.data, key);
        }

        // Then create a device key that matches this specific device and save it in the system
        this.deviceInformation.data = new DeviceInformationTBL();
        this.deviceInformation.data = this.deviceInformation.add(this.userLogin.data, key, req.getLoginDeviceName(), req.getLocation());
        boolean checkIfSessionKeyExists = false;
        JamiiRandomKeyToolGen sessionkeyToolGen = new JamiiRandomKeyToolGen();
        sessionkeyToolGen.setLen(70);
        sessionkeyToolGen.setInclude_letters(true);
        sessionkeyToolGen.setInclude_numbers(true);
        sessionkeyToolGen.setInclude_special_chars(true);
        String sessionkey = "";
        while (!checkIfSessionKeyExists) {
            sessionkey = sessionkeyToolGen.generate();
            checkIfSessionKeyExists = this.userCookies.checkIfKeyExisitsInTheDatabase(this.userLogin.data, this.deviceInformation.data, sessionkey);
        }

        // save both the key and the device key to create a cookie, that can be share back to the device.
        this.userCookies.data = new UserCookiesTBL();
        this.userCookies.data = this.userCookies.add(this.userLogin.data, this.deviceInformation.data, sessionkey, req.getRememberLogin());
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {

            StringBuilder response = new StringBuilder();

            UserLoginRESP userLoginRESP = new UserLoginRESP();
            userLoginRESP.setUSER_KEY(this.userLogin.data.getUserKey());
            userLoginRESP.setUSERNAME(this.userLogin.data.getUsername());
            userLoginRESP.setEMAIL_ADDRESS(this.userLogin.data.getEmailaddress());
            userLoginRESP.setDATE_CREATED(JamiiDateUtils.COOKIE_DATE.format(this.userCookies.data.getDatecreated().atZone(ZoneId.of("GMT"))));
            userLoginRESP.setDEVICE_KEY(this.deviceInformation.data.getDevicekey());
            userLoginRESP.setSESSION_KEY(this.userCookies.data.getSessionkey());
            userLoginRESP.setEXPIRY_DATE(JamiiDateUtils.COOKIE_DATE.format(this.userCookies.data.getExpiredate().atZone(ZoneId.of("GMT"))));

            response.append(userLoginRESP.getJSONRESP());

            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        }
        return super.getResponse();
    }
}
