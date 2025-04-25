package com.jamii.applicationControllers;

import com.jamii.Utils.JamiiLoggingUtils;
import com.jamii.operations.publicServices.AbstractPublicServices;
import com.jamii.operations.publicServices.CreateNewUserOPS;
import com.jamii.operations.publicServices.ReactivateUserOPS;
import com.jamii.operations.publicServices.UserLoginOPS;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PublicServices extends AbstractApplicationControllers {

	@Autowired
	JamiiLoggingUtils jamiiLoggingUtils;
	@Autowired
	CreateNewUserOPS createNewUserOPS;
	@Autowired
	UserLoginOPS userLoginOPS;
	@Autowired
	ReactivateUserOPS reactivateUserOPS;

	private final Map<String, AbstractPublicServices> directoryMap = new HashMap<>();

    @PostConstruct
	protected void initPathing() {
		directoryMap.put("createnewuser", createNewUserOPS );
		directoryMap.put("userlogin", userLoginOPS );
		directoryMap.put("reactivateuser", reactivateUserOPS );
	}

	public ResponseEntity<?> processJSONRequest(String operation, Object requestPayload) {

		try{
			jamiiDebug.info("Received request for operation: " + operation);

			// Lookup the handler
			AbstractPublicServices handler = directoryMap.get(operation);

			if (handler == null) {
				jamiiDebug.warn("Unknown Service-Header: " + operation);
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
