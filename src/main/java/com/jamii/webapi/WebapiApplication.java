package com.jamii.webapi;

import com.jamii.webapi.activeDirectory.UserLoginOPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@SpringBootApplication
@RestController
public class WebapiApplication {

	@Autowired
	private UserLoginOPS userLoginOPS;

	public static void main(String[] args) {
		SpringApplication.run(WebapiApplication.class, args);
	}

	@PostMapping( path = "userlogin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity< HashMap <String, String> > userlogin(@RequestBody UserLoginOPS userLoginOPS) {
		System.out.println("Pausing for a sec");
		this.userLoginOPS.setLoginCredential(userLoginOPS.getLoginCredential( ) );
		this.userLoginOPS.setLoginPassword(userLoginOPS.getLoginPassword( ) );
		this.userLoginOPS.processRequest( );
		return this.userLoginOPS.response( );
	}

}
