package com.jamii.operations.userServices.social;

import com.jamii.Utils.JamiiLoggingUtils;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SearchUsersOPS
        extends AbstractUserServicesOPS
{
    private Map<String, SearchResultsHelper.SearchResults> searchResults = new ConcurrentHashMap<>();

    @Autowired JamiiLoggingUtils jamiiLoggingUtils;
    @Autowired private UserLogin userLogin;
    @Autowired private UserData userData;
    @Autowired private JamiiRelationshipUtils jamiiRelationshipUtils;

    @Override
    public void validateCookie()
            throws Exception
    {
        SearchUserServicesREQ req = (SearchUserServicesREQ) JamiiMapperUtils.mapObject(getRequest(), SearchUserServicesREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
        super.validateCookie();
    }

    @Override
    public void processRequest()
            throws Exception
    {
        try {
            if (!getIsSuccessful()) {
                return;
            }

            this.searchResults = new ConcurrentHashMap<>();
            this.userData.dataList = new ArrayList<>();
            this.userLogin.data = new UserLoginTBL();

            SearchUserServicesREQ req = (SearchUserServicesREQ) JamiiMapperUtils.mapObject(getRequest(), SearchUserServicesREQ.class);

            // Fetch user information
            this.userLogin.data = this.userLogin.fetchByUserKey(req.getUserKey(), UserLogin.ACTIVE_ON).orElse(null);
            if ( this.userLogin.data.getId() == null ) {
                this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
                this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
                this.isSuccessful = false;
                return;
            }

            // Run searches in parallel
            CompletableFuture<Void> emailUsernameSearch = searchUsingEmailAndUsername(req);
            CompletableFuture<Void> namesSearch = searchUsingNames(req);

            // Wait for both to complete
            CompletableFuture.allOf(emailUsernameSearch, namesSearch).join();
        }
        catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger(this.getClass().getName(), e, this.userLogin.data );
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
        }
    }

    @Override
    public ResponseEntity<?> getResponse()
    {
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
    @Transactional(readOnly = true)
    public CompletableFuture<Void> searchUsingEmailAndUsername(SearchUserServicesREQ req)
    {
        List<CompletableFuture<List<UserLoginTBL>>> loginFutures = Arrays.asList(CompletableFuture.supplyAsync(() -> this.userLogin.searchUserUsername(req.getSearchstring())), CompletableFuture.supplyAsync(() -> this.userLogin.searchUserEmailAddress(req.getSearchstring())));

        List<UserLoginTBL> allLoginUsers = loginFutures.stream().map(CompletableFuture::join).flatMap(List::stream).toList();

        allLoginUsers.parallelStream().forEach(user -> {
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
            obj.setUSERNAME(user.getUsername());
            obj.setUSER_KEY(user.getUserKey());
            if (userdata.isPresent()) {
                obj.setFIRSTNAME(userdata.get().getFirstname());
                obj.setLASTNAME(userdata.get().getLastname());
            }
            else {
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
    @Transactional(readOnly = true)
    public CompletableFuture<Void> searchUsingNames(SearchUserServicesREQ req)
    {
        List<CompletableFuture<List<UserDataTBL>>> futures = Arrays.asList(CompletableFuture.supplyAsync(() -> this.userData.searchUserFirstname(req.getSearchstring())), CompletableFuture.supplyAsync(() -> this.userData.searchUserMiddlename(req.getSearchstring())), CompletableFuture.supplyAsync(() -> this.userData.searchUserLastname(req.getSearchstring())));

        List<UserDataTBL> allUsers = futures.stream().map(CompletableFuture::join).flatMap(List::stream).toList();

        allUsers.parallelStream().forEach(userdata -> {
            Optional<UserLoginTBL> user = this.userLogin.fetchByUserKey(userdata.getUserloginid().getUserKey(), UserLogin.ACTIVE_ON);
            if (user.isEmpty() || Objects.equals(this.userLogin.data.getId(), user.get().getId())) {
                return;
            }

            jamiiRelationshipUtils.setSender(this.userLogin.data);
            jamiiRelationshipUtils.setReceiver(user.get());
            jamiiRelationshipUtils.initRelationShip();

            if (jamiiRelationshipUtils.checkIfUserIsBlocked() || jamiiRelationshipUtils.checkIfUserHasBlockedReceiver()) {
                return;
            }

            SearchResultsHelper.SearchResults obj = new SearchResultsHelper.SearchResults();
            obj.setUSERNAME(user.get().getUsername());
            obj.setUSER_KEY(user.get().getUserKey());
            obj.setFIRSTNAME(userdata.getFirstname());
            obj.setLASTNAME(userdata.getLastname());
            obj.setFriend(jamiiRelationshipUtils.checkIfUsersAreFriends());
            obj.setFollowing(jamiiRelationshipUtils.checkIfUserIsFollowing());
            obj.setHasPendingFriendRequest(jamiiRelationshipUtils.checkIfUserHasPendingFriendRequest());
            obj.setHasPendingFollowingRequest(jamiiRelationshipUtils.checkIfUserHasPendingFollowRequest());

            this.searchResults.putIfAbsent(user.get().getUserKey(), obj);
        });
        return CompletableFuture.completedFuture(null);
    }
}
