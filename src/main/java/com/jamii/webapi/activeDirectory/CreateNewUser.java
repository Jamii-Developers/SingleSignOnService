package com.jamii.webapi.activeDirectory;

import com.jamii.webapi.activeDirectory.data.CreateNewUserDataStruct;

public class CreateNewUser {

    private CreateNewUserDataStruct createNewUserDataStruct;
    public CreateNewUser() {
    }

    public CreateNewUser(CreateNewUserDataStruct createNewUserDataStruct) {
        this.createNewUserDataStruct = createNewUserDataStruct;
    }
}
