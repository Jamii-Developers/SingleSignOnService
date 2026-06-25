package com.jamii.users;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import com.jamii.users.services.ChangePasswordOPS;
import com.jamii.users.services.DeactivateUserOPS;
import com.jamii.users.services.EditUserDataOPS;
import com.jamii.users.services.FetchUserDataOPS;
import com.jamii.users.services.SessionValidator;
import com.jamii.users.services.UserLogoffOPS;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/")
@CrossOrigin(origins = "*")
public class UserServices
        extends AbstractApplicationControllers
{


    @Autowired EditUserDataOPS editUserDataOPS;
    @Autowired FetchUserDataOPS fetchUserDataOPS;
    @Autowired DeactivateUserOPS deactivateUserOPS;
    @Autowired ChangePasswordOPS changePasswordOPS;
    @Autowired UserLogoffOPS userLogoffOPS;
    @Autowired SessionValidator sessionValidator;

    @PostConstruct
    protected void initPathing()
    {

        directoryMap.put("chngpassword", changePasswordOPS);
        directoryMap.put("editprofile", editUserDataOPS);
        directoryMap.put("fetchprofile", fetchUserDataOPS);
        directoryMap.put("deactivateuser", deactivateUserOPS);
        directoryMap.put("userlogoff", userLogoffOPS);
        directoryMap.put("validate-session", sessionValidator);

    }

}
