package com.jamii.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for centralized error handling across the application.
 * 
 * <p>This class provides consistent error responses for various exceptions that may occur
 * during API request processing. It ensures that all error responses follow a standard format
 * and include appropriate logging for debugging purposes.</p>
 * 
 * <p>Error response format:</p>
 * <pre>
 * {
 *   "timestamp": "2024-01-01T12:00:00",
 *   "status": 500,
 *   "error": "INTERNAL_SERVER_ERROR",
 *   "message": "An unexpected error occurred",
 *   "path": "/api/v1/juser/operation"
 * }
 * </pre>
 */
@ControllerAdvice
public class JamiiGlobalExceptionHandler
{

    private static final Logger logger = LoggerFactory.getLogger(JamiiGlobalExceptionHandler.class);

    /**
     * Handles generic exceptions that are not caught by more specific handlers.
     * 
     * @param ex the exception that occurred
     * @param request the current web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred. Please try again later.",
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles IllegalArgumentException with bad request status.
     * 
     * @param ex the exception that occurred
     * @param request the current web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Invalid argument: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = createErrorResponse(
            HttpStatus.BAD_REQUEST,
            "BAD_REQUEST",
            ex.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles security-related exceptions.
     * 
     * @param ex the exception that occurred
     * @param request the current web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleSecurityException(SecurityException ex, WebRequest request) {
        logger.warn("Security violation: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = createErrorResponse(
            HttpStatus.FORBIDDEN,
            "FORBIDDEN",
            "Access denied: " + ex.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles resource not found exceptions.
     * 
     * @param ex the exception that occurred
     * @param request the current web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex, WebRequest request) {
        logger.warn("Runtime error: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = createErrorResponse(
            HttpStatus.NOT_FOUND,
            "NOT_FOUND",
            ex.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a standardized error response map.
     * 
     * @param status the HTTP status
     * @param error the error code
     * @param message the error message
     * @param path the request path
     * @return Map containing error response
     */
    private Map<String, Object> createErrorResponse(HttpStatus status, String error, String message, String path) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("path", path.replace("uri=", ""));
        
        return errorResponse;
    }
}
