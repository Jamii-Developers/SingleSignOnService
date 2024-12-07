package com.jamii.applicationControllers.publicControllers;

import com.jamii.Utils.JamiiDebug;
import com.jamii.operations.activedirectory.functionOPS.CreateNewUserOPS;
import com.jamii.operations.activedirectory.functionOPS.ReactivateUserOPS;
import com.jamii.operations.activedirectory.functionOPS.UserLoginOPS;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PublicServices {

	@Autowired
	CreateNewUserOPS createNewUserOPS;
	@Autowired
	UserLoginOPS userLoginOPS;
	@Autowired
	ReactivateUserOPS reactivateUserOPS;

	private final JamiiDebug jamiiDebug = new JamiiDebug( this.getClass() );

	private final Map<String, Object> publicDirectoryMap = new HashMap<>();

    @PostConstruct
	private void initPathing() {
		publicDirectoryMap.put("createnewuser", createNewUserOPS );
		publicDirectoryMap.put("userlogin", userLoginOPS );
		publicDirectoryMap.put("reactivateuser", reactivateUserOPS );
	}

	public ResponseEntity<?> processRequest( String operation, Object requestPayload) throws Exception {
		jamiiDebug.info("Received request for operation: " + operation);

		// Lookup the handler
		Object handler = publicDirectoryMap.get(operation);

		if ( handler instanceof UserLoginOPS ){
			( (UserLoginOPS) handler).reset( );
			return ( (UserLoginOPS) handler).run( requestPayload );
		}

		if( handler instanceof CreateNewUserOPS ){
			( ( CreateNewUserOPS) handler ).reset( );
			return ( ( CreateNewUserOPS) handler ).run( requestPayload );
		}

		if( handler instanceof ReactivateUserOPS ){
			( (ReactivateUserOPS) handler).reset( );
			return ( (ReactivateUserOPS) handler).run( requestPayload );
		}

		throw new Exception( operation );
	}
}
