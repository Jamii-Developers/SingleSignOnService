package com.jamii.operations.publicServices;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiUserPasswordEncryptTool;
import com.jamii.configs.FileServerConfigs;
import com.jamii.jamiidb.controllers.PasswordHashRecords;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.model.PasswordHashRecordsTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.requests.publicServices.CreateNewUserREQ;
import com.jamii.responses.publicResponses.CreateNewUserRESP;
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

    @Autowired private UserLogin userLogin;
    @Autowired private PasswordHashRecords passwordHashRecords;

    @Override
    public void processRequest()
            throws Exception
    {

        CreateNewUserREQ req = (CreateNewUserREQ) JamiiMapperUtils.mapObject(getRequest(), CreateNewUserREQ.class);

        // First check if user information exists in the system
        if (userLogin.checkifUserExists(req.getEmailaddress(), req.getUsername())) {
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
        this.userLogin.data.setPrivacy(0);

        LocalDateTime dateCreated = LocalDateTime.now();
        this.userLogin.data.setDatecreated(dateCreated);

        String userKey = JamiiUserPasswordEncryptTool.generateUserKey(req.getUsername(), req.getEmailaddress(), this.userLogin.data.getDatecreated().toString());
        this.userLogin.data.setUserKey(userKey);

        this.userLogin.save();

        //Add new password records
        this.passwordHashRecords.data = new PasswordHashRecordsTBL();
        if (this.userLogin.data != null) {
            this.passwordHashRecords.addUserNewPasswordRecord(this.userLogin.data);
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {

            StringBuilder response = new StringBuilder();

            File file = new File(FileServerConfigs.USER_IMAGE_STORE + File.separator + this.userLogin.data.getIdAsString());
            file.mkdir();

            CreateNewUserRESP createNewUserRESP = new CreateNewUserRESP();

            createNewUserRESP.setUSER_KEY(this.userLogin.data.getUserKey());
            createNewUserRESP.setUSERNAME(this.userLogin.data.getUsername());
            createNewUserRESP.setEMAIL_ADDRESS(this.userLogin.data.getEmailaddress());
            createNewUserRESP.setDATE_CREATED(this.userLogin.data.getDatecreated().toString());

            response.append(createNewUserRESP.getJSONRESP());

            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
