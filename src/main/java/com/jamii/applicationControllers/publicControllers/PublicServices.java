package com.jamii.applicationControllers.publicControllers;

import com.jamii.configs.JamiiClassConfigs;
import com.jamii.Utils.JamiiDebug;
import com.jamii.operations.activedirectory.functionOPS.CreateNewUserOPS;
import com.jamii.operations.activedirectory.functionOPS.ReactivateUserOPS;
import com.jamii.operations.activedirectory.functionOPS.UserLoginOPS;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PublicServices {

	private final JamiiDebug jamiiDebug = new JamiiDebug( this.getClass() );
	@Autowired
	private JamiiClassConfigs jamiiClassConfigs;

	private final Map<String, Object> publicDirectoryMap = new HashMap<>();


	@PostConstruct
	private void initPathing() {
		publicDirectoryMap.put("createnewuser", jamiiClassConfigs.getCreateNewUserOPS( ) );
		publicDirectoryMap.put("userlogin", jamiiClassConfigs.getUserLoginOPS( ) );
		publicDirectoryMap.put("reactivateuser", jamiiClassConfigs.getReactivateUserOPS( ));
	}

	public ResponseEntity<?> processRequest( String operation, Object requestPayload) throws Exception {
		jamiiDebug.info("Received request for operation: " + operation);

		// Lookup the handler
		Object handler = publicDirectoryMap.get(operation);

		if ( handler instanceof UserLoginOPS ){
			return ( (UserLoginOPS) handler).run( requestPayload );
		}

		if( handler instanceof CreateNewUserOPS ){
			return ( ( CreateNewUserOPS) handler ).run( requestPayload );
		}

		if( handler instanceof ReactivateUserOPS ){
			return ( (ReactivateUserOPS) handler).run( requestPayload );
		}

		return new ResponseEntity<>("This is not a valid request", HttpStatus.BAD_REQUEST);
	}
}
