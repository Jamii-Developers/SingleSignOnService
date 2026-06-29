package com.jamii.jSocial.model;

import com.jamii.jUser.model.UserLoginTBL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserPostsTBL entity.
 * 
 * <p>These tests verify the functionality of the UserPostsTBL entity including
 * field validation, default values, and relationships.</p>
 */
class UserPostsTBLTest {

    private UserPostsTBL userPost;
    private UserLoginTBL user;

    @BeforeEach
    void setUp() {
        userPost = new UserPostsTBL();
        user = new UserLoginTBL();
        user.setId(1);
    }

    @Test
    void testDefaultConstructor_SetsDefaultValues() {
        // When
        UserPostsTBL newPost = new UserPostsTBL();

        // Then
        assertNotNull(newPost.getCreationdate());
        assertNotNull(newPost.getLastupdated());
        assertEquals(UserPostsTBL.STATUS_ACTIVE, newPost.getStatus());
        assertEquals(0, newPost.getLikescount());
        assertEquals(0, newPost.getCommentscount());
    }

    @Test
    void testSetAndGetId() {
        // Given
        Integer expectedId = 123;

        // When
        userPost.setId(expectedId);

        // Then
        assertEquals(expectedId, userPost.getId());
    }

    @Test
    void testSetAndGetUserid() {
        // Given
        UserLoginTBL expectedUser = new UserLoginTBL();
        expectedUser.setId(456);

        // When
        userPost.setUserid(expectedUser);

        // Then
        assertEquals(expectedUser, userPost.getUserid());
        assertEquals(456, userPost.getUserid().getId());
    }

    @Test
    void testSetAndGetContent() {
        // Given
        String expectedContent = "This is a test post content.";

        // When
        userPost.setContent(expectedContent);

        // Then
        assertEquals(expectedContent, userPost.getContent());
    }

    @Test
    void testSetAndGetCreationDate() {
        // Given
        LocalDateTime expectedDate = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

        // When
        userPost.setCreationdate(expectedDate);

        // Then
        assertEquals(expectedDate, userPost.getCreationdate());
    }

    @Test
    void testSetAndGetLastUpdated() {
        // Given
        LocalDateTime expectedDate = LocalDateTime.of(2024, 1, 1, 12, 30, 0);

        // When
        userPost.setLastupdated(expectedDate);

        // Then
        assertEquals(expectedDate, userPost.getLastupdated());
    }

    @Test
    void testSetAndGetStatus() {
        // Given
        String expectedStatus = UserPostsTBL.STATUS_DELETED;

        // When
        userPost.setStatus(expectedStatus);

        // Then
        assertEquals(expectedStatus, userPost.getStatus());
    }

    @Test
    void testSetAndGetLikesCount() {
        // Given
        Integer expectedLikes = 42;

        // When
        userPost.setLikescount(expectedLikes);

        // Then
        assertEquals(expectedLikes, userPost.getLikescount());
    }

    @Test
    void testSetAndGetCommentsCount() {
        // Given
        Integer expectedComments = 15;

        // When
        userPost.setCommentscount(expectedComments);

        // Then
        assertEquals(expectedComments, userPost.getCommentscount());
    }

    @Test
    void testStatusConstants() {
        // Then
        assertEquals("ACTIVE", UserPostsTBL.STATUS_ACTIVE);
        assertEquals("DELETED", UserPostsTBL.STATUS_DELETED);
        assertEquals("DRAFT", UserPostsTBL.STATUS_DRAFT);
    }

    @Test
    void testContentLengthValidation_MaxLength() {
        // Given
        String maxLengthContent = "a".repeat(2000);

        // When
        userPost.setContent(maxLengthContent);

        // Then
        assertEquals(2000, userPost.getContent().length());
        assertEquals(maxLengthContent, userPost.getContent());
    }

    @Test
    void testPostCreation_WithUser() {
        // Given
        userPost.setUserid(user);
        userPost.setContent("Test post with user");

        // When
        UserLoginTBL retrievedUser = userPost.getUserid();

        // Then
        assertNotNull(retrievedUser);
        assertEquals(1, retrievedUser.getId());
        assertEquals("Test post with user", userPost.getContent());
    }

    @Test
    void testTimestamps_AreSetOnCreation() {
        // When
        UserPostsTBL newPost = new UserPostsTBL();
        LocalDateTime now = LocalDateTime.now();

        // Then
        assertNotNull(newPost.getCreationdate());
        assertNotNull(newPost.getLastupdated());
        assertTrue(newPost.getCreationdate().isBefore(now.plusSeconds(1)));
        assertTrue(newPost.getLastupdated().isBefore(now.plusSeconds(1)));
    }

    @Test
    void testPostWithNullUser() {
        // When
        userPost.setUserid(null);

        // Then
        assertNull(userPost.getUserid());
    }

    @Test
    void testPostWithEmptyContent() {
        // When
        userPost.setContent("");

        // Then
        assertEquals("", userPost.getContent());
    }

    @Test
    void testPostWithNullContent() {
        // When
        userPost.setContent(null);

        // Then
        assertNull(userPost.getContent());
    }

    @Test
    void testNegativeCounts() {
        // Given
        Integer negativeCount = -5;

        // When
        userPost.setLikescount(negativeCount);
        userPost.setCommentscount(negativeCount);

        // Then
        assertEquals(negativeCount, userPost.getLikescount());
        assertEquals(negativeCount, userPost.getCommentscount());
    }
}
