package com.jamii.jUser.services;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jUser.requests.SessionValidatorREQ;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SessionValidator
        extends AbstractUserServicesOPS
{

    @Override
    public void processRequest()
            throws Exception
    {
    }

    @Override
    public void validateCookie()
            throws Exception
    {
        SessionValidatorREQ req = (SessionValidatorREQ) JamiiMapperUtils.mapObject(getRequest(), SessionValidatorREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
        super.validateCookie();
    }

    @Override
    public ResponseEntity<?> getResponse()
    {

        if (getIsSuccessful()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}
