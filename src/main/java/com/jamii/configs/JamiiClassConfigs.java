package com.jamii.configs;

import com.jamii.applicationControllers.publicControllers.PublicServices;
import com.jamii.operations.activedirectory.functionOPS.CreateNewUserOPS;
import com.jamii.operations.activedirectory.functionOPS.ReactivateUserOPS;
import com.jamii.operations.activedirectory.functionOPS.UserLoginOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JamiiClassConfigs {

    @Autowired
    private PublicServices publicServices = new PublicServices();
    @Autowired
    private UserLoginOPS userLoginOPS = new UserLoginOPS();
    @Autowired
    private CreateNewUserOPS createNewUserOPS = new CreateNewUserOPS();
    @Autowired
    private ReactivateUserOPS reactivateUserOPS = new ReactivateUserOPS();

    public PublicServices getPublicServices() {
        return publicServices;
    }

    public UserLoginOPS getUserLoginOPS() {
        return userLoginOPS;
    }

    public CreateNewUserOPS getCreateNewUserOPS() {
        return createNewUserOPS;
    }

    public ReactivateUserOPS getReactivateUserOPS() {
        return reactivateUserOPS;
    }
}
