package com.jamii.jSocial.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;
import com.jamii.utils.ValidationUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request object for creating a new post.
 * 
 * <p>This class contains the necessary parameters for creating a new user post,
 * including authentication credentials and post content. Extends the base
 * AbstractUserServicesREQ to inherit common authentication fields.</p>
 * 
 * <p>Required fields:</p>
 * <ul>
 *   <li>deviceKey - Device identifier for session validation (inherited)</li>
 *   <li>userKey - User identifier for authentication (inherited)</li>
 *   <li>sessionKey - Session key for validation (inherited)</li>
 *   <li>content - Post content text (max 2000 characters)</li>
 * </ul>
 */
public class CreateNewPostServicesREQ extends AbstractUserServicesREQ
{
    @NotBlank(message = "Post content cannot be empty")
    @Size(min = 1, max = 2000, message = "Post content must be between 1 and 2000 characters")
    private String content;

    // Constructors
    public CreateNewPostServicesREQ() {
        super();
    }

    public CreateNewPostServicesREQ(String deviceKey, String userKey, String sessionKey, String content) {
        super();
        this.setDeviceKey(deviceKey);
        this.setUserKey(userKey);
        this.setSessionKey(sessionKey);
        this.content = content;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = ValidationUtils.sanitize(content);
    }

    /**
     * Validates the request data using ValidationUtils.
     * 
     * @return true if all required fields are valid, false otherwise
     */
    public boolean isValid() {
        return ValidationUtils.isValidPostContent(content) &&
               ValidationUtils.areAuthenticationKeysValid(
                   getDeviceKey(), 
                   getUserKey(), 
                   getSessionKey()
               );
    }

    /**
     * Gets validation error messages.
     * 
     * @return error message if validation fails, null if valid
     */
    public String getValidationErrorMessage() {
        if (!ValidationUtils.areAuthenticationKeysValid(getDeviceKey(), getUserKey(), getSessionKey())) {
            return "Invalid authentication credentials";
        }
        if (!ValidationUtils.isValidPostContent(content)) {
            return "Post content must be between 1 and 2000 characters";
        }
        return null;
    }
}
