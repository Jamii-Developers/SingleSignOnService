package com.jamii.jAdmin;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/jadmin/")
@CrossOrigin(origins = {"https://jamiix.netlify.app", "http://localhost:3000"})
public class JAdminServices
 extends AbstractApplicationControllers
{



    @Override
    protected void initPathing()
    {


    }

}
