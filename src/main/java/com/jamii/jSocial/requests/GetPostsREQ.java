package com.jamii.jSocial.requests;

import com.jamii.abstractClasses.AbstractUserServicesREQ;

/**
 * Request object for retrieving user posts.
 * 
 * <p>This class contains the parameters needed to fetch posts from the backend.
 * It supports various filtering options including user-specific posts, pagination,
 * and status filtering.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *   <li>{@code userKey} - User authentication key (inherited)</li>
 *   <li>{@code deviceKey} - Device authentication key (inherited)</li>
 *   <li>{@code sessionKey} - Session authentication key (inherited)</li>
 *   <li>{@code lastPostId} - Last post ID for pagination/cursor-based fetching</li>
 *   <li>{@code userId} - Optional: Specific user ID to fetch posts for</li>
 *   <li>{@code limit} - Maximum number of posts to return (default: 20)</li>
 *   <li>{@code status} - Optional: Filter by post status</li>
 * </ul>
 */
public class GetPostsREQ
        extends AbstractUserServicesREQ
{
    /** Last post ID for pagination/cursor-based fetching */
    private String lastPostId;
    
    /** Optional: Specific user ID to fetch posts for (null for current user's posts) */
    private Integer userId;
    
    /** Maximum number of posts to return */
    private Integer limit = 20;
    
    /** Optional: Filter by post status (ACTIVE, DELETED, DRAFT) */
    private String status;

    public String getLastPostId()
    {
        return lastPostId;
    }

    public void setLastPostId(String lastPostId)
    {
        this.lastPostId = lastPostId;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Integer getLimit()
    {
        return limit;
    }

    public void setLimit(Integer limit)
    {
        this.limit = limit != null ? limit : 20;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
