package com.jamii.jSocial.responses;

import com.jamii.abstractClasses.AbstractResponses;

/**
 * Response object for creating a new post.
 * 
 * <p>This class contains the response data for successful post creation,
 * including the post ID and creation timestamp. Extends AbstractResponses
 * to inherit JSON response generation capabilities.</p>
 * 
 * <p>Response fields:</p>
 * <ul>
 *   <li>postId - Unique identifier of the created post</li>
 *   <li>creationDate - Timestamp when the post was created</li>
 *   <li>message - Success message</li>
 * </ul>
 */
public class CreateNewPostRESP extends AbstractResponses
{
    private Integer postId;
    private String creationDate;
    private String message;

    // Constructors
    public CreateNewPostRESP() {
        this.message = "Post created successfully";
    }

    public CreateNewPostRESP(Integer postId, String creationDate) {
        this.postId = postId;
        this.creationDate = creationDate;
        this.message = "Post created successfully";
    }

    // Getters and Setters
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
