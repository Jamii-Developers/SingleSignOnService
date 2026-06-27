package com.jamii.jUser;

import com.jamii.abstractClasses.AbstractApplicationControllers;
import com.jamii.jUser.services.SearchUsersOPS;
import com.jamii.jUser.services.ViewUserProfileOPS;
import com.jamii.jUser.services.ChangePasswordOPS;
import com.jamii.jUser.services.DeactivateUserOPS;
import com.jamii.jUser.services.EditUserDataOPS;
import com.jamii.jUser.services.FetchUserDataOPS;
import com.jamii.jUser.services.SessionValidator;
import com.jamii.jUser.services.UserLogoffOPS;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/juser/")
@CrossOrigin(origins = "*")
public class JUserServices
        extends AbstractApplicationControllers
{

    @Autowired EditUserDataOPS editUserDataOPS;
    @Autowired FetchUserDataOPS fetchUserDataOPS;
    @Autowired DeactivateUserOPS deactivateUserOPS;
    @Autowired ChangePasswordOPS changePasswordOPS;
    @Autowired UserLogoffOPS userLogoffOPS;
    @Autowired SessionValidator sessionValidator;
    @Autowired SearchUsersOPS searchUsersOPS;
    @Autowired ViewUserProfileOPS viewUserProfileOPS;

    @PostConstruct
    protected void initPathing()
    {

        directoryMap.put("chngpassword", changePasswordOPS);
        directoryMap.put("editprofile", editUserDataOPS);
        directoryMap.put("fetchprofile", fetchUserDataOPS);
        directoryMap.put("deactivateuser", deactivateUserOPS);
        directoryMap.put("userlogoff", userLogoffOPS);
        directoryMap.put("validate-session", sessionValidator);
        directoryMap.put("searchuser", searchUsersOPS);
        directoryMap.put("viewuserprofile", viewUserProfileOPS);


    }

}
