package com.jamii.jSocial.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;

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
        this.content = content;
    }
}
