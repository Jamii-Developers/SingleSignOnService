package com.jamii.jSocial.services.posts;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jSocial.controllers.UserPosts;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jSocial.model.UserPostsTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.EditPostServicesREQ;
import com.jamii.jSocial.responses.EditPostRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for editing existing user posts.
 * 
 * <p>This service allows authenticated users to edit their own posts.
 * The service validates the user session, verifies post ownership,
 * validates the new content, and updates the post with proper metadata.</p>
 * 
 * <p>Operation flow:</p>
 * <ol>
 *   <li>Extract authentication keys and post data in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Validate post ID and new content</li>
 *   <li>Fetch post from database</li>
 *   <li>Verify post ownership (user can only edit their own posts)</li>
 *   <li>Validate new content (non-empty, within length limits)</li>
 *   <li>Update post content and metadata</li>
 *   <li>Return edit response</li>
 * </ol>
 * 
 * <p>Error conditions:</p>
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found or inactive</li>
 *   <li>Invalid post ID</li>
 *   <li>Post not found</li>
 *   <li>User does not own the post</li>
 *   <li>Post is deleted</li>
 *   <li>Empty or invalid new content</li>
 *   <li>New content exceeds maximum length (2000 characters)</li>
 * </ul>
 * 
 * <p>Performance considerations:</p>
 * <ul>
 *   <li>Single database read operation for post retrieval</li>
 *   <li>Single database update operation for content modification</li>
 *   <li>Content validation performed before database access</li>
 *   <li>Automatic timestamp update for last modified</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see EditPostServicesREQ
 * @see UserPostsTBL
 */
@Service
public class EditPost
        extends AbstractUserServicesOPS
{

    @Autowired private UserPosts userPosts;
    @Autowired private UserLogin userLogin;
    
    /** Request object containing post editing data */
    protected EditPostServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link EditPostServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new EditPostServicesREQ();
        req = (EditPostServicesREQ) JamiiMapperUtils.mapObject(getRequest(), EditPostServicesREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
    }

    @Override
    public void processRequest()
            throws Exception
    {

        if (!getIsSuccessful()) {
            return;
        }

        // Request parameters are already mapped in setUserRequestData()

        // Validate post ID
        if (req.getPostId() == null || req.getPostId() <= 0) {
            this.jamiiErrorsMessagesRESP.setEditPost_InvalidPostId();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        // Validate new content
        if (req.getContent() == null || req.getContent().trim().isEmpty()) {
            this.jamiiErrorsMessagesRESP.setEditPost_InvalidContent();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        // Check content length (max 2000 characters as defined in UserPostsTBL)
        if (req.getContent().length() > 2000) {
            this.jamiiErrorsMessagesRESP.setEditPost_InvalidContent();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        // Get authenticated user from validated cookie
        UserLoginTBL user = this.cookie.getValidatedUser();
        if (user == null) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        // Fetch post from database
        Optional<UserPostsTBL> postOpt = userPosts.fetchById(req.getPostId());
        if (postOpt.isEmpty()) {
            this.jamiiErrorsMessagesRESP.setEditPost_PostNotFound();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        UserPostsTBL post = postOpt.get();

        // Verify post ownership
        if (!post.getUserid().getId().equals(user.getId())) {
            this.jamiiErrorsMessagesRESP.setEditPost_Unauthorized();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        // Check if post is deleted
        if (UserPosts.STATUS_DELETED.equals(post.getStatus())) {
            this.jamiiErrorsMessagesRESP.setEditPost_PostNotFound();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        try {
            // Update post content
            UserPostsTBL updatedPost = userPosts.updatePostContent(post, req.getContent().trim());
            
            // Set response data
            EditPostRESP response = new EditPostRESP();
            response.setPostId(updatedPost.getId());
            response.setLastUpdated(updatedPost.getLastupdated().toString());
            response.setContent(updatedPost.getContent());
            
            this.Response = response;
        } catch (Exception e) {
            this.jamiiErrorsMessagesRESP.setEditPost_UpdateFailed();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {
        if (getIsSuccessful()) {
            return new ResponseEntity<>(this.Response, HttpStatus.OK);
        }
        
        return super.getResponse();
    }
}
