package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiMapperUtils;
import com.jamii.Utils.JamiiRelationshipUtils;
import com.jamii.jamiidb.controllers.UserData;
import com.jamii.jamiidb.controllers.UserLogin;
import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.operations.userServices.AbstractUserServicesOPS;
import com.jamii.operations.userServices.social.Utils.SearchResultsHelper;
import com.jamii.requests.userServices.socialREQ.SearchUserServicesREQ;
import com.jamii.responses.userResponses.socialResponses.SearchUserRESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SearchUsersOPS extends AbstractUserServicesOPS {

    private final Map<String, SearchResultsHelper.SearchResults> searchResults = new ConcurrentHashMap<>();

    @Autowired
    private UserLogin userLogin;
    @Autowired
    private UserData userData;
    @Autowired
    private JamiiRelationshipUtils jamiiRelationshipUtils;

    @Override
    public void validateCookie() throws Exception {
        SearchUserServicesREQ req = (SearchUserServicesREQ) JamiiMapperUtils.mapObject(getRequest(), SearchUserServicesREQ.class);
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

        SearchUserServicesREQ req = (SearchUserServicesREQ) JamiiMapperUtils.mapObject(getRequest(), SearchUserServicesREQ.class);

        // Run searches in parallel
        CompletableFuture<Void> emailUsernameSearch = searchUsingEmailAndUsername(req);
        CompletableFuture<Void> namesSearch = searchUsingNames(req);

        // Wait for both to complete
        CompletableFuture.allOf(emailUsernameSearch, namesSearch).join();

        setIsSuccessful(true);
    }

    @Override
    public ResponseEntity<?> getResponse() {
        if (getIsSuccessful()) {
            SearchUserRESP resp = new SearchUserRESP();
            for (Map.Entry<String, SearchResultsHelper.SearchResults> entry : this.searchResults.entrySet()) {
                SearchUserRESP.Results res = new SearchUserRESP.Results();
                res.setUsername(entry.getValue().getUSERNAME());
                res.setUserKey(entry.getValue().getUSER_KEY());
                res.setFirstname(entry.getValue().getFIRSTNAME());
                res.setLastname(entry.getValue().getLASTNAME());
                res.setFriend(entry.getValue().isFriend());
                res.setFollowing(entry.getValue().isFollowing());
                res.setHasPendingFriendRequest(entry.getValue().isHasPendingFriendRequest());
                res.setHasPendingFollowingRequest(entry.getValue().isHasPendingFollowingRequest());
                resp.getResults().add(res);
            }
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        return super.getResponse();
    }

    @Async
    public CompletableFuture<Void> searchUsingEmailAndUsername(SearchUserServicesREQ req) {
        this.userLogin.dataList = new ArrayList<>();
        List<CompletableFuture<List<UserLoginTBL>>> loginFutures = Arrays.asList(
                CompletableFuture.supplyAsync(() -> this.userLogin.searchUserUsername(req.getSearchstring())),
                CompletableFuture.supplyAsync(() -> this.userLogin.searchUserEmailAddress(req.getSearchstring()))
        );

        // Wait for all futures to complete and aggregate the results
        List<UserLoginTBL> allLoginUsers = loginFutures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .toList();

        this.userLogin.dataList.addAll(allLoginUsers);

        this.userLogin.dataList.parallelStream().forEach(user -> {
            if (Objects.equals(this.userLogin.data.getId(), user.getId())) {
                return;
            }

            Optional<UserDataTBL> userdata = this.userData.fetch(user, UserData.CURRENT_STATUS_ON);
            jamiiRelationshipUtils.setSender(this.userLogin.data);
            jamiiRelationshipUtils.setReceiver(user);
            jamiiRelationshipUtils.initRelationShip();

            if (jamiiRelationshipUtils.checkIfUserIsBlocked() || jamiiRelationshipUtils.checkIfUserHasBlockedReceiver()) {
                return;
            }

            SearchResultsHelper.SearchResults obj = new SearchResultsHelper.SearchResults();
            if (userdata.isPresent()) {
                obj.setUSERNAME(user.getUsername());
                obj.setUSER_KEY(user.getUserKey());
                obj.setFIRSTNAME(userdata.get().getFirstname());
                obj.setLASTNAME(userdata.get().getLastname());
            } else {
                obj.setUSERNAME(user.getUsername());
                obj.setUSER_KEY(user.getUserKey());
                obj.setFIRSTNAME("N/A");
                obj.setLASTNAME("N/A");
            }

            obj.setFriend(jamiiRelationshipUtils.checkIfUsersAreFriends());
            obj.setFollowing(jamiiRelationshipUtils.checkIfUserIsFollowing());
            obj.setHasPendingFriendRequest(jamiiRelationshipUtils.checkIfUserHasPendingFriendRequest());
            obj.setHasPendingFollowingRequest(jamiiRelationshipUtils.checkIfUserHasPendingFollowRequest());

            this.searchResults.putIfAbsent(user.getUserKey(), obj);
        });
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> searchUsingNames(SearchUserServicesREQ req) {
        List<CompletableFuture<List<UserDataTBL>>> futures = Arrays.asList(
                CompletableFuture.supplyAsync(() -> this.userData.searchUserFirstname(req.getSearchstring())),
                CompletableFuture.supplyAsync(() -> this.userData.searchUserMiddlename(req.getSearchstring())),
                CompletableFuture.supplyAsync(() -> this.userData.searchUserLastname(req.getSearchstring()))
        );

        // Wait for all futures to complete and aggregate the results
        List<UserDataTBL> allUsers = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .toList();

        allUsers.parallelStream().forEach(userdata -> {
            UserLoginTBL user = this.userLogin.fetchByUserKey(userdata.getUserloginid().getUserKey(), UserLogin.ACTIVE_ON).orElse(null);
            if (user == null || Objects.equals(this.userLogin.data.getId(), user.getId())) {
                return;
            }

            jamiiRelationshipUtils.setSender(this.userLogin.data);
            jamiiRelationshipUtils.setReceiver(user);
            jamiiRelationshipUtils.initRelationShip();

            if (jamiiRelationshipUtils.checkIfUserIsBlocked() || jamiiRelationshipUtils.checkIfUserHasBlockedReceiver()) {
                return;
            }

            SearchResultsHelper.SearchResults obj = new SearchResultsHelper.SearchResults();
            obj.setUSERNAME(user.getUsername());
            obj.setUSER_KEY(user.getUserKey());
            obj.setFIRSTNAME(userdata.getFirstname());
            obj.setLASTNAME(userdata.getLastname());
            obj.setFriend(jamiiRelationshipUtils.checkIfUsersAreFriends());
            obj.setFollowing(jamiiRelationshipUtils.checkIfUserIsFollowing());
            obj.setHasPendingFriendRequest(jamiiRelationshipUtils.checkIfUserHasPendingFriendRequest());
            obj.setHasPendingFollowingRequest(jamiiRelationshipUtils.checkIfUserHasPendingFollowRequest());

            this.searchResults.putIfAbsent(user.getUserKey(), obj);
        });
        return CompletableFuture.completedFuture(null);
    }
}
