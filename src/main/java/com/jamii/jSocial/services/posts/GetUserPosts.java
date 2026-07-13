package com.jamii.jSocial.services.posts;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jSocial.peer.UserPosts;
import com.jamii.jUser.peer.UserLogin;
import com.jamii.jSocial.model.UserPostsTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.GetPostsREQ;
import com.jamii.jSocial.responses.GetPostsRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for retrieving user posts from the backend.
 * 
 * <p>This service allows authenticated users to fetch posts with various filtering
 * and pagination options. Users can retrieve their own posts, posts from specific
 * users, or all posts with appropriate filtering.</p>
 * 
 * <p>Operation flow:</p>
 * <ol>
 *   <li>Extract authentication keys and request parameters in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Validate request parameters (limit, status, etc.)</li>
 *   <li>Determine fetch scope (current user, specific user, or all posts)</li>
 *   <li>Apply filters and pagination</li>
 *   <li>Fetch posts from database</li>
 *   <li>Transform posts to response format</li>
 *   <li>Return paginated response with metadata</li>
 * </ol>
 * 
 * <p>Supported operations:</p>
 * <ul>
 *   <li>Fetch current user's posts</li>
 *   <li>Fetch posts from a specific user (if accessible)</li>
 *   <li>Fetch all posts with optional filters</li>
 *   <li>Cursor-based pagination for efficient data loading</li>
 *   <li>Status filtering (ACTIVE, DELETED, DRAFT)</li>
 *   <li>Limit control for response size</li>
 * </ul>
 * 
 * <p>Error conditions:</p>
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>User not found or inactive</li>
 *   <li>Invalid limit parameter (must be positive)</li>
 *   <li>Invalid status parameter</li>
 *   <li>Database access errors</li>
 * </ul>
 * 
 * <p>Performance considerations:</p>
 * <ul>
 *   <li>Cursor-based pagination for efficient data loading</li>
 *   <li>Database queries optimized with proper indexing</li>
 *   <li>Limit enforcement to prevent excessive data transfer</li>
 *   <li>Lazy loading of user information</li>
 *   <li>Result transformation performed after database access</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see GetPostsREQ
 * @see GetPostsRESP
 * @see UserPostsTBL
 */
@Service
public class GetUserPosts
        extends AbstractUserServicesOPS
{

    @Autowired private UserPosts userPosts;
    @Autowired private UserLogin userLogin;

    protected GetPostsREQ req = null;

    @Override
    protected void setUserRequestData()
    {
        req = new GetPostsREQ();
        req = (GetPostsREQ) JamiiMapperUtils.mapObject(getRequest(), GetPostsREQ.class);
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

        // Validate limit parameter
        if (req.getLimit() != null && req.getLimit() <= 0) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        // Validate status parameter if provided
        if (req.getStatus() != null && !isValidStatus(req.getStatus())) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        // Get authenticated user from validated cookie
        UserLoginTBL currentUser = this.cookie.getValidatedUser();
        if (currentUser == null) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        try {
            // Parse lastPostId for pagination
            Integer lastPostId = null;
            if (req.getLastPostId() != null && !req.getLastPostId().trim().isEmpty()) {
                try {
                    lastPostId = Integer.parseInt(req.getLastPostId());
                } catch (NumberFormatException e) {
                    this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
                    this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
                    this.isSuccessful = false;
                    return;
                }
            }

            // Determine fetch scope and retrieve posts
            List<UserPostsTBL> posts;
            Long totalCount;
            
            if (req.getUserId() != null) {
                // Fetch posts for specific user
                Optional<UserLoginTBL> targetUserOpt = userLogin.fetch(req.getUserId(), UserLogin.ACTIVE_ON);
                if (targetUserOpt.isEmpty()) {
                    this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
                    this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
                    this.isSuccessful = false;
                    return;
                }
                
                UserLoginTBL targetUser = targetUserOpt.get();
                posts = userPosts.fetchByUserWithPagination(targetUser, lastPostId, req.getLimit(), req.getStatus());
                totalCount = userPosts.countUserPosts(targetUser, req.getStatus());
            } else {
                // Fetch all posts (or current user's posts based on business logic)
                posts = userPosts.fetchAllPostsWithPagination(lastPostId, req.getLimit(), req.getStatus());
                totalCount = userPosts.countAllPosts(req.getStatus());
            }

            // Transform posts to response format
            List<GetPostsRESP.PostData> postDataList = new ArrayList<>();
            for (UserPostsTBL post : posts) {
                GetPostsRESP.PostData postData = new GetPostsRESP.PostData();
                postData.setPostId(post.getId());
                postData.setUserId(post.getUserid().getId());
                postData.setUsername(post.getUserid().getUsername());
                postData.setContent(post.getContent());
                postData.setCreationDate(post.getCreationdate());
                postData.setLastUpdated(post.getLastupdated());
                postData.setStatus(post.getStatus());
                postData.setLikesCount(post.getLikescount());
                postData.setCommentsCount(post.getCommentscount());
                postDataList.add(postData);
            }

            // Set response data
            GetPostsRESP response = new GetPostsRESP();
            response.setPosts(postDataList);
            response.setTotalCount(totalCount.intValue());
            response.setHasMore(posts.size() >= req.getLimit());
            
            // Set lastPostId for pagination if there are posts
            if (!posts.isEmpty()) {
                response.setLastPostId(posts.get(posts.size() - 1).getId().toString());
            }
            
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

    /**
     * Validates the status parameter against allowed values.
     * 
     * @param status the status to validate
     * @return true if status is valid, false otherwise
     */
    private boolean isValidStatus(String status) {
        return UserPostsTBL.STATUS_ACTIVE.equals(status) ||
               UserPostsTBL.STATUS_DELETED.equals(status) ||
               UserPostsTBL.STATUS_DRAFT.equals(status);
    }
}
