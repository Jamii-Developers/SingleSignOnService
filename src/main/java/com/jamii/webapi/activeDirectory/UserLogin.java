package com.jamii.webapi.activeDirectory;

import com.jamii.webapi.activeDirectory.data.UserLoginStruct;

public class UserLogin {

    private UserLoginStruct userloginstruct = new UserLoginStruct( );

    public UserLogin( UserLoginStruct userloginstruct ){
        this.userloginstruct = userloginstruct;
    }

    public String getResponse( ) {
        return this.userloginstruct.getRESPONSE();
    }
}
