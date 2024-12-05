package com.jamii.applicationControllers.publicControllers;

import com.jamii.Utils.JamiiDebug;
import com.jamii.operations.activedirectory.FunctionOPS.CreateNewUserOPS;
import com.jamii.operations.activedirectory.FunctionOPS.ReactivateUserOPS;
import com.jamii.operations.activedirectory.FunctionOPS.UserLoginOPS;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PublicServices {

	/**
	 * Configured to run on port 8080
	 */
	private final JamiiDebug jamiiDebug = new JamiiDebug( );

	private Map<String, Object> pathing( ){

		Map<String, Object> publicDirectoryMap = new HashMap< >( );
		publicDirectoryMap.put( "createnewuser", new CreateNewUserOPS( ) );
		publicDirectoryMap.put( "userlogin", new UserLoginOPS( ) );
		publicDirectoryMap.put( "reactivateuser", new ReactivateUserOPS( ) );
		return publicDirectoryMap;

	}

	public ResponseEntity<?> processRequest( String operation, Object requestPayload) throws Exception {
		jamiiDebug.info("Received request for operation: " + operation);

		// Lookup the handler
		Object handler = pathing( ).get(operation);

		if ( handler instanceof CreateNewUserOPS ){
			((CreateNewUserOPS) handler).run( requestPayload );
		}

		if ( handler instanceof UserLoginOPS ){
			((UserLoginOPS) handler).run( requestPayload );
		}

		return new ResponseEntity<>("This is not a valid request", HttpStatus.BAD_REQUEST);
	}
}
