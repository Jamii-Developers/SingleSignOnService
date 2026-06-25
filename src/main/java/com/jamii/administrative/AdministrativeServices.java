package com.jamii.administrative;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import com.jamii.administrative.services.clientCommunication.ContactSupportOPS;
import com.jamii.administrative.services.clientCommunication.ReviewUsOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/admin/")
@CrossOrigin(origins = "*")
public class AdministrativeServices
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
