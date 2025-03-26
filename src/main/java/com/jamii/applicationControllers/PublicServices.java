package com.jamii.applicationControllers;

import com.jamii.Utils.JamiiDebug;
import com.jamii.operations.publicServices.CreateNewUserOPS;
import com.jamii.operations.publicServices.ReactivateUserOPS;
import com.jamii.operations.publicServices.UserLoginOPS;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	private final Map<String, Object> directoryMap = new HashMap<>();
    @PostConstruct
	private void initPathing() {
		directoryMap.put("createnewuser", createNewUserOPS );
		directoryMap.put("userlogin", userLoginOPS );
		directoryMap.put("reactivateuser", reactivateUserOPS );
	}

	public ResponseEntity<?> processRequest( String operation, Object requestPayload) throws Exception {

		try{
			jamiiDebug.info("Received request for operation: " + operation);

			// Lookup the handler
			Object handler = directoryMap.get(operation);

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

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> processMultipartRequest(String operation, String userKey, String deviceKey, String sessionKey, MultipartFile multipartFile) {
		return null;
	}
}
