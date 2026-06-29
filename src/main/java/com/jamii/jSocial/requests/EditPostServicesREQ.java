package com.jamii.jSocial.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;

/**
 * Request object for editing an existing post.
 * 
 * <p>This class contains the necessary parameters for editing an existing user post,
 * including authentication credentials, post identifier, and updated content. 
 * Extends the base AbstractUserServicesREQ to inherit common authentication fields.</p>
 * 
 * <p>Required fields:</p>
 * <ul>
 *   <li>deviceKey - Device identifier for session validation (inherited)</li>
 *   <li>userKey - User identifier for authentication (inherited)</li>
 *   <li>sessionKey - Session key for validation (inherited)</li>
 *   <li>postId - Unique identifier of the post to edit</li>
 *   <li>content - Updated post content text (max 2000 characters)</li>
 * </ul>
 */
public class EditPostServicesREQ extends AbstractUserServicesREQ
{
    private Integer postId;
    private String content;

    // Constructors
    public EditPostServicesREQ() {
        super();
    }

    public EditPostServicesREQ(String deviceKey, String userKey, String sessionKey, Integer postId, String content) {
        super();
        this.setDeviceKey(deviceKey);
        this.setUserKey(userKey);
        this.setSessionKey(sessionKey);
        this.postId = postId;
        this.content = content;
    }

    // Getters and Setters
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
