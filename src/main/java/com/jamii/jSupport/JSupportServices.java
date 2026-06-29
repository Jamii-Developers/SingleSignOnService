package com.jamii.jSupport;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import com.jamii.jSupport.services.ContactSupportOPS;
import com.jamii.jSupport.services.ReviewUsOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jsupport/")
@CrossOrigin(origins = {"https://jamiix.netlify.app", "http://localhost:3000"})
public class JSupportServices
 extends AbstractApplicationControllers
{
    @Autowired ReviewUsOPS reviewUsOPS;
    @Autowired ContactSupportOPS contactSupportOPS;

    @Override
    protected void initPathing()
    {
        directoryMap.put("reviewus", reviewUsOPS);
        directoryMap.put("contactsupport", contactSupportOPS);
    }
}
