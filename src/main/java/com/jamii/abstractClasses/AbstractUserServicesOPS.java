package com.jamii.abstractClasses;

import com.jamii.utils.JamiiCookieProcessor;
import com.jamii.utils.JamiiDebug;
import com.jamii.utils.JamiiErrorsMessagesRESP;
import com.jamii.utils.JamiiMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Abstract base class for user service operations requiring authentication.
 * 
 * <p>This class provides common functionality for user-related operations including:
 * <ul>
 *   <li>Session validation through cookie-based authentication</li>
 *   <li>Request processing workflow management</li>
 *   <li>Error handling and response generation</li>
 *   <li>Authentication key management (device, user, session)</li>
 * </ul>
 * 
 * <p>Typical usage pattern:
 * <pre>
 * public class ConcreteService extends AbstractUserServicesOPS {
 *     public void validateCookie() throws Exception {
 *         // Extract keys from request and call super.validateCookie()
 *     }
 *     
 *     public void processRequest() throws Exception {
 *         // Implement business logic
 *     }
 * }
 * </pre>
 * 
 * <p>Thread Safety: This class maintains instance-level authentication state,
 * making it safe for concurrent use in web applications.
 * 
 * @see JamiiCookieProcessor
 * @see JamiiErrorsMessagesRESP
 */
public abstract class AbstractUserServicesOPS
{

    /** Device authentication key */
    protected String deviceKey;
    
    /** User authentication key */
    protected String userKey;
    
    /** Session authentication key */
    protected String sessionKey;
    
    /** Debug logger for this service instance */
    protected final JamiiDebug jamiiDebug = new JamiiDebug(this.getClass());
    
    /** Error message string populated when validation fails */
    protected String JamiiError;
    
    /** Response object containing error messages and status */
    protected JamiiErrorsMessagesRESP jamiiErrorsMessagesRESP = null;
    
    /** Flag indicating whether the operation completed successfully */
    protected Boolean isSuccessful = true;
    
    /** The incoming request payload */
    protected Object request;

    /** The response object for successful operations */
    protected Object Response;

    /** Cookie processor for validating session authentication */
    @Autowired protected JamiiCookieProcessor cookie;

    /**
     * Gets the device authentication key.
     * 
     * @return the device key, or null if not set
     */
    protected String getDeviceKey() {
        return deviceKey;
    }

    /**
     * Sets the device authentication key.
     * 
     * @param deviceKey the device key to set
     */
    protected void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    /**
     * Gets the user authentication key.
     * 
     * @return the user key, or null if not set
     */
    protected String getUserKey() {
        return userKey;
    }

    /**
     * Sets the user authentication key.
     * 
     * @param userKey the user key to set
     */
    protected void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    /**
     * Gets the session authentication key.
     * 
     * @return the session key, or null if not set
     */
    protected String getSessionKey() {
        return sessionKey;
    }

    /**
     * Sets the session authentication key.
     * 
     * @param sessionKey the session key to set
     */
    protected void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    /**
     * Gets the success status of the operation.
     * 
     * @return true if the operation succeeded, false otherwise
     */
    protected Boolean getIsSuccessful() {
        return isSuccessful;
    }

    /**
     * Sets the success status of the operation.
     * 
     * @param isSuccessful the success status to set
     */
    protected void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    /**
     * Gets the request payload.
     * 
     * @return the request object
     */
    public Object getRequest() {
        return request;
    }

    /**
     * Sets the request payload.
     * 
     * @param request the request object to set
     */
    public void setRequest(Object request) {
        this.request = request;
    }

    /**
     * Resets the service state to initial values.
     * 
     * <p>This method clears all authentication keys, error messages,
     * and resets the success flag. Should be called before processing
     * a new request to ensure clean state.
     */
    public void reset() {
        this.JamiiError = null;
        this.jamiiErrorsMessagesRESP = new JamiiErrorsMessagesRESP();
        setDeviceKey(null);
        setUserKey(null);
        setSessionKey(null);
        setIsSuccessful(false);
    }

