package com.jamii.operations.publicServices;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.databaseconfig.controllers.UserLogin;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.requests.publicServices.ReactivateUserREQ;
import com.jamii.responses.publicResponses.ReactivateUserRESP;
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

        //Check if the user exists as active
        Optional<UserLoginTBL> user = this.userLogin.fetch(req.getEmailaddress(), req.getUsername(), UserLogin.ACTIVE_ON);
        if (user.isEmpty()) {
            jamiiDebug.warning("No deactivated user matches the information shared " + req.getUsername());
            this.jamiiErrorsMessagesRESP.setReactivateUser_UsernameOrEmailAddressDoesNotExist();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
            return;
        }

        //Check if the password is valid
        if (!this.userLogin.isPasswordValid(req.getPassword(), user.get())) {
            jamiiDebug.warning("Password is incorrect " + req.getUsername());
            this.jamiiErrorsMessagesRESP.setReactivateUser_PasswordsNotMatching();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            return;
        }

        //Reactivate user
        userLogin.reactivateUser(user.get());
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        ReactivateUserREQ req = (ReactivateUserREQ) JamiiMapperUtils.mapObject(getRequest(), ReactivateUserREQ.class);

        if (getIsSuccessful()) {
            StringBuilder response = new StringBuilder();
            jamiiDebug.info(String.format("This is account has been Reactivated : %s ", req.getUsername()));
            ReactivateUserRESP reactivateUserRESP = new ReactivateUserRESP();
            response.append(reactivateUserRESP.getJSONRESP());
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        }

        jamiiDebug.warning(String.format("Account Reactivation is unsuccessful : %s ", req.getUsername()));
        return super.getResponse();
    }
}
