package com.jamii.jSocial.services.posts;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jSocial.peer.UserPosts;
import com.jamii.jUser.peer.UserLogin;
import com.jamii.jSocial.model.UserPostsTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.CreateNewPostServicesREQ;
import com.jamii.jSocial.responses.CreateNewPostRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service for creating new user posts.
 * 
 * <p>This service allows authenticated users to create new posts with text content.
 * The service validates the user session, creates the post with proper metadata,
 * and returns the created post information.</p>
 * 
 * <p>Operation flow:</p>
 * <ol>
 *   <li>Extract authentication keys and post content in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Validate post content (non-empty, within length limits)</li>
 *   <li>Create new post with user information and metadata</li>
 *   <li>Save post to database</li>
 *   <li>Return post creation response</li>
 * </ol>
 * 
 * <p>Error conditions:</p>
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found or inactive</li>
 *   <li>Empty or invalid post content</li>
 *   <li>Post content exceeds maximum length (2000 characters)</li>
 * </ul>
 * 
 * <p>Performance considerations:</p>
 * <ul>
 *   <li>Single database operation for post creation</li>
 *   <li>Content validation performed before database access</li>
 *   <li>Automatic timestamp generation for creation and last updated</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see CreateNewPostServicesREQ
 * @see UserPostsTBL
 */
@Service
public class CreateNewPost
        extends AbstractUserServicesOPS
{

    @Autowired private UserPosts userPosts;
    @Autowired private UserLogin userLogin;
    
    /** Request object containing post creation data */
    protected CreateNewPostServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link CreateNewPostServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new CreateNewPostServicesREQ();
        req = (CreateNewPostServicesREQ) JamiiMapperUtils.mapObject(getRequest(), CreateNewPostServicesREQ.class);
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

        // Validate post content
        if (req.getContent() == null || req.getContent().trim().isEmpty()) {
            this.jamiiErrorsMessagesRESP.setCreateNewPost_InvalidContent();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        // Check content length (max 2000 characters as defined in UserPostsTBL)
        if (req.getContent().length() > 2000) {
            this.jamiiErrorsMessagesRESP.setCreateNewPost_InvalidContent();
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

        try {
            // Create new post
            UserPostsTBL newPost = userPosts.createPost(user, req.getContent().trim());
            
            // Set response data
            CreateNewPostRESP response = new CreateNewPostRESP();
            response.setPostId(newPost.getId());
            response.setCreationDate(newPost.getCreationdate().toString());
            
            this.Response = response;
        } catch (Exception e) {
            this.jamiiErrorsMessagesRESP.setCreateNewPost_PostCreationFailed();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {
        if (getIsSuccessful()) {
            return new ResponseEntity<>(this.Response, HttpStatus.CREATED);
        }
        
        return super.getResponse();
    }
}
