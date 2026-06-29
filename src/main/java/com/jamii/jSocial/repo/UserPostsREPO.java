package com.jamii.jSocial.repo;

import com.jamii.jSocial.model.UserPostsTBL;
import com.jamii.jUser.model.UserLoginTBL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository interface for UserPostsTBL entities.
 * 
 * <p>This repository provides database access methods for user post operations,
 * including standard CRUD operations and custom queries for post management.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *   <li>Standard CRUD operations inherited from JpaRepository</li>
 *   <li>Query posts by user and status</li>
 *   <li>Query posts by date ranges</li>
 *   <li>Search posts by content</li>
 *   <li>Count posts by various criteria</li>
 * </ul>
 */
@Repository
public interface UserPostsREPO extends JpaRepository<UserPostsTBL, Integer>
{

    /**
     * Finds posts by user ID and status.
     * 
     * @param userid the user whose posts to find
     * @param status the status filter (active, deleted, etc.)
     * @return List of posts matching the criteria
     */
    List<UserPostsTBL> findByUseridAndStatus(UserLoginTBL userid, String status);

    /**
     * Finds all posts for a specific user.
     * 
     * @param userid the user whose posts to find
     * @return List of all posts for the user
     */
    List<UserPostsTBL> findByUserid(UserLoginTBL userid);

    /**
     * Finds posts by status.
     * 
     * @param status the status filter
     * @return List of posts with the specified status
     */
    List<UserPostsTBL> findByStatus(String status);

    /**
     * Finds posts created within a specific date range.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return List of posts created in the date range
     */
    List<UserPostsTBL> findByCreationdateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds posts with content containing the specified text (case-insensitive).
     * 
     * @param searchText the text to search for
     * @return List of posts containing the search text
     */
    List<UserPostsTBL> findByContentContainingIgnoreCase(String searchText);

    /**
     * Counts posts by user ID and status.
     * 
     * @param userid the user whose posts to count
     * @param status the status filter
     * @return Number of posts matching the criteria
     */
    Long countByUseridAndStatus(UserLoginTBL userid, String status);

    /**
     * Finds posts by user ID ordered by creation date (most recent first).
     * 
     * @param userid the user whose posts to find
     * @return List of posts ordered by creation date
     */
    @Query("SELECT p FROM UserPostsTBL p WHERE p.userid = :userid ORDER BY p.creationdate DESC")
    List<UserPostsTBL> findByUseridOrderByCreationdateDesc(@Param("userid") UserLoginTBL userid);

    /**
     * Finds posts by user ID and status ordered by creation date (most recent first).
     * 
     * @param userid the user whose posts to find
     * @param status the status filter
     * @return List of posts matching criteria ordered by creation date
     */
    @Query("SELECT p FROM UserPostsTBL p WHERE p.userid = :userid AND p.status = :status ORDER BY p.creationdate DESC")
    List<UserPostsTBL> findByUseridAndStatusOrderByCreationdateDesc(@Param("userid") UserLoginTBL userid, @Param("status") String status);

    /**
     * Finds posts by multiple users and status.
     * 
     * @param userids list of users whose posts to find
     * @param status the status filter
     * @return List of posts from the specified users
     */
    @Query("SELECT p FROM UserPostsTBL p WHERE p.userid IN :userids AND p.status = :status ORDER BY p.creationdate DESC")
    List<UserPostsTBL> findByUseridsAndStatusOrderByCreationdateDesc(@Param("userids") List<UserLoginTBL> userids, @Param("status") String status);

    /**
     * Finds posts with likes count greater than or equal to the specified value.
     * 
     * @param minLikes the minimum likes count
     * @return List of posts with at least the specified likes
     */
    List<UserPostsTBL> findByLikescountGreaterThanEqualOrderByLikescountDesc(Integer minLikes);

    /**
     * Finds posts with comments count greater than or equal to the specified value.
     * 
     * @param minComments the minimum comments count
     * @return List of posts with at least the specified comments
     */
    List<UserPostsTBL> findByCommentscountGreaterThanEqualOrderByCommentscountDesc(Integer minComments);

    /**
     * Finds posts updated within a specific date range.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return List of posts updated in the date range
     */
    List<UserPostsTBL> findByLastupdatedBetween(LocalDateTime startDate, LocalDateTime endDate);
}
