package com.jamii.jSocial.services.posts;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jSocial.controllers.UserPosts;
import com.jamii.jUser.controller.UserLogin;
import com.jamii.jSocial.model.UserPostsTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.DeletePostServicesREQ;
import com.jamii.jSocial.responses.DeletePostRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for deleting user posts.
 * 
 * <p>This service allows authenticated users to delete their own posts.
 * The service validates the user session, verifies post ownership,
 * and performs a soft delete by marking the post as deleted.</p>
 * 
 * <p>Operation flow:</p>
 * <ol>
 *   <li>Extract authentication keys and post ID in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Validate post ID parameter</li>
 *   <li>Fetch post from database</li>
 *   <li>Verify post ownership (user can only delete their own posts)</li>
 *   <li>Perform soft delete (mark as deleted)</li>
 *   <li>Return deletion response</li>
 * </ol>
 * 
 * <p>Error conditions:</p>
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found or inactive</li>
 *   <li>Invalid post ID</li>
 *   <li>Post not found</li>
 *   <li>User does not own the post</li>
 *   <li>Post already deleted</li>
 * </ul>
 * 
 * <p>Performance considerations:</p>
 * <ul>
 *   <li>Single database read operation for post retrieval</li>
 *   <li>Single database update operation for soft delete</li>
 *   <li>Ownership validation prevents unauthorized deletions</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see DeletePostServicesREQ
 * @see UserPostsTBL
 */
@Service
public class DeletePost
        extends AbstractUserServicesOPS
{

    @Autowired private UserPosts userPosts;
    @Autowired private UserLogin userLogin;
    
    /** Request object containing post deletion data */
    protected DeletePostServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link DeletePostServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new DeletePostServicesREQ();
        req = (DeletePostServicesREQ) JamiiMapperUtils.mapObject(getRequest(), DeletePostServicesREQ.class);
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
            this.jamiiErrorsMessagesRESP.setDeletePost_InvalidPostId();
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
            this.jamiiErrorsMessagesRESP.setDeletePost_PostNotFound();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        UserPostsTBL post = postOpt.get();

        // Verify post ownership
        if (!post.getUserid().getId().equals(user.getId())) {
            this.jamiiErrorsMessagesRESP.setDeletePost_Unauthorized();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        // Check if post is already deleted
        if (UserPosts.STATUS_DELETED.equals(post.getStatus())) {
            this.jamiiErrorsMessagesRESP.setDeletePost_PostNotFound();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        try {
            // Perform soft delete
            UserPostsTBL deletedPost = userPosts.deletePost(post);
            
            // Set response data
            DeletePostRESP response = new DeletePostRESP();
            response.setPostId(deletedPost.getId());
            response.setDeletionDate(deletedPost.getLastupdated().toString());
            
            this.Response = response;
        } catch (Exception e) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
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
