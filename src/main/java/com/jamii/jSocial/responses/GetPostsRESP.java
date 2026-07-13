package com.jamii.jSocial.responses;

import com.jamii.abstractClasses.AbstractResponses;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response object for user posts retrieval operations.
 * 
 * <p>This class contains the response data returned by the GetUserPosts service.
 * It includes the list of posts, pagination information, and metadata.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *   <li>{@code posts} - List of post objects with full post data</li>
 *   <li>{@code totalCount} - Total number of posts matching the criteria</li>
 *   <li>{@code hasMore} - Whether more posts are available for pagination</li>
 *   <li>{@code lastPostId} - ID of the last post for cursor-based pagination</li>
 * </ul>
 */
public class GetPostsRESP extends AbstractResponses
{
    /** List of posts matching the request criteria */
    private List<PostData> posts;
    
    /** Total number of posts matching the criteria */
    private Integer totalCount;
    
    /** Whether more posts are available for pagination */
    private Boolean hasMore;
    
    /** ID of the last post for cursor-based pagination */
    private String lastPostId;

    // Constructors
    public GetPostsRESP() {
    }

    public GetPostsRESP(List<PostData> posts) {
        this.posts = posts;
        this.totalCount = posts != null ? posts.size() : 0;
        this.hasMore = false;
    }

    // Getters and Setters
    public List<PostData> getPosts() {
        return posts;
    }

    public void setPosts(List<PostData> posts) {
        this.posts = posts;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public String getLastPostId() {
        return lastPostId;
    }

    public void setLastPostId(String lastPostId) {
        this.lastPostId = lastPostId;
    }

    /**
     * Inner class representing individual post data in the response.
     */
    public static class PostData {
        
        /** Post unique identifier */
        private Integer postId;
        
        /** User ID of the post author */
        private Integer userId;
        
        /** Username of the post author */
        private String username;
        
        /** Post content text */
        private String content;
        
        /** Post creation date and time */
        private LocalDateTime creationDate;
        
        /** Post last updated date and time */
        private LocalDateTime lastUpdated;
        
        /** Post status (ACTIVE, DELETED, DRAFT) */
        private String status;
        
        /** Number of likes on the post */
        private Integer likesCount;
        
        /** Number of comments on the post */
        private Integer commentsCount;

        // Constructors
        public PostData() {
        }

        public PostData(Integer postId, Integer userId, String username, String content, 
                       LocalDateTime creationDate, LocalDateTime lastUpdated, 
                       String status, Integer likesCount, Integer commentsCount) {
            this.postId = postId;
            this.userId = userId;
            this.username = username;
            this.content = content;
            this.creationDate = creationDate;
            this.lastUpdated = lastUpdated;
            this.status = status;
            this.likesCount = likesCount;
            this.commentsCount = commentsCount;
        }

        // Getters and Setters
        public Integer getPostId() {
            return postId;
        }

        public void setPostId(Integer postId) {
            this.postId = postId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public LocalDateTime getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
        }

        public LocalDateTime getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(LocalDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getLikesCount() {
            return likesCount;
        }

        public void setLikesCount(Integer likesCount) {
            this.likesCount = likesCount;
        }

        public Integer getCommentsCount() {
            return commentsCount;
        }

        public void setCommentsCount(Integer commentsCount) {
            this.commentsCount = commentsCount;
        }
    }
}
