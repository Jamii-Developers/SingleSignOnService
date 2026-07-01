package com.jamii.jAdmin;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/jadmin/")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = {"https://jamiix.netlify.app", "http://localhost:3000"})
public class JAdminController
 extends AbstractApplicationControllers
{



    @Override
    protected void initPathing()
    {


    }

}
