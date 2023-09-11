package com.jamii.webapi;

import com.jamii.Utils.JamiiDebug;
import com.jamii.requests.activeDirectory.*;
import com.jamii.webapi.activeDirectory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*") // Apply to the entire controller
@SpringBootApplication
@RestController
public class WebapiApplication {

	@Autowired
	private UserLoginOPS userLoginOPS;
	@Autowired
	private CreateNewUserOPS createNewUserOPS;
	@Autowired
	private ChangePasswordOPS changePasswordOPS;
	@Autowired
	private EditUserDataOPS editUserDataOPS;
	@Autowired
	private ReactivateUserOPS reactivateUserOPS;
	@Autowired
	private DeactivateUserOPS deactivateUserOPS;



	private final JamiiDebug jamiiDebug = new JamiiDebug( );

	public static void main( String[ ] args ) {
		SpringApplication.run( WebapiApplication.class, args);
	}

	@PostMapping( path = "createnewuser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity< String > createnewuser( @RequestBody CreateNewUserREQ createNewUserREQ ) throws Exception {
		jamiiDebug.info("Received request" );

		this.createNewUserOPS.reset( );
		this.createNewUserOPS.setCreateNewUserREQ( createNewUserREQ );
		this.createNewUserOPS.processRequest( );

		jamiiDebug.info("Request completed");
		return this.createNewUserOPS.getResponse( );
	}

	@PostMapping( path = "userlogin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity< String > userLogin(@RequestBody UserLoginREQ userLoginREQ ) {

		jamiiDebug.info("Received request");

		this.userLoginOPS.reset( );
		this.userLoginOPS.setUserLoginREQ( userLoginREQ );
		this.userLoginOPS.processRequest( );

		jamiiDebug.info("Request completed");
		return this.userLoginOPS.getResponse( );
	}

	@PostMapping( path = "changepassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity< String > changepassword( @RequestBody ChangePasswordREQ changePasswordREQ ) throws Exception {

		jamiiDebug.info("Received request" );

		this.changePasswordOPS.reset( );
		this.changePasswordOPS.setChangePasswordREQ( changePasswordREQ );
		this.changePasswordOPS.processRequest( );
		jamiiDebug.info("Request completed");

		return this.changePasswordOPS.getResponse( );
	}

	@PostMapping( path = "edituserdata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity< String > edituserdata( @RequestBody EditUserDataREQ editUserDataREQ ) throws Exception {
		jamiiDebug.info("Received request" );

		this.editUserDataOPS.reset( );
		this.editUserDataOPS.setEditUserDataREQ( editUserDataREQ );
		this.editUserDataOPS.processRequest( ) ;

		jamiiDebug.info("Request completed");
		return this.editUserDataOPS.getResponse( );
	}

	@PostMapping( path = "reactivateuser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity< String > reactivateuser( @RequestBody ReactivateUserREQ reactivateUserREQ ) throws Exception {
		jamiiDebug.info("Received request" );
		this.reactivateUserOPS.reset( );
		this.reactivateUserOPS.setReactivateUserREQ( reactivateUserREQ );
		this.reactivateUserOPS.processRequest( );
		jamiiDebug.info("Request completed");
		return this.reactivateUserOPS.getResponse( );
	}

	@PostMapping( path = "deactivateuser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity< String > deactivateuser( @RequestBody DeactivateUserREQ deactivateUserREQ ) throws Exception {
		jamiiDebug.info("Received request" );
		this.deactivateUserOPS.reset( );
		this.deactivateUserOPS.setDeactivateUserREQ( deactivateUserREQ );
		this.deactivateUserOPS.processRequest( );
		jamiiDebug.info("Request completed");
		return this.deactivateUserOPS.getResponse( );
	}


}
