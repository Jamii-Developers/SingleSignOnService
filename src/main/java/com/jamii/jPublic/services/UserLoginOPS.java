package com.jamii.jPublic.services;

import com.jamii.abstractClasses.AbstractPublicServices;
import com.jamii.jPublic.requests.UserLoginREQ;
import com.jamii.jPublic.responses.UserLoginRESP;
import com.jamii.jUser.controller.DeviceInformation;
import com.jamii.jUser.controller.UserCookies;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.utils.JamiiDateUtils;
import com.jamii.utils.JamiiMapperUtils;
import com.jamii.utils.JamiiRandomKeyToolGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.function.BiFunction;

@Service
public class UserLoginOPS
        extends AbstractPublicServices
{

    private static final int DEVICE_KEY_LENGTH = 50;
    private static final int SESSION_KEY_LENGTH = 70;
    private static final String TIMEZONE_GMT = "GMT";
    private static final int MAX_KEY_GENERATION_RETRIES = 100;

    // Fetch all necessary connections
    @Autowired private UserLogin userLogin;
    @Autowired private DeviceInformation deviceInformation;
    @Autowired private UserCookies userCookies;

    @Override
    public void processRequest()
    {
        UserLoginREQ req = (UserLoginREQ) JamiiMapperUtils.mapObject(getRequest(), UserLoginREQ.class);

        // Validate input parameters
        if (req.getLoginCredential() == null || req.getLoginCredential().trim().isEmpty()) {
            jamiiDebug.warning("Login credential is empty");
            this.jamiiErrorsMessagesRESP.setLoginError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        if (req.getLoginPassword() == null || req.getLoginPassword().trim().isEmpty()) {
            jamiiDebug.warning("Login password is empty");
            this.jamiiErrorsMessagesRESP.setLoginError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // First check if the user information is in our system by validating the username or email address
        this.userLogin.data = this.userLogin.checkAndRetrieveValidLogin(req.getLoginCredential(), req.getLoginPassword());
        if (this.userLogin.data == null) {
            jamiiDebug.warning("Invalid login credentials for: " + req.getLoginCredential());
            this.jamiiErrorsMessagesRESP.setLoginError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        jamiiDebug.info("User login validated successfully: " + this.userLogin.data.getUsername());

        // Generate unique device key
        String deviceKey = generateUniqueKey(DEVICE_KEY_LENGTH, 
            (user, key) -> this.deviceInformation.checkIfKeyExisitsInTheDatabase(user, key));
        if (deviceKey == null) {
            jamiiDebug.error("Failed to generate unique device key after " + MAX_KEY_GENERATION_RETRIES + " attempts");
            this.jamiiErrorsMessagesRESP.setLoginError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // Then create a device key that matches this specific device and save it in the system
        this.deviceInformation.data = this.deviceInformation.add(this.userLogin.data, deviceKey, req.getLoginDeviceName(), req.getLocation());

        // Generate unique session key
        String sessionKey = generateUniqueKey(SESSION_KEY_LENGTH, 
            (user, key) -> this.userCookies.checkIfKeyExisitsInTheDatabase(user, this.deviceInformation.data, key));
        if (sessionKey == null) {
            jamiiDebug.error("Failed to generate unique session key after " + MAX_KEY_GENERATION_RETRIES + " attempts");
            this.jamiiErrorsMessagesRESP.setLoginError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // save both the key and the device key to create a cookie, that can be share back to the device.
        this.userCookies.data = this.userCookies.add(this.userLogin.data, this.deviceInformation.data, sessionKey, req.getRememberLogin());
        jamiiDebug.info("User login completed successfully: " + this.userLogin.data.getUsername());
    }

    /**
     * Generates a unique key by retrying until a unique key is found or max retries is reached.
     * 
     * @param keyLength the length of the key to generate
     * @param keyValidator a function that checks if a key is unique (returns true if key does NOT exist)
     * @return the unique key, or null if max retries exceeded
     */
    private String generateUniqueKey(int keyLength, BiFunction<UserLoginTBL, String, Boolean> keyValidator) {
        JamiiRandomKeyToolGen keyToolGen = new JamiiRandomKeyToolGen();
        keyToolGen.setLen(keyLength);
        keyToolGen.setInclude_letters(true);
        keyToolGen.setInclude_numbers(true);
        keyToolGen.setInclude_special_chars(true);

        for (int attempt = 0; attempt < MAX_KEY_GENERATION_RETRIES; attempt++) {
            String key = keyToolGen.generate();
            if (keyValidator.apply(this.userLogin.data, key)) {
                return key;
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<?> getResponse()
    {
        if (getIsSuccessful()) {
            UserLoginRESP userLoginRESP = new UserLoginRESP();
            userLoginRESP.setUserKey(this.userLogin.data.getUserKey());
            userLoginRESP.setUsername(this.userLogin.data.getUsername());
            userLoginRESP.setEmailAddress(this.userLogin.data.getEmailaddress());
            userLoginRESP.setDateCreated(JamiiDateUtils.COOKIE_DATE.format(this.userCookies.data.getDatecreated().atZone(ZoneId.of(TIMEZONE_GMT))));
            userLoginRESP.setDeviceKey(this.deviceInformation.data.getDevicekey());
            userLoginRESP.setSessionKey(this.userCookies.data.getSessionkey());
            userLoginRESP.setExpiryDate(JamiiDateUtils.COOKIE_DATE.format(this.userCookies.data.getExpiredate().atZone(ZoneId.of(TIMEZONE_GMT))));

            return new ResponseEntity<>(userLoginRESP.getJSONRESP(), HttpStatus.OK);
        }
        return super.getResponse();
    }
}
