package com.jamii.jSocial.responses;

import com.jamii.abstractClasses.AbstractResponses;

/**
 * Response object for editing an existing post.
 * 
 * <p>This class contains the response data for successful post editing,
 * including the post ID, updated content, and modification timestamp. 
 * Extends AbstractResponses to inherit JSON response generation capabilities.</p>
 * 
 * <p>Response fields:</p>
 * <ul>
 *   <li>postId - Unique identifier of the edited post</li>
 *   <li>lastUpdated - Timestamp when the post was last updated</li>
 *   <li>content - Updated post content</li>
 *   <li>message - Success message</li>
 * </ul>
 */
public class EditPostRESP extends AbstractResponses
{
    private Integer postId;
    private String lastUpdated;
    private String content;
    private String message;

    // Constructors
    public EditPostRESP() {
        this.message = "Post updated successfully";
    }

    public EditPostRESP(Integer postId, String lastUpdated, String content) {
        this.postId = postId;
        this.lastUpdated = lastUpdated;
        this.content = content;
        this.message = "Post updated successfully";
    }

    // Getters and Setters
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
