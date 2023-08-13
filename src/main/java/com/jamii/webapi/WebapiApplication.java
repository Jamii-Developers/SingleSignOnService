package com.jamii.webapi;

import com.jamii.webapi.activeDirectory.UserLoginOPS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WebapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebapiApplication.class, args);
	}

	@PostMapping( path = "userlogin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity< String > userlogin( @RequestBody UserLoginOPS userLoginOPS) {
		userLoginOPS.processRequest( );
		return userLoginOPS.response( );
	}

}
