package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.jamiidb.controllers.UserBlockList;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.controllers.UserRelationship;
import com.jamii.jamiidb.controllers.UserRequest;
import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.requests.userServices.socialREQ.BlockUserRequestServicesREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BlockUserOPS extends AbstractUserServicesOPS {

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserRelationship userRelationship;
    @Autowired
    private UserRequest userRequest;
    @Autowired
    private UserBlockList userBlockList;

    @Override
    public void validateCookie() throws Exception {
        BlockUserRequestServicesREQ req = (BlockUserRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), BlockUserRequestServicesREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
        super.validateCookie();
    }

    @Override
    public void processRequest() throws Exception {

        if (!getIsSuccessful()) {
            return;
        }

        BlockUserRequestServicesREQ req = (BlockUserRequestServicesREQ) JamiiMapperUtils.mapObject(getRequest(), BlockUserRequestServicesREQ.class);

        // Check if both users exist in the system
        this.userLogin.data = new UserLoginTBL( );
        this.userLogin.otherUser = new UserLoginTBL( );

        this.userLogin.data = this.userLogin.fetchByUserKey( UserKey, UserLogin.ACTIVE_ON ).orElse( null );
        this.userLogin.otherUser = this.userLogin.fetchByUserKey( req.getTargetUserKey( ), UserLogin.ACTIVE_ON ).orElse( null );
        if( this.userLogin.data == null  || this.userLogin.otherUser == null ){
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage( );
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP( ) ;
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
        } else {
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
    public CompletableFuture<Void> fetchRequests() {
        this.userRequest.dataList = new ArrayList<>();
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
    public CompletableFuture<Void> fetchBlockList() {
        this.userBlockList.dataList = new ArrayList<>( );
        this.userBlockList.dataList.addAll( userBlockList.fetch(this.userLogin.data, this.userLogin.otherUser, UserBlockList.STATUS_ACTIVE) );
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> fetchRelationships() {
        this.userRelationship.dataList = new ArrayList<>();
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
