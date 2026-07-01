package com.jamii.jPublic.services;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.abstractClasses.AbstractPublicServices;
import com.jamii.jUser.peer.UserLogin;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jPublic.requests.ReactivateUserREQ;
import com.jamii.jPublic.responses.ReactivateUserRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReactivateUserOPS
        extends AbstractPublicServices
{

    @Autowired private UserLogin userLogin;

    @Override
    public void processRequest()
            throws Exception
    {

        ReactivateUserREQ req = (ReactivateUserREQ) JamiiMapperUtils.mapObject(getRequest(), ReactivateUserREQ.class);

        // Validate input parameters
        if (req.getEmailaddress() == null || req.getEmailaddress().trim().isEmpty()) {
            jamiiDebug.warning("Email address is empty");
            this.jamiiErrorsMessagesRESP.setReactivateUser_UsernameOrEmailAddressDoesNotExist();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        if (req.getUsername() == null || req.getUsername().trim().isEmpty()) {
            jamiiDebug.warning("Username is empty");
            this.jamiiErrorsMessagesRESP.setReactivateUser_UsernameOrEmailAddressDoesNotExist();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
            jamiiDebug.warning("Password is empty");
            this.jamiiErrorsMessagesRESP.setReactivateUser_PasswordsNotMatching();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        //Check if the user exists as active
        Optional<UserLoginTBL> user = this.userLogin.fetch(req.getEmailaddress(), req.getUsername(), UserLogin.ACTIVE_ON);
        if (user.isEmpty()) {
            jamiiDebug.warning("No deactivated user matches the information shared " + req.getUsername());
            this.jamiiErrorsMessagesRESP.setReactivateUser_UsernameOrEmailAddressDoesNotExist();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        UserLoginTBL userRecord = user.get();

        //Check if the password is valid
        if (!this.userLogin.isPasswordValid(req.getPassword(), userRecord)) {
            jamiiDebug.warning("Password is incorrect " + req.getUsername());
            this.jamiiErrorsMessagesRESP.setReactivateUser_PasswordsNotMatching();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        //Reactivate user
        try {
            userLogin.reactivateUser(userRecord);
            jamiiDebug.info("User reactivated successfully: " + req.getUsername());
        } catch (Exception e) {
            jamiiDebug.error("Error reactivating user: " + req.getUsername() + " - " + e.getMessage());
            this.jamiiErrorsMessagesRESP.setReactivateUser_UsernameOrEmailAddressDoesNotExist();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {
        if (getIsSuccessful()) {
            ReactivateUserRESP reactivateUserRESP = new ReactivateUserRESP();
            return new ResponseEntity<>(reactivateUserRESP.getJSONRESP(), HttpStatus.OK);
        }

        return super.getResponse();
    }
}