    /**
     * Maps the incoming request to the appropriate request object and extracts authentication credentials.
     * 
     * <p>This abstract method must be implemented by all concrete service classes to handle
     * request mapping and authentication key extraction. The method is called during the
     * service execution workflow before session validation occurs.</p>
     * 
     * <p>Standard implementation pattern:</p>
     * <ol>
     *   <li>Create a new instance of the specific request class</li>
     *   <li>Map the incoming request using {@code JamiiMapperUtils.mapObject()}</li>
     *   <li>Extract and set authentication keys (deviceKey, userKey, sessionKey)</li>
     *   <li>Store the mapped request object in a protected field for later use</li>
     * </ol>
     * 
     * <p>Example implementation:</p>
     * <pre>{@code
     * protected void setUserRequestData() {
     *     req = new MyServiceRequest();
     *     req = (MyServiceRequest) JamiiMapperUtils.mapObject(getRequest(), MyServiceRequest.class);
     *     setDeviceKey(req.getDeviceKey());
     *     setUserKey(req.getUserKey());
     *     setSessionKey(req.getSessionKey());
     * }
     * }</pre>
     * 
     * <p>Important considerations:</p>
     * <ul>
     *   <li>Must be implemented by all concrete service classes</li>
     *   <li>Should follow the established pattern for consistency</li>
     *   <li>Authentication keys are required for session validation</li>
     *   <li>Request object should be stored in a protected field for {@code processRequest()}</li>
     * </ul>
     * 
     * <p>This method is called automatically by the {@code run()} method in the service
     * execution pipeline and should not be called directly by other methods.</p>
     * 
     * @see JamiiMapperUtils#mapObject(Object, Class)
     * @see #setDeviceKey(String)
     * @see #setUserKey(String) 
     * @see #setSessionKey(String)
     * @see #processRequest()
     */
    protected abstract void setUserRequestData();

    /**
     * Processes the main business logic for the service operation.
     * 
     * <p>Subclasses must implement this method to define the specific
     * business logic for their operation. The method is called after
     * successful cookie validation.
     * 
     * @throws Exception if an error occurs during processing
     */
    public abstract void processRequest() throws Exception;

    /**
     * Executes the complete service operation workflow.
     * 
     * <p>This method orchestrates the full request processing pipeline:
     * <ol>
     *   <li>Resets service state</li>
     *   <li>Sets the request payload</li>
     *   <li>Validates the session cookie</li>
     *   <li>Processes the business logic</li>
     *   <li>Returns the response</li>
     * </ol>
     * 
     * @param requestPayload the incoming request object
     * @return ResponseEntity containing the operation result
     * @throws Exception if an error occurs during execution
     */
    public ResponseEntity<?> run(Object requestPayload) throws Exception {
        reset();
        jamiiDebug.info("Received request");
        setRequest(requestPayload);
        setUserRequestData();
        validateCookie();
        if (getIsSuccessful()) {
            processRequest();
        }
        jamiiDebug.info("Request completed");
        return this.getResponse();
    }

    /**
     * Validates the session cookie and authentication keys.
     * 
     * <p>This method performs three levels of validation:
     * <ol>
     *   <li>Checks that device, user, and session keys are present (not null)</li>
     *   <li>Checks that all keys are non-empty strings</li>
     *   <li>Validates the session cookie using the cookie processor</li>
     * </ol>
     * 
     * <p>If any validation fails, the error state is set and the operation
     * is marked as unsuccessful.
     * 
     * @throws Exception if an error occurs during validation
     */
    public void validateCookie() throws Exception {
        // Check if cookie information is available
        if (deviceKey == null || userKey == null || sessionKey == null) {
            setValidationError();
            return;
        }

        if (deviceKey.isEmpty() || userKey.isEmpty() || sessionKey.isEmpty()) {
            setValidationError();
            return;
        }

        // Check if user cookie is valid
        cookie.setUSER_KEY(getUserKey());
        cookie.setDEVICE_KEY(getDeviceKey());
        cookie.setUSER_COOKIE(getSessionKey());

        if (!cookie.checkCookieIsValid()) {
            setValidationError();
        }
    }

    /**
     * Sets the validation error state when cookie validation fails.
     * 
     * <p>This is a helper method to reduce code duplication in validateCookie().
     */
    private void setValidationError() {
        this.jamiiErrorsMessagesRESP.setSearchUserOPS_DeviceNotFound();
        this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
        setIsSuccessful(false);
    }

    /**
     * Generates the HTTP response for the service operation.
     * 
     * <p>If an error occurred during processing, returns the error message.
     * Otherwise, returns an empty response. Subclasses should override
     * this method to provide custom response handling.
     * 
     * @return ResponseEntity containing the response data with HTTP status
     */
    public ResponseEntity<?> getResponse() {
        StringBuilder response = new StringBuilder();
        if (JamiiError != null && !JamiiError.isEmpty()) {
            response.append(this.JamiiError);
        }

        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}
