package com.jamii.jSocial.services.relationshipManagement;

import com.jamii.utils.JamiiMapperUtils;
import com.jamii.jSocial.peer.UserBlockList;
import com.jamii.jUser.peer.UserLogin;
import com.jamii.jSocial.peer.UserRelationship;
import com.jamii.jSocial.peer.UserRequest;
import com.jamii.jSocial.model.UserBlockListTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jSocial.model.UserRelationshipTBL;
import com.jamii.jSocial.model.UserRequestsTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.requests.BlockUserRequestServicesREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for blocking other users in the social platform.
 * 
 * <p>This service allows authenticated users to block other users, which:
 * <ul>
 *   <li>Deactivates all existing relationships (friend/follow) between the users</li>
 *   <li>Deactivates all pending requests between the users</li>
 *   <li>Creates or reactivates a block list entry</li>
 * </ul>
 * The operation requires valid session authentication.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Verify both users exist and are active</li>
 *   <li>Fetch relationships, requests, and block list concurrently</li>
 *   <li>Deactivate all relationships and requests</li>
 *   <li>Create or reactivate block list entry</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>Users not found or inactive</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see BlockUserRequestServicesREQ
 */
@Service
public class BlockUserOPS
        extends AbstractUserServicesOPS
{

    @Autowired private UserLogin userLogin;
    @Autowired private UserRelationship userRelationship;
    @Autowired private UserRequest userRequest;
    @Autowired private UserBlockList userBlockList;
    
    /** Request object containing user block request data */
    protected BlockUserRequestServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link BlockUserRequestServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new BlockUserRequestServicesREQ();
        req = (BlockUserRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), BlockUserRequestServicesREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
    }

    @Override
    public void processRequest()
            throws Exception
    {

        if (!getIsSuccessful()) {
            return;
        }

        // Request parameters are already mapped in setUserRequestData()

        // Check if both jUser exist in the system
        this.userLogin.data = new UserLoginTBL();
        this.userLogin.otherUser = new UserLoginTBL();

        this.userLogin.data = this.cookie.getValidatedUser();
        this.userLogin.otherUser = this.userLogin.fetchByUserKey(req.getTargetUserKey(), UserLogin.ACTIVE_ON).orElse(null);
        if (this.userLogin.data == null || this.userLogin.otherUser == null) {
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            this.isSuccessful = false;
            return;
        }

        // Fetch user data concurrently
        CompletableFuture<Void> fetchRequests = fetchRequests();
        CompletableFuture<Void> fetchBlockList = fetchBlockList();
        CompletableFuture<Void> fetchRelationships = fetchRelationships();

        // Wait for all data to be fetched
        CompletableFuture.allOf(fetchRequests, fetchBlockList, fetchRelationships).join();

        // Deactivate any active relationships
        this.userRelationship.dataList.forEach(rlshp -> {
            rlshp.setStatus(UserRelationship.STATUS_INACTIVE);
            rlshp.setDateupdated(LocalDateTime.now());
        });
        this.userRelationship.saveAll();

        // Deactivate any active requests
        this.userRequest.dataList.forEach(userReq -> {
            userReq.setStatus(UserRequest.STATUS_INACTIVE);
            userReq.setDateupdated(LocalDateTime.now());
        });
        this.userRequest.saveAll();

        // Set any blocked ID for the other User ID back to active
        if (!this.userBlockList.dataList.isEmpty()) {
            this.userBlockList.dataList.forEach(blcklst -> {
                blcklst.setStatus(UserBlockList.STATUS_ACTIVE);
                blcklst.setDateupdated(LocalDateTime.now());
            });
            this.userBlockList.saveAll();
        }
        else {
            // Create a new block entry if none exist
            this.userBlockList.data = new UserBlockListTBL();
            this.userBlockList.data.setUserid(this.userLogin.data);
            this.userBlockList.data.setBlockedid(this.userLogin.otherUser);
            this.userBlockList.data.setStatus(UserBlockList.STATUS_ACTIVE);
            this.userBlockList.data.setDateupdated(LocalDateTime.now());
            this.userBlockList.save();
        }
    }

    @Async
    public CompletableFuture<Void> fetchRequests()
    {
        this.userRequest.dataList = new ArrayList<>();
        // Optimize by combining all request types into a single batch operation
        List<CompletableFuture<List<UserRequestsTBL>>> futures = List.of(
            CompletableFuture.supplyAsync(() -> userRequest.fetch(this.userLogin.otherUser, this.userLogin.data, UserRequest.TYPE_FRIEND, UserRequest.STATUS_ACTIVE)),
            CompletableFuture.supplyAsync(() -> userRequest.fetch(this.userLogin.data, this.userLogin.otherUser, UserRequest.TYPE_FRIEND, UserRequest.STATUS_ACTIVE)),
            CompletableFuture.supplyAsync(() -> userRequest.fetch(this.userLogin.otherUser, this.userLogin.data, UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE)),
            CompletableFuture.supplyAsync(() -> userRequest.fetch(this.userLogin.data, this.userLogin.otherUser, UserRequest.TYPE_FOLLOW, UserRequest.STATUS_ACTIVE))
        );
        this.userRequest.dataList.addAll(futures.stream().map(CompletableFuture::join).flatMap(List::stream).toList());
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> fetchBlockList()
    {
        this.userBlockList.dataList = new ArrayList<>();
        // Check both directions for existing blocks to optimize later processing
        List<UserBlockListTBL> blocksFromUser = userBlockList.fetch(this.userLogin.data, this.userLogin.otherUser, UserBlockList.STATUS_ACTIVE);
        List<UserBlockListTBL> blocksFromOther = userBlockList.fetch(this.userLogin.otherUser, this.userLogin.data, UserBlockList.STATUS_ACTIVE);
        this.userBlockList.dataList.addAll(blocksFromUser);
        this.userBlockList.dataList.addAll(blocksFromOther);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> fetchRelationships()
    {
        this.userRelationship.dataList = new ArrayList<>();
        // Optimize by combining all relationship types into a single batch operation
        List<CompletableFuture<List<UserRelationshipTBL>>> futures = List.of(
            CompletableFuture.supplyAsync(() -> userRelationship.fetch(this.userLogin.data, this.userLogin.otherUser, UserRelationship.TYPE_FRIEND, UserRelationship.STATUS_ACTIVE)),
            CompletableFuture.supplyAsync(() -> userRelationship.fetch(this.userLogin.otherUser, this.userLogin.data, UserRelationship.TYPE_FRIEND, UserRelationship.STATUS_ACTIVE)),
            CompletableFuture.supplyAsync(() -> userRelationship.fetch(this.userLogin.data, this.userLogin.otherUser, UserRelationship.TYPE_FOLLOW, UserRelationship.STATUS_ACTIVE)),
            CompletableFuture.supplyAsync(() -> userRelationship.fetch(this.userLogin.otherUser, this.userLogin.data, UserRelationship.TYPE_FOLLOW, UserRelationship.STATUS_ACTIVE))
        );
        this.userRelationship.dataList.addAll(futures.stream().map(CompletableFuture::join).flatMap(List::stream).toList());
        return CompletableFuture.completedFuture(null);
    }
}
