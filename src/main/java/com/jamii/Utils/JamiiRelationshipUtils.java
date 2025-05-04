package com.jamii.Utils;

import com.jamii.jamiidb.controllers.UserBlockList;
import com.jamii.jamiidb.controllers.UserRelationship;
import com.jamii.jamiidb.controllers.UserRequest;
import com.jamii.jamiidb.model.UserLoginTBL;
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

    public void initRelationShip(  ){

        // Initial Available BlockList
        this.userBlockList.dataList = new ArrayList< >( );
        this.userBlockList.dataList.addAll( userBlockList.fetch( sender, receiver ) );
        this.userBlockList.dataList.addAll( userBlockList.fetch( receiver, sender ) );

        // Initialize Available Requests
        this.userRequest.dataList = new ArrayList< >( );
        this.userRequest.dataList.addAll( userRequest.fetch( sender, receiver ) );
        this.userRequest.dataList.addAll( userRequest.fetch( receiver, sender ) );

        // Initialize Available Relationships
        //Fetch Relationships
        this.userRelationship.dataList = new ArrayList< >( );
        this.userRelationship.dataList.addAll( userRelationship.fetch( sender, receiver ) );
        this.userRelationship.dataList.addAll( userRelationship.fetch( receiver, sender ) );

    }

    public boolean checkIfUserIsBlocked( ) {

        if ( !this.userBlockList.dataList.isEmpty()
                && this.userBlockList.dataList.stream( ).anyMatch(
                x -> Objects.equals(
                        x.getStatus(), UserRequest.STATUS_ACTIVE )
                        && x.getUserid( ) == getReceiver( ) ) )
        {
            return true;
        }

        return false;
    }

    public boolean checkIfUserHasBlockedReceiver(  ){

        if ( !this.userBlockList.dataList.isEmpty()
                && this.userBlockList.dataList.stream( ).anyMatch(
                        x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE )
                                && x.getUserid( ) == getSender( ) ) ){
            return true;
        }

        return false;

    }

    public boolean checkIfUsersAreFriends( ) {

        if( !this.userRelationship.dataList.isEmpty() &&
                this.userRelationship.dataList.stream().anyMatch(
                        x -> Objects.equals( x.getStatus(), UserRelationship.STATUS_ACTIVE )
                                && Objects.equals( x.getType(), UserRelationship.TYPE_FRIEND) ) ){
            return true;
        }


        return false;
    }

    public boolean checkIfUserIsFollowing(  ){

        if( !this.userRelationship.dataList.isEmpty() &&
                this.userRelationship.dataList.stream().anyMatch(
                        x -> Objects.equals( x.getStatus(), UserRelationship.STATUS_ACTIVE ) &&
                                x.getSenderid( ) == getSender( ) &&
                                Objects.equals(x.getType(), UserRelationship.TYPE_FOLLOW) ) ){
            return true;
        }

        return false;
    }

    public boolean checkIfUserHasPendingFriendRequest( ) {

        if( !this.userRequest.dataList.isEmpty() &&
                this.userRequest.dataList.stream().anyMatch(
                        x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE ) &&
                                x.getSenderid( ) == getSender( ) &&
                                Objects.equals(x.getType(), UserRequest.TYPE_FRIEND ) ) ){
            return true;
        }

        return false;
    }

    public boolean checkifUserHasAPendingRequestFriendFromReceiver( ){
        if( !this.userRequest.dataList.isEmpty() &&
                this.userRequest.dataList.stream().anyMatch(
                        x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE ) &&
                                x.getSenderid( ) == getReceiver( ) &&
                                Objects.equals(x.getType(), UserRequest.TYPE_FRIEND ) ) ){
            return true;
        }

        return false;
    }

    public boolean checkIfUserHasPendingFollowRequest( ) {

        if( !this.userRequest.dataList.isEmpty() &&
                this.userRequest.dataList.stream().anyMatch(
                        x -> Objects.equals( x.getStatus(), UserRequest.STATUS_ACTIVE ) &&
                                x.getSenderid( ) == getSender( ) &&
                                Objects.equals(x.getType(), UserRelationship.TYPE_FOLLOW) ) ){
            return true;
        }

        return false;
    }

}
