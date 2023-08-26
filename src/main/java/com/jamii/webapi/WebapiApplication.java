package com.jamii.webapi;

import com.jamii.Utils.JamiiDebug;
import com.jamii.webapi.activeDirectory.CreateNewUserOPS;
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
	@Autowired
	private CreateNewUserOPS createNewUserOPS;



	private final JamiiDebug jamiiDebug = new JamiiDebug( );

	public static void main(String[] args) {
		SpringApplication.run( WebapiApplication.class, args);
	}

	@PostMapping( path = "userlogin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity< HashMap <String, String> > userLogin(@RequestBody UserLoginOPS userLoginOPS) {

		jamiiDebug.info("Received request");

		this.userLoginOPS.setLoginCredential( userLoginOPS.getLoginCredential( ) );
		this.userLoginOPS.setLoginPassword( userLoginOPS.getLoginPassword( ) );
		this.userLoginOPS.reset( );

		this.userLoginOPS.processRequest( );

		jamiiDebug.info("Request completed");
		return this.userLoginOPS.response( );
	}

	@PostMapping( path = "createnewuser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity< HashMap <String, String> > createnewuser(@RequestBody CreateNewUserOPS createNewUserOPS ) throws Exception {
		jamiiDebug.info("Received request" );

		this.createNewUserOPS.setEmailaddress( createNewUserOPS.getEmailaddress( ) );
		this.createNewUserOPS.setUsername( createNewUserOPS.getUsername( ) );
		this.createNewUserOPS.setPassword( createNewUserOPS.getPassword( ) );

		this.createNewUserOPS.processRequest( );

		jamiiDebug.info("Request completed");
		return this.createNewUserOPS.response( );
	}

}
