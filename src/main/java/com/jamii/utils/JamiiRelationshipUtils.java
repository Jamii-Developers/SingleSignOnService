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
 * <p>This class provides methods to check the relationship status between jUser,
 * including friendship, following, blocking, and pending requests.</p>
 */
@Component
public class JamiiRelationshipUtils {

    @Autowired
    private UserRelationship userRelationship;
    @Autowired
    private UserRequest userRequest;
    @Autowired
    private UserBlockList userBlockList;

    private UserLoginTBL sender;
    private UserLoginTBL receiver;
    private Integer type;
    private Integer status;

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
     * Initializes the relationship data by loading block lists, requests, and relationships.
     */
    public void initRelationShip() {

        // Initialize Available BlockList
        this.userBlockList.dataList = new ArrayList<>();
        this.userBlockList.dataList.addAll(
                sender.getUseridUserBlockListTBL().stream()
                        .filter( x -> Objects.equals( x.getStatus(), UserBlockList.STATUS_ACTIVE) && Objects.equals( x.getBlockedid().getId(), receiver.getId( ) ) )
                        .toList()
        );

        this.userBlockList.dataList.addAll(
                receiver.getBlockedidUserBlockListTBL().stream()
                        .filter( x -> Objects.equals( x.getStatus(), UserBlockList.STATUS_ACTIVE) && Objects.equals( x.getBlockedid().getId(), sender.getId( ) ) )
                        .toList()
        );

        // Initialize Available Requests
        this.userRequest.dataList = new ArrayList<>();
        this.userRequest.dataList.addAll(
                sender.getSenderIDUserRequests().stream()
                        .filter( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE) && Objects.equals( x.getReceiverid().getId(), receiver.getId( ) ) )
                        .toList()
        );

        this.userRequest.dataList.addAll(
                receiver.getSenderIDUserRequests().stream()
                        .filter( x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE) && Objects.equals( x.getReceiverid().getId(), sender.getId( ) ) )
                        .toList()
        );

        // Initialize Available Relationships
        this.userRelationship.dataList = new ArrayList<>();
        this.userRelationship.dataList.addAll(
                sender.getSenderIDuserRelationship().stream()
                        .filter( x -> Objects.equals( x.getStatus(), UserRelationship.STATUS_ACTIVE) && Objects.equals( x.getReceiverid().getId(), receiver.getId( ) ) )
                        .toList()
        );

        this.userRelationship.dataList.addAll(
                receiver.getSenderIDuserRelationship().stream()
                        .filter( x -> Objects.equals( x.getStatus(), UserRelationship.STATUS_ACTIVE) && Objects.equals( x.getReceiverid().getId(), sender.getId( ) ) )
                        .toList()
        );


    }

    /**
     * Checks if the sender is blocked by the receiver.
     * @return true if the sender is blocked, false otherwise
     */
    public boolean checkIfUserIsBlocked() {

        return !this.userBlockList.dataList.isEmpty()
                && this.userBlockList.dataList.stream().anyMatch(
                x -> Objects.equals(
                        x.getStatus(), UserRequest.STATUS_ACTIVE)
                        && x.getUserid() == getReceiver());
    }

    /**
     * Checks if the sender has blocked the receiver.
     * @return true if the sender has blocked the receiver, false otherwise
     */
    public boolean checkIfUserHasBlockedReceiver() {

        return !this.userBlockList.dataList.isEmpty()
                && this.userBlockList.dataList.stream().anyMatch(
                x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE)
                        && x.getUserid() == getSender());

    }

    /**
     * Checks if the sender and receiver have a valid friendship relationship.
     * @return true if the jUser are friends, false otherwise
     */
    public boolean checkIfUsersAreFriends() {

        return !this.userRelationship.dataList.isEmpty() &&
                this.userRelationship.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRelationship.STATUS_ACTIVE)
                                && Objects.equals(x.getType(), UserRelationship.TYPE_FRIEND));
    }

    /**
     * Checks if the sender is following the receiver.
     * @return true if the sender is following the receiver, false otherwise
     */
    public boolean checkIfUserIsFollowing() {

        return !this.userRelationship.dataList.isEmpty() &&
                this.userRelationship.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRelationship.STATUS_ACTIVE) &&
                                x.getSenderid() == getSender() &&
                                Objects.equals(x.getType(), UserRelationship.TYPE_FOLLOW));
    }

    /**
     * Checks if the sender has a pending friend request to the receiver.
     * @return true if there is a pending friend request, false otherwise
     */
    public boolean checkIfUserHasPendingFriendRequest() {

        return !this.userRequest.dataList.isEmpty() &&
                this.userRequest.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE) &&
                                x.getSenderid() == getSender() &&
                                Objects.equals(x.getType(), UserRequest.TYPE_FRIEND));
    }

    /**
     * Checks if the receiver has a pending friend request to the sender.
     * @return true if there is a pending friend request from the receiver, false otherwise
     */
    public boolean checkifUserHasAPendingRequestFriendFromReceiver() {
        return !this.userRequest.dataList.isEmpty() &&
                this.userRequest.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE) &&
                                x.getSenderid() == getReceiver() &&
                                Objects.equals(x.getType(), UserRequest.TYPE_FRIEND));
    }

    /**
     * Checks if the sender has a pending follow request to the receiver.
     * @return true if there is a pending follow request, false otherwise
     */
    public boolean checkIfUserHasPendingFollowRequest() {

        return !this.userRequest.dataList.isEmpty() &&
                this.userRequest.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE) &&
                                x.getSenderid() == getSender() &&
                                Objects.equals(x.getType(), UserRelationship.TYPE_FOLLOW));
    }

}
