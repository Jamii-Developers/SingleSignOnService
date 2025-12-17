package com.jamii.Utils;

import com.jamii.databaseconfig.controllers.UserBlockList;
import com.jamii.databaseconfig.controllers.UserRelationship;
import com.jamii.databaseconfig.controllers.UserRequest;
import com.jamii.databaseconfig.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;

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

    public UserLoginTBL getSender() {
        return sender;
    }

    public void setSender(UserLoginTBL sender) {
        this.sender = sender;
    }

    public UserLoginTBL getReceiver() {
        return receiver;
    }

    public void setReceiver(UserLoginTBL receiver) {
        this.receiver = receiver;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void initRelationShip() {

        // Initial Available BlockList
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

    public boolean checkIfUserIsBlocked() {

        return !this.userBlockList.dataList.isEmpty()
                && this.userBlockList.dataList.stream().anyMatch(
                x -> Objects.equals(
                        x.getStatus(), UserRequest.STATUS_ACTIVE)
                        && x.getUserid() == getReceiver());
    }

    public boolean checkIfUserHasBlockedReceiver() {

        return !this.userBlockList.dataList.isEmpty()
                && this.userBlockList.dataList.stream().anyMatch(
                x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE)
                        && x.getUserid() == getSender());

    }

    public boolean checkIfUsersAreFriends() {

        return !this.userRelationship.dataList.isEmpty() &&
                this.userRelationship.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRelationship.STATUS_ACTIVE)
                                && Objects.equals(x.getType(), UserRelationship.TYPE_FRIEND));
    }

    public boolean checkIfUserIsFollowing() {

        return !this.userRelationship.dataList.isEmpty() &&
                this.userRelationship.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRelationship.STATUS_ACTIVE) &&
                                x.getSenderid() == getSender() &&
                                Objects.equals(x.getType(), UserRelationship.TYPE_FOLLOW));
    }

    public boolean checkIfUserHasPendingFriendRequest() {

        return !this.userRequest.dataList.isEmpty() &&
                this.userRequest.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE) &&
                                x.getSenderid() == getSender() &&
                                Objects.equals(x.getType(), UserRequest.TYPE_FRIEND));
    }

    public boolean checkifUserHasAPendingRequestFriendFromReceiver() {
        return !this.userRequest.dataList.isEmpty() &&
                this.userRequest.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE) &&
                                x.getSenderid() == getReceiver() &&
                                Objects.equals(x.getType(), UserRequest.TYPE_FRIEND));
    }

    public boolean checkIfUserHasPendingFollowRequest() {

        return !this.userRequest.dataList.isEmpty() &&
                this.userRequest.dataList.stream().anyMatch(
                        x -> Objects.equals(x.getStatus(), UserRequest.STATUS_ACTIVE) &&
                                x.getSenderid() == getSender() &&
                                Objects.equals(x.getType(), UserRelationship.TYPE_FOLLOW));
    }

}
