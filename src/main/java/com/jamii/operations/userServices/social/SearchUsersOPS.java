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
    @Autowired private JamiiRelationshipUtils rutils;

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
        List<UserLoginTBL> allUsers = List.of(
                        userLogin.searchUserUsername(req.getSearchstring()),
                        userLogin.searchUserEmailAddress(req.getSearchstring())
                ).stream().flatMap(List::stream).toList();

        for( UserLoginTBL user : allUsers ){

            if( Objects.equals(this.userLogin.data.getId(), user.getId())){
                continue;
            }

            rutils.setSender(this.userLogin.data);
            rutils.setReceiver(user);
            rutils.initRelationShip();

            if (rutils.checkIfUserIsBlocked() || rutils.checkIfUserHasBlockedReceiver()) {
                continue;
            }

            SearchResultsHelper.SearchResults obj = new SearchResultsHelper.SearchResults();
            obj.setUSERNAME(user.getUsername());
            obj.setUSER_KEY(user.getUserKey());
            if ( user.getUserData() != null ) {
                obj.setFIRSTNAME(user.getUserData().getFirstname());
                obj.setLASTNAME(user.getUserData().getLastname());
            }
            else {
                obj.setFIRSTNAME("N/A");
                obj.setLASTNAME("N/A");
            }

            obj.setFriend(rutils.checkIfUsersAreFriends());
            obj.setFollowing(rutils.checkIfUserIsFollowing());
            obj.setHasPendingFriendRequest(rutils.checkIfUserHasPendingFriendRequest());
            obj.setHasPendingFollowingRequest(rutils.checkIfUserHasPendingFollowRequest());

            this.searchResults.putIfAbsent(user.getUserKey(), obj);
        };
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<Void> searchUsingNames(SearchUserServicesREQ req)
    {
        List<UserDataTBL> allUsers =
                List.of(
                        userData.searchUserFirstname(req.getSearchstring()),
                        userData.searchUserMiddlename(req.getSearchstring()),
                        userData.searchUserLastname(req.getSearchstring())
                ).stream().flatMap(List::stream).toList();

        for( UserDataTBL userdata : allUsers ){

            Optional<UserLoginTBL> user = Optional.ofNullable( userdata.getUserloginid() );
            if (user.isEmpty() || Objects.equals(this.userLogin.data.getId(), user.get().getId())) {
                continue;
            }

            rutils.setSender(this.userLogin.data);
            rutils.setReceiver(user.get());
            rutils.initRelationShip();

            if (rutils.checkIfUserIsBlocked() || rutils.checkIfUserHasBlockedReceiver()) {
                continue;
            }

            SearchResultsHelper.SearchResults obj = new SearchResultsHelper.SearchResults();
            obj.setUSERNAME(user.get().getUsername());
            obj.setUSER_KEY(user.get().getUserKey());
            obj.setFIRSTNAME(userdata.getFirstname());
            obj.setLASTNAME(userdata.getLastname());
            obj.setFriend(rutils.checkIfUsersAreFriends());
            obj.setFollowing(rutils.checkIfUserIsFollowing());
            obj.setHasPendingFriendRequest(rutils.checkIfUserHasPendingFriendRequest());
            obj.setHasPendingFollowingRequest(rutils.checkIfUserHasPendingFollowRequest());

            this.searchResults.putIfAbsent(user.get().getUserKey(), obj);
        };
        return CompletableFuture.completedFuture(null);
    }
}
