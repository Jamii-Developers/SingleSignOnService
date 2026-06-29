package com.jamii.jSocial.controllers;

import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jSocial.model.UserPostsTBL;
import com.jamii.jSocial.repo.UserPostsREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service component for managing user post records in the database.
 * 
 * <p>This class handles user-generated posts, including creation, modification,
 * deletion, and retrieval operations. Posts support engagement metrics like likes
 * and comments count.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Create new user posts</li>
 *     <li>Fetch posts by user ID, status, and other criteria</li>
 *     <li>Update post content and metadata</li>
 *     <li>Soft delete posts (mark as deleted)</li>
 *     <li>Manage engagement metrics (likes, comments)</li>
 *     <li>Save post records individually or in batch</li>
 * </ul>
 */
@Component
public class UserPosts
{

    /**
     * Post status constant for active posts.
     */
    public static final String STATUS_ACTIVE = UserPostsTBL.STATUS_ACTIVE;
    
    /**
     * Post status constant for deleted posts.
     */
    public static final String STATUS_DELETED = UserPostsTBL.STATUS_DELETED;
    
    /**
     * Post status constant for draft posts.
     */
    public static final String STATUS_DRAFT = UserPostsTBL.STATUS_DRAFT;

    @Autowired
    private UserPostsREPO userPostsREPO;

    /** Current post data being processed */
    public UserPostsTBL data;
    
    /** List of post data for batch operations */
    public List<UserPostsTBL> dataList;

    /**
     * Creates a new post for the specified user.
     * 
     * @param userid the user creating the post
     * @param content the post content
     * @return the created post entity
     */
    public UserPostsTBL createPost(UserLoginTBL userid, String content) {
        UserPostsTBL post = new UserPostsTBL();
        post.setUserid(userid);
        post.setContent(content);
        post.setStatus(STATUS_ACTIVE);
        post.setCreationdate(LocalDateTime.now());
        post.setLastupdated(LocalDateTime.now());
        post.setLikescount(0);
        post.setCommentscount(0);
        
        return userPostsREPO.save(post);
    }

    /**
     * Fetches a post by its unique identifier.
     * 
     * @param postId the post ID to fetch
     * @return Optional containing the post if found
     */
    public Optional<UserPostsTBL> fetchById(Integer postId) {
        return userPostsREPO.findById(postId);
    }

    /**
     * Fetches posts by user ID and status.
     * 
     * @param userid the user whose posts to fetch
     * @param status the status filter (active, deleted, etc.)
     * @return List of posts matching the criteria
     */
    public List<UserPostsTBL> fetchByUserAndStatus(UserLoginTBL userid, String status) {
        return userPostsREPO.findByUseridAndStatus(userid, status);
    }

    /**
     * Fetches all posts for a specific user.
     * 
     * @param userid the user whose posts to fetch
     * @return List of all posts for the user
     */
    public List<UserPostsTBL> fetchByUser(UserLoginTBL userid) {
        return userPostsREPO.findByUserid(userid);
    }

    /**
     * Fetches posts by status.
     * 
     * @param status the status filter
     * @return List of posts with the specified status
     */
    public List<UserPostsTBL> fetchByStatus(String status) {
        return userPostsREPO.findByStatus(status);
    }

    /**
     * Updates an existing post's content.
     * 
     * @param post the post to update
     * @param content the new content
     * @return the updated post entity
     */
    public UserPostsTBL updatePostContent(UserPostsTBL post, String content) {
        post.setContent(content);
        post.setLastupdated(LocalDateTime.now());
        return userPostsREPO.save(post);
    }

    /**
     * Soft deletes a post by marking it as deleted.
     * 
     * @param post the post to delete
     * @return the updated post entity
     */
    public UserPostsTBL deletePost(UserPostsTBL post) {
        post.setStatus(STATUS_DELETED);
        post.setLastupdated(LocalDateTime.now());
        return userPostsREPO.save(post);
    }

    /**
     * Increments the likes count for a post.
     * 
     * @param post the post to update
     * @return the updated post entity
     */
    public UserPostsTBL incrementLikes(UserPostsTBL post) {
        post.setLikescount(post.getLikescount() + 1);
        post.setLastupdated(LocalDateTime.now());
        return userPostsREPO.save(post);
    }

    /**
     * Decrements the likes count for a post.
     * 
     * @param post the post to update
     * @return the updated post entity
     */
    public UserPostsTBL decrementLikes(UserPostsTBL post) {
        post.setLikescount(Math.max(0, post.getLikescount() - 1));
        post.setLastupdated(LocalDateTime.now());
        return userPostsREPO.save(post);
    }

    /**
     * Increments the comments count for a post.
     * 
     * @param post the post to update
     * @return the updated post entity
     */
    public UserPostsTBL incrementComments(UserPostsTBL post) {
        post.setCommentscount(post.getCommentscount() + 1);
        post.setLastupdated(LocalDateTime.now());
        return userPostsREPO.save(post);
    }

    /**
     * Decrements the comments count for a post.
     * 
     * @param post the post to update
     * @return the updated post entity
     */
    public UserPostsTBL decrementComments(UserPostsTBL post) {
        post.setCommentscount(Math.max(0, post.getCommentscount() - 1));
        post.setLastupdated(LocalDateTime.now());
        return userPostsREPO.save(post);
    }

    /**
     * Saves the current post data to the database.
     */
    public void save() {
        if (data != null) {
            userPostsREPO.save(data);
        }
    }

    /**
     * Saves all posts in the data list to the database.
     */
    public void saveAll() {
        if (dataList != null && !dataList.isEmpty()) {
            userPostsREPO.saveAll(dataList);
        }
    }

    /**
     * Fetches posts created within a specific date range.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return List of posts created in the date range
     */
    public List<UserPostsTBL> fetchByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return userPostsREPO.findByCreationdateBetween(startDate, endDate);
    }

    /**
     * Fetches posts with content containing the specified text.
     * 
     * @param searchText the text to search for
     * @return List of posts containing the search text
     */
    public List<UserPostsTBL> fetchByContentContaining(String searchText) {
        return userPostsREPO.findByContentContainingIgnoreCase(searchText);
    }
}
