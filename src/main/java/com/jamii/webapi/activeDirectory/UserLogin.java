package com.jamii.webapi.activeDirectory;

import com.jamii.webapi.activeDirectory.data.UserLoginStruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserLogin {

    private UserLoginStruct userloginstruct;

    public UserLogin( UserLoginStruct userloginstruct ){
        this.userloginstruct = userloginstruct;
    }

    public void processRequest( ){

        String response = "The request was processed successfully";
        System.out.println( this.userloginstruct.getUSERNAME( ) );
        System.out.println( this.userloginstruct.getPASSWORD( ) );
        System.out.println( this.userloginstruct.getUSERID( ) );
        System.out.println( this.userloginstruct.getDEVICEID( ) );
        System.out.println( this.userloginstruct.getAUTHENTICATIONTOKEN( ) );
        this.userloginstruct.setRESPONSE( new ResponseEntity<>( response, HttpStatus.OK) );
    }
}
