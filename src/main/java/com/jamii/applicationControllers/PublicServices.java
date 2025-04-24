package com.jamii.applicationControllers;

import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiLoggingUtils;
import com.jamii.operations.publicServices.*;
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
	JamiiLoggingUtils jamiiLoggingUtils;
	@Autowired
	CreateNewUserOPS createNewUserOPS;
	@Autowired
	UserLoginOPS userLoginOPS;
	@Autowired
	ReactivateUserOPS reactivateUserOPS;

	private final JamiiDebug jamiiDebug = new JamiiDebug( this.getClass() );
	private final Map<String, AbstractPublicServices> directoryMap = new HashMap<>();

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
			AbstractPublicServices handler = directoryMap.get(operation);

			if (handler == null) {
				jamiiDebug.warn("Unknown operation: " + operation);
				throw new Exception( "Operation could not be found " + operation );
			}

			handler.reset();
			return handler.run(requestPayload);

		} catch (Exception e) {
			jamiiLoggingUtils.ExceptionLogger( this.getClass().getName() , e , null ) ;
		}
		return new ResponseEntity<>("Oops! something went wrong with your request", HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> processMultipartRequest(String operation, String userKey, String deviceKey, String sessionKey, MultipartFile multipartFile) {
		return null;
	}
}
