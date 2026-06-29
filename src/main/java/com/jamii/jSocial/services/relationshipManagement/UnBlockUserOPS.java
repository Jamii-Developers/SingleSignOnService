package com.jamii.jSocial.services.relationshipManagement;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jSocial.controllers.UserBlockList;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jSocial.model.UserBlockListTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.UnBlockUserServicesREQ;
import com.jamii.jSocial.responses.UnBlockUserRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Service for unblocking users from the authenticated user's block list.
 * 
 * <p>This service allows authenticated users to remove users from their block list,
 * deactivating the block relationship. The operation requires valid session
 * authentication and an existing active block relationship.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Verify both users exist and are active</li>
 *   <li>Fetch and validate the block relationship exists</li>
 *   <li>Deactivate the block relationship</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>Users not found or inactive</li>
 *   <li>No active block relationship found</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see UnBlockUserServicesREQ
 */
@Service
public class UnBlockUserOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserBlockList blockList;
    
    /** Request object containing user unblock data */
    protected UnBlockUserServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link UnBlockUserServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new UnBlockUserServicesREQ();
        req = (UnBlockUserServicesREQ) JamiiMapperUtils.mapObject(getRequest(), UnBlockUserServicesREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
    }

    @Override
    public void processRequest()
            throws Exception
    {

        if (!getIsSuccessful()) {
            return;
        }

        // Request parameters are already mapped in setUserRequestData()

        // Check if both jUser exist in the system
        this.userLogin.data = new UserLoginTBL();
        this.userLogin.otherUser = new UserLoginTBL();
        this.userLogin.data = this.cookie.getValidatedUser();
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
