package com.jamii.abstractClasses;

import com.jamii.utils.JamiiDebug;
import com.jamii.utils.JamiiErrorsMessagesRESP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Abstract base class for public-facing service operations in the Jamii application.
 * 
 * <p>This class provides a template for processing public API requests, including:
 * <ul>
 *   <li>Request lifecycle management (reset, process, respond)</li>
 *   <li>Error handling and response generation</li>
 *   <li>Debug logging capabilities</li>
 *   <li>Success/failure state tracking</li>
 * </ul>
 * 
 * <p>Subclasses must implement the {@link #processRequest()} method to define their specific
 * business logic. The {@link #getResponse()} method can be overridden to customize success responses,
 * while the base implementation handles error responses.
 * 
 * <p>Typical usage pattern:
 * <pre>
 * // Called by controller
 * service.reset();
 * service.run(requestPayload);
 * </pre>
 * 
 * @see AbstractApplicationControllers
 */
public abstract class AbstractPublicServices
{

    private static final String EMPTY_STRING = "";

    /** Error message string populated when request processing fails */
    protected String JamiiError;
    
    /** Response object containing standardized error messages */
    protected JamiiErrorsMessagesRESP jamiiErrorsMessagesRESP = null;
    
    /** Debug logging utility for this service instance */
    protected final JamiiDebug jamiiDebug = new JamiiDebug(this.getClass());

    /** Flag indicating whether the request was processed successfully */
    protected Boolean isSuccessful = true;
    
    /** The original request payload */
    protected Object request;

    /**
     * Returns the success status of the request processing.
     * 
     * @return true if the request was processed successfully, false otherwise
     */
    protected Boolean getIsSuccessful()
    {
        return isSuccessful;
    }

    /**
     * Sets the success status of the request processing.
     * 
     * @param isSuccessful true if the request was processed successfully, false otherwise
     */
    protected void setIsSuccessful(Boolean isSuccessful)
    {
        this.isSuccessful = isSuccessful;
    }

    /**
     * Returns the original request payload.
     * 
     * @return the request object
     */
    public Object getRequest()
    {
        return request;
    }

    /**
     * Sets the request payload for processing.
     * 
     * @param request the request object to process
     */
    public void setRequest(Object request) {this.request = request;}

    /**
     * Resets the service state to prepare for a new request.
     * 
     * <p>This method clears:
     * <ul>
     *   <li>Error message responses</li>
     *   <li>The request payload</li>
     *   <li>The success status flag (sets to true)</li>
 * </ul>
     * 
     * <p>Should be called before processing each new request to ensure
     * clean state and prevent data leakage between requests.
     */
    public void reset()
    {
        this.JamiiError = EMPTY_STRING;
        this.jamiiErrorsMessagesRESP = new JamiiErrorsMessagesRESP();
        setRequest(null);
        setIsSuccessful(true);
    }

    /**
     * Processes the request payload according to the specific service's business logic.
     * 
     * <p>This method must be implemented by all subclasses to define their specific
     * request handling logic. Implementations should:
     * <ul>
     *   <li>Validate the request parameters</li>
     *   <li>Perform the required business operations</li>
     *   <li>Set {@link #isSuccessful} to false if processing fails</li>
     *   <li>Populate {@link #JamiiError} with appropriate error messages on failure</li>
     * </ul>
     * 
     * @throws Exception if an unrecoverable error occurs during processing
     */
    public abstract void processRequest()
            throws Exception;

    /**
     * Executes the complete request processing lifecycle.
     * 
     * <p>This method orchestrates the request processing flow:
     * <ol>
     *   <li>Logs the incoming request</li>
     *   <li>Sets the request payload</li>
     *   <li>Calls {@link #processRequest()} to execute business logic</li>
     *   <li>Logs completion</li>
     *   <li>Returns the response via {@link #getResponse()}</li>
     * </ol>
     * 
     * @param requestPayload the request object to process
     * @return the ResponseEntity containing the response data
     * @throws Exception if an unrecoverable error occurs during processing
     */
    public ResponseEntity<?> run(Object requestPayload)
            throws Exception
    {
        jamiiDebug.info("Received request");
        setRequest(requestPayload);
        processRequest();
        jamiiDebug.info("Request completed");
        return this.getResponse();
    }

    /**
     * Generates the HTTP response for the request.
     * 
     * <p>This base implementation handles error responses by returning the
     * {@link #JamiiError} message when {@link #isSuccessful} is false.
     * 
     * <p>Subclasses should override this method to customize successful responses.
     * When overriding, call {@code super.getResponse()} to handle error cases:
     * <pre>
     * public ResponseEntity<?> getResponse() {
     *     if (getIsSuccessful()) {
     *         // Build and return success response
         *     }
     *     return super.getResponse();
     * }
     * </pre>
     * 
     * @return ResponseEntity containing the response data with appropriate status code
     */
    public ResponseEntity<?> getResponse()
    {
        if (JamiiError != null && !JamiiError.isEmpty()) {
            return new ResponseEntity<>(JamiiError, HttpStatus.OK);
        }
        return new ResponseEntity<>(EMPTY_STRING, HttpStatus.OK);
    }
}
