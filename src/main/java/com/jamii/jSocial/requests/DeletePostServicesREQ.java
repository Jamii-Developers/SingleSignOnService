package com.jamii.jSocial.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;

/**
 * Request object for deleting a post.
 * 
 * <p>This class contains the necessary parameters for deleting an existing user post,
 * including authentication credentials and post identifier. Extends the base
 * AbstractUserServicesREQ to inherit common authentication fields.</p>
 * 
 * <p>Required fields:</p>
 * <ul>
 *   <li>deviceKey - Device identifier for session validation (inherited)</li>
 *   <li>userKey - User identifier for authentication (inherited)</li>
 *   <li>sessionKey - Session key for validation (inherited)</li>
 *   <li>postId - Unique identifier of the post to delete</li>
 * </ul>
 */
public class DeletePostServicesREQ extends AbstractUserServicesREQ
{
    private Integer postId;

    // Constructors
    public DeletePostServicesREQ() {
        super();
    }

    public DeletePostServicesREQ(String deviceKey, String userKey, String sessionKey, Integer postId) {
        super();
        this.setDeviceKey(deviceKey);
        this.setUserKey(userKey);
        this.setSessionKey(sessionKey);
        this.postId = postId;
    }

    // Getters and Setters
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }
}
