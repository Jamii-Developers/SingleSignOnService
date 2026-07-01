package com.jamii.jPublic.services;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.utils.JamiiUserPasswordEncryptTool;
import com.jamii.abstractClasses.AbstractPublicServices;
import com.jamii.sysconfigs.FileServerConfigs;
import com.jamii.jUser.peer.PasswordHashRecords;
import com.jamii.jUser.peer.UserLogin;
import com.jamii.jUser.model.PasswordHashRecordsTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jPublic.requests.CreateNewUserREQ;
import com.jamii.jPublic.responses.CreateNewUserRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;

@Service
public class CreateNewUserOPS
        extends AbstractPublicServices
{

    private static final int DEFAULT_PRIVACY_LEVEL = 0;

    @Autowired private UserLogin userLogin;
    @Autowired private PasswordHashRecords passwordHashRecords;

    @Override
    public void processRequest()
            throws Exception
    {

        CreateNewUserREQ req = (CreateNewUserREQ) JamiiMapperUtils.mapObject(getRequest(), CreateNewUserREQ.class);

        // Validate input parameters
        if (req.getEmailaddress() == null || req.getEmailaddress().trim().isEmpty()) {
            jamiiDebug.warning("Email address is empty");
            this.jamiiErrorsMessagesRESP.createNewUserError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        if (req.getUsername() == null || req.getUsername().trim().isEmpty()) {
            jamiiDebug.warning("Username is empty");
            this.jamiiErrorsMessagesRESP.createNewUserError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
            jamiiDebug.warning("Password is empty");
            this.jamiiErrorsMessagesRESP.createNewUserError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        // First check if user information exists in the system
        if (userLogin.checkifUserExists(req.getEmailaddress(), req.getUsername())) {
            jamiiDebug.warning("User already exists with email: " + req.getEmailaddress() + " or username: " + req.getUsername());
            this.jamiiErrorsMessagesRESP.createNewUserError();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        this.userLogin.data = new UserLoginTBL();
        this.userLogin.data.setEmailAddress(req.getEmailaddress());
        this.userLogin.data.setUsername(req.getUsername());
        this.userLogin.data.setPasswordsalt(JamiiUserPasswordEncryptTool.encryptPassword(req.getPassword()));
        this.userLogin.data.setActive(UserLogin.ACTIVE_ON);
        this.userLogin.data.setPrivacy(DEFAULT_PRIVACY_LEVEL);

        LocalDateTime dateCreated = LocalDateTime.now();
        this.userLogin.data.setDatecreated(dateCreated);

        String userKey = JamiiUserPasswordEncryptTool.generateUserKey(req.getUsername(), req.getEmailaddress(), this.userLogin.data.getDatecreated().toString());
        this.userLogin.data.setUserKey(userKey);

        this.userLogin.save();
        jamiiDebug.info("User created successfully: " + req.getUsername());

        //Add new password records
        this.passwordHashRecords.data = new PasswordHashRecordsTBL();
        this.passwordHashRecords.addUserNewPasswordRecord(this.userLogin.data);

        // Create user image directory
        try {
            File file = new File(FileServerConfigs.USER_IMAGE_STORE + File.separator + this.userLogin.data.getIdAsString());
            if (!file.exists()) {
                boolean created = file.mkdir();
                if (created) {
                    jamiiDebug.info("User image directory created: " + file.getAbsolutePath());
                } else {
                    jamiiDebug.warning("Failed to create user image directory: " + file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            jamiiDebug.error("Error creating user image directory: " + e.getMessage());
            // Continue without failing the entire operation
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {
        if (getIsSuccessful()) {
            CreateNewUserRESP createNewUserRESP = new CreateNewUserRESP();

            createNewUserRESP.setUserKey(this.userLogin.data.getUserKey());
            createNewUserRESP.setUsername(this.userLogin.data.getUsername());
            createNewUserRESP.setEmailAddress(this.userLogin.data.getEmailaddress());
            createNewUserRESP.setDateCreated(this.userLogin.data.getDatecreated().toString());

            return new ResponseEntity<>(createNewUserRESP.getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
