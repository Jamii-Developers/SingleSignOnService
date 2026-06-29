package com.jamii.jSocial.responses;

import com.jamii.abstractClasses.AbstractResponses;

/**
 * Response object for deleting a post.
 * 
 * <p>This class contains the response data for successful post deletion,
 * including the deleted post ID and deletion timestamp. Extends AbstractResponses
 * to inherit JSON response generation capabilities.</p>
 * 
 * <p>Response fields:</p>
 * <ul>
 *   <li>postId - Unique identifier of the deleted post</li>
 *   <li>deletionDate - Timestamp when the post was deleted</li>
 *   <li>message - Success message</li>
 * </ul>
 */
public class DeletePostRESP extends AbstractResponses
{
    private Integer postId;
    private String deletionDate;
    private String message;

    // Constructors
    public DeletePostRESP() {
        this.message = "Post deleted successfully";
    }

    public DeletePostRESP(Integer postId, String deletionDate) {
        this.postId = postId;
        this.deletionDate = deletionDate;
        this.message = "Post deleted successfully";
    }

    // Getters and Setters
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(String deletionDate) {
        this.deletionDate = deletionDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
