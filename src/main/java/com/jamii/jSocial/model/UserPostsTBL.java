package com.jamii.jSocial.model;

import com.jamii.jUser.model.UserLoginTBL;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * JPA Entity representing user posts in the database.
 * 
 * <p>This class maps to the {@code user_posts} table and stores information about
 * user-generated posts, including content, metadata, and engagement metrics.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code userid} - User who created the post</li>
 *     <li>{@code content} - Post content text</li>
 *     <li>{@code status} - Post status (active, deleted, etc.)</li>
 *     <li>{@code likesCount} - Number of likes</li>
 *     <li>{@code commentsCount} - Number of comments</li>
 * </ul>
 */
@Entity
@Table(name = "user_posts")
public class UserPostsTBL
{

    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "user_posts";
    
    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for user ID.
     */
    public static final String USER_ID = "USER_ID";
    
    /**
     * Column name constant for content.
     */
    public static final String CONTENT = "CONTENT";
    
    /**
     * Column name constant for creation date.
     */
    public static final String CREATION_DATE = "CREATION_DATE";
    
    /**
     * Column name constant for last updated date.
     */
    public static final String LAST_UPDATED = "LAST_UPDATED";
    
    /**
     * Column name constant for status.
     */
    public static final String STATUS = "STATUS";
    
    /**
     * Column name constant for likes count.
     */
    public static final String LIKES_COUNT = "LIKES_COUNT";
    
    /**
     * Column name constant for comments count.
     */
    public static final String COMMENTS_COUNT = "COMMENTS_COUNT";

    /**
     * Post status constants.
     */
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_DELETED = "DELETED";
    public static final String STATUS_DRAFT = "DRAFT";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = USER_ID, nullable = false)
    private UserLoginTBL userid;

    @Lob
    @Column(name = CONTENT, nullable = false, length = 2000, columnDefinition = "TEXT")
    private String content;

    @Column(name = CREATION_DATE, nullable = false)
    private LocalDateTime creationdate;

    @Column(name = LAST_UPDATED, nullable = false)
    private LocalDateTime lastupdated;

    @Column(name = STATUS, nullable = false, length = 20)
    private String status;

    @Column(name = LIKES_COUNT, nullable = false)
    private Integer likescount;

    @Column(name = COMMENTS_COUNT, nullable = false)
    private Integer commentscount;

    // Constructors
    public UserPostsTBL() {
        this.creationdate = LocalDateTime.now();
        this.lastupdated = LocalDateTime.now();
        this.status = STATUS_ACTIVE;
        this.likescount = 0;
        this.commentscount = 0;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserLoginTBL getUserid() {
        return userid;
    }

    public void setUserid(UserLoginTBL userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(LocalDateTime creationdate) {
        this.creationdate = creationdate;
    }

    public LocalDateTime getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(LocalDateTime lastupdated) {
        this.lastupdated = lastupdated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLikescount() {
        return likescount;
    }

    public void setLikescount(Integer likescount) {
        this.likescount = likescount;
    }

    public Integer getCommentscount() {
        return commentscount;
    }

    public void setCommentscount(Integer commentscount) {
        this.commentscount = commentscount;
    }
}
