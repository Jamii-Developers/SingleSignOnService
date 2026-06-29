package com.jamii.jUser.services;

import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jUser.requests.SessionValidatorREQ;
import com.jamii.jUser.responses.SessionValidatorRESP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SessionValidator service.
 * 
 * <p>These tests verify the functionality of session validation including
 * request mapping, authentication key extraction, and response generation.</p>
 */
@ExtendWith(MockitoExtension.class)
class SessionValidatorTest {

    @InjectMocks
    private SessionValidator sessionValidator;

    private SessionValidatorREQ validRequest;
    private SessionValidatorREQ invalidRequest;

    @BeforeEach
    void setUp() {
        validRequest = new SessionValidatorREQ();
        validRequest.setDeviceKey("test-device-key");
        validRequest.setUserKey("test-user-key");
        validRequest.setSessionKey("test-session-key");

        invalidRequest = new SessionValidatorREQ();
        invalidRequest.setDeviceKey("");
        invalidRequest.setUserKey("");
        invalidRequest.setSessionKey("");
    }

    @Test
    void testSetUserRequestData_WithValidRequest() {
        // Given
        sessionValidator.setRequest(validRequest);

        // When
        sessionValidator.setUserRequestData();

        // Then
        assertEquals("test-device-key", sessionValidator.getDeviceKey());
        assertEquals("test-user-key", sessionValidator.getUserKey());
        assertEquals("test-session-key", sessionValidator.getSessionKey());
    }

    @Test
    void testSetUserRequestData_WithInvalidRequest() {
        // Given
        sessionValidator.setRequest(invalidRequest);

        // When
        sessionValidator.setUserRequestData();

        // Then
        assertEquals("", sessionValidator.getDeviceKey());
        assertEquals("", sessionValidator.getUserKey());
        assertEquals("", sessionValidator.getSessionKey());
    }

    @Test
    void testProcessRequest_DoesNotThrowException() {
        // Given
        sessionValidator.setRequest(validRequest);
        sessionValidator.setUserRequestData();

        // When & Then
        assertDoesNotThrow(() -> {
            try {
                sessionValidator.processRequest();
            } catch (Exception e) {
                fail("ProcessRequest should not throw exception: " + e.getMessage());
            }
        });
    }

    @Test
    void testGetResponse_WhenSuccessful() {
        // Given
        sessionValidator.setIsSuccessful(true);

        // When
        ResponseEntity<?> response = sessionValidator.getResponse();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetResponse_WhenNotSuccessful() {
        // Given
        sessionValidator.setIsSuccessful(false);
        sessionValidator.setJamiiError("Test error message");

        // When
        ResponseEntity<?> response = sessionValidator.getResponse();

        // Then
        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
    }

    @Test
    void testReset_ResetsAllFields() {
        // Given
        sessionValidator.setRequest(validRequest);
        sessionValidator.setUserRequestData();
        sessionValidator.setIsSuccessful(true);

        // When
        sessionValidator.reset();

        // Then
        assertNull(sessionValidator.getDeviceKey());
        assertNull(sessionValidator.getUserKey());
        assertNull(sessionValidator.getSessionKey());
        assertFalse(sessionValidator.getIsSuccessful());
    }

    @Test
    void testRequestResponseFlow() {
        // Given
        SessionValidatorREQ request = new SessionValidatorREQ();
        request.setDeviceKey("device-123");
        request.setUserKey("user-123");
        request.setSessionKey("session-123");

        // When
        sessionValidator.setRequest(request);
        sessionValidator.setUserRequestData();
        sessionValidator.reset();
        sessionValidator.setRequest(request);
        sessionValidator.setUserRequestData();
        sessionValidator.setIsSuccessful(true);
        ResponseEntity<?> response = sessionValidator.getResponse();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testSessionValidatorRESP_Creation() {
        // Given
        SessionValidatorRESP response = new SessionValidatorRESP();

        // When
        String jsonResponse = response.getJSONRESP();

        // Then
        assertNotNull(jsonResponse);
        assertFalse(jsonResponse.isEmpty());
    }
}
