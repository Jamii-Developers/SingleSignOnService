package com.jamii.utils;

import com.jamii.jSocial.controllers.UserBlockList;
import com.jamii.jSocial.controllers.UserRelationship;
import com.jamii.jSocial.controllers.UserRequest;
import com.jamii.jUser.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Utility class for managing user relationships, requests, and block lists.
 * 
 * <p>This class provides methods to check the relationship status between users,
 * including friendship, following, blocking, and pending requests. It operates on
 * in-memory data loaded from entity relationships to minimize database calls.
 * 
 * <p>Typical usage pattern:
 * <pre>
 * jamiiRelationshipUtils.setSender(senderUser);
 * jamiiRelationshipUtils.setReceiver(receiverUser);
 * jamiiRelationshipUtils.initRelationShip();
 * 
 * if (jamiiRelationshipUtils.checkIfUsersAreFriends()) {
 *     // Users are friends
 * }
 * if (jamiiRelationshipUtils.checkIfUserIsBlocked()) {
 *     // Sender is blocked by receiver
 * }
 * </pre>
 * 
 * <p>Performance considerations:
 * <ul>
 *   <li>Call {@link #initRelationShip()} once before performing multiple checks</li>
 *   <li>The class caches relationship data in memory after initialization</li>
 *   <li>Multiple check methods can be called without additional database queries</li>
 *   <li>Call {@link #reset()} when switching to a new sender/receiver pair</li>
 * </ul>
 * 
 * @see UserRelationship
 * @see UserRequest
 * @see UserBlockList
 */
@Component
public class JamiiRelationshipUtils {

    /** Controller for user relationship operations */
    @Autowired
    private UserRelationship userRelationship;
    
    /** Controller for user request operations */
    @Autowired
    private UserRequest userRequest;
    
    /** Controller for user block list operations */
    @Autowired
    private UserBlockList userBlockList;

    /** The user initiating the relationship check */
    private UserLoginTBL sender;
    
    /** The user being checked against */
    private UserLoginTBL receiver;
    
    /** The relationship type (friend, follow, etc.) */
    private Integer type;
    
    /** The relationship status (active, pending, etc.) */
    private Integer status;
    
    /** Cached results for expensive checks to avoid repeated stream operations */
    private Boolean cachedIsBlocked;
    private Boolean cachedHasBlockedReceiver;
    private Boolean cachedAreFriends;
    private Boolean cachedIsFollowing;
    private Boolean cachedHasPendingFriendRequest;
    private Boolean cachedHasPendingFollowRequest;
    private Boolean cachedHasPendingRequestFromReceiver;

    /**
     * Gets the sender user.
     * @return the sender user
     */
    public UserLoginTBL getSender() {
        return sender;
    }

    /**
     * Sets the sender user.
     * @param sender the sender user to set
     */
    public void setSender(UserLoginTBL sender) {
        this.sender = sender;
        clearCache();
    }

    /**
     * Gets the receiver user.
     * @return the receiver user
     */
    public UserLoginTBL getReceiver() {
        return receiver;
    }

    /**
     * Sets the receiver user.
     * @param receiver the receiver user to set
     */
    public void setReceiver(UserLoginTBL receiver) {
        this.receiver = receiver;
        clearCache();
    }

    /**
     * Gets the relationship type.
     * @return the relationship type
     */
    public Integer getType() {
        return type;
    }

    /**
     * Sets the relationship type.
     * @param type the relationship type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * Gets the relationship status.
     * @return the relationship status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets the relationship status.
     * @param status the relationship status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Clears cached check results.
     * 
     * <p>This should be called when sender or receiver changes to ensure
     * cached results are invalidated.
     */
    private void clearCache() {
        cachedIsBlocked = null;
        cachedHasBlockedReceiver = null;
        cachedAreFriends = null;
        cachedIsFollowing = null;
        cachedHasPendingFriendRequest = null;
        cachedHasPendingFollowRequest = null;
        cachedHasPendingRequestFromReceiver = null;
    }

    /**
     * Resets the utility to its initial state.
     * 
     * <p>This method clears all data lists and cached results. Should be called
     * when switching to a new sender/receiver pair to avoid stale data.
     */
    public void reset() {
        this.sender = null;
        this.receiver = null;
        this.type = null;
        this.status = null;
        this.userBlockList.dataList = new ArrayList<>();
        this.userRequest.dataList = new ArrayList<>();
        this.userRelationship.dataList = new ArrayList<>();
        clearCache();
    }

    /**
     * Initializes the relationship data by loading block lists, requests, and relationships.
     * 
     * <p>This method loads relationship data from the sender and receiver entities
     * and filters it to only include active relationships between these two users.
     * The data is cached in memory for subsequent check operations.
     * 
     * <p>This method should be called once after setting sender and receiver,
     * before calling any of the check methods.
     * 
     * <p>Note: This relies on the entities having their relationships eagerly loaded
     * or properly initialized. If relationships are lazy-loaded, this may trigger
     * database queries.
     */
    public void initRelationShip() {
        clearCache();

        // Initialize Available BlockList - filter for active blocks between sender and receiver
        this.userBlockList.dataList = new ArrayList<>();
        this.userBlockList.dataList.addAll(
                sender.getUseridUserBlockListTBL().stream()
                        .filter( x -> Objects.equals( x.getStatus(), UserBlockList.STATUS_ACTIVE) 
                                && Objects.equals( x.getBlockedid().getId(), receiver.getId( ) ) )
                        .toList()
        );

        this.userBlockList.dataList.addAll(
                receiver.getBlockedidUserBlockListTBL().stream()
                        .filter( x -> Objects.equals( x.getStatus(), UserBlockList.STATUS_ACTIVE) 
                                && Objects.equals( x.getBlockedid().getId(), sender.getId( ) ) )
                        .toList()
        );

        // Initialize Available Requests - filter for active requests between sender and receiver
        this.userRequest.dataList = new ArrayList<>();
        this.userRequest.dataList.addAll(
                sender.getSenderIDUserRequests().stream()
                        .filter( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE) 
                                && Objects.equals( x.getReceiverid().getId(), receiver.getId( ) ) )
                        .toList()
        );

        this.userRequest.dataList.addAll(
                receiver.getSenderIDUserRequests().stream()
                        .filter( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE) 
                                && Objects.equals( x.getReceiverid().getId(), sender.getId( ) ) )
                        .toList()
        );

        // Initialize Available Relationships - filter for active relationships between sender and receiver
        this.userRelationship.dataList = new ArrayList<>();
        this.userRelationship.dataList.addAll(
                sender.getSenderIDuserRelationship().stream()
                        .filter( x -> Objects.equals( x.getStatus(), UserRelationship.STATUS_ACTIVE) 
                                && Objects.equals( x.getReceiverid().getId(), receiver.getId( ) ) )
                        .toList()
        );

        this.userRelationship.dataList.addAll(
                receiver.getSenderIDuserRelationship().stream()
                        .filter( x -> Objects.equals( x.getStatus(), UserRelationship.STATUS_ACTIVE) 
                                && Objects.equals( x.getReceiverid().getId(), sender.getId( ) ) )
                        .toList()
        );
    }

    /**
     * Checks if the sender is blocked by the receiver.
     * 
     * <p>This method checks if the receiver has blocked the sender in their block list.
     * The result is cached after the first call for performance.
     * 
     * @return true if the sender is blocked by the receiver, false otherwise
     */
    public boolean checkIfUserIsBlocked() {
        if (cachedIsBlocked != null) {
            return cachedIsBlocked;
        }
        
        cachedIsBlocked = !this.userBlockList.dataList.isEmpty()
                && this.userBlockList.dataList.stream().anyMatch(
                x -> Objects.equals(x.getStatus(), UserBlockList.STATUS_ACTIVE)
                        && Objects.equals(x.getUserid().getId(), receiver.getId()));
        return cachedIsBlocked;
    }

    /**
     * Checks if the sender has blocked the receiver.
     * 
     * <p>This method checks if the sender has blocked the receiver in their block list.
     * The result is cached after the first call for performance.
     * 
     * @return true if the sender has blocked the receiver, false otherwise
     */
    public boolean checkIfUserHasBlockedReceiver() {
        if (cachedHasBlockedReceiver != null) {
            return cachedHasBlockedReceiver;
        }
        
        cachedHasBlockedReceiver = !this.userBlockList.dataList.isEmpty()
                && this.userBlockList.dataList.stream().anyMatch(
                x -> Objects.equals(x.getStatus(), UserBlockList.STATUS_ACTIVE)
                        && Objects.equals(x.getUserid().getId(), sender.getId()));
        return cachedHasBlockedReceiver;
    }

    /**
     * Checks if the sender and receiver have a valid friendship relationship.
     * 
     * <p>This method checks if there is an active friendship relationship
     * between the sender and receiver. The result is cached after the first call.
     * 
     * @return true if the users are friends, false otherwise
     */
    public boolean checkIfUsersAreFriends() {
        if (cachedAreFriends != null) {
            return cachedAreFriends;
        }
        
        cachedAreFriends = !this.userRelationship.dataList.isEmpty() &&
                this.userRelationship.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRelationship.STATUS_ACTIVE)
                                && Objects.equals(x.getType(), UserRelationship.TYPE_FRIEND));
        return cachedAreFriends;
    }

    /**
     * Checks if the sender is following the receiver.
     * 
     * <p>This method checks if the sender has an active follow relationship
     * with the receiver. The result is cached after the first call.
     * 
     * @return true if the sender is following the receiver, false otherwise
     */
    public boolean checkIfUserIsFollowing() {
        if (cachedIsFollowing != null) {
            return cachedIsFollowing;
        }
        
        cachedIsFollowing = !this.userRelationship.dataList.isEmpty() &&
                this.userRelationship.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRelationship.STATUS_ACTIVE) &&
                                Objects.equals(x.getSenderid().getId(), sender.getId()) &&
                                Objects.equals(x.getType(), UserRelationship.TYPE_FOLLOW));
        return cachedIsFollowing;
    }

    /**
     * Checks if the sender has a pending friend request to the receiver.
     * 
     * <p>This method checks if there is an active friend request from sender to receiver.
     * The result is cached after the first call.
     * 
     * @return true if there is a pending friend request, false otherwise
     */
    public boolean checkIfUserHasPendingFriendRequest() {
        if (cachedHasPendingFriendRequest != null) {
            return cachedHasPendingFriendRequest;
        }
        
        cachedHasPendingFriendRequest = !this.userRequest.dataList.isEmpty() &&
                this.userRequest.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE) &&
                                Objects.equals(x.getSenderid().getId(), sender.getId()) &&
                                Objects.equals(x.getType(), UserRequest.TYPE_FRIEND));
        return cachedHasPendingFriendRequest;
    }

    /**
     * Checks if the receiver has a pending friend request to the sender.
     * 
     * <p>This method checks if there is an active friend request from receiver to sender.
     * The result is cached after the first call.
     * 
     * @return true if there is a pending friend request from the receiver, false otherwise
     */
    public boolean checkifUserHasAPendingRequestFriendFromReceiver() {
        if (cachedHasPendingRequestFromReceiver != null) {
            return cachedHasPendingRequestFromReceiver;
        }
        
        cachedHasPendingRequestFromReceiver = !this.userRequest.dataList.isEmpty() &&
                this.userRequest.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE) &&
                                Objects.equals(x.getSenderid().getId(), receiver.getId()) &&
                                Objects.equals(x.getType(), UserRequest.TYPE_FRIEND));
        return cachedHasPendingRequestFromReceiver;
    }

    /**
     * Checks if the sender has a pending follow request to the receiver.
     * 
     * <p>This method checks if there is an active follow request from sender to receiver.
     * The result is cached after the first call.
     * 
     * @return true if there is a pending follow request, false otherwise
     */
    public boolean checkIfUserHasPendingFollowRequest() {
        if (cachedHasPendingFollowRequest != null) {
            return cachedHasPendingFollowRequest;
        }
        
        cachedHasPendingFollowRequest = !this.userRequest.dataList.isEmpty() &&
                this.userRequest.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE) &&
                                Objects.equals(x.getSenderid().getId(), sender.getId()) &&
                                Objects.equals(x.getType(), UserRequest.TYPE_FOLLOW));
        return cachedHasPendingFollowRequest;
    }

}
