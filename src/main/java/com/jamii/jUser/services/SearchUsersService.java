package com.jamii.jUser.services;

import com.jamii.utils.JamiiLoggingUtils;
import com.jamii.utils.JamiiMapperUtils;
import com.jamii.utils.JamiiRelationshipUtils;
import com.jamii.jUser.peer.UserData;
import com.jamii.jUser.peer.UserLogin;
import com.jamii.jUser.model.UserDataTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.abstractClasses.AbstractUserServicesOPS;
import com.jamii.jSocial.services.utils.SearchResultsHelper;
import com.jamii.jSocial.requests.SearchUserServicesREQ;
import com.jamii.jSocial.responses.SearchUserRESP;
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

/**
 * Service for searching users in the Jamii social platform.
 * 
 * <p>This service allows authenticated users to search for other users using multiple criteria:
 * <ul>
 *   <li>Email address</li>
 *   <li>Username</li>
 *   <li>First name</li>
 *   <li>Middle name</li>
 *   <li>Last name</li>
 * </ul>
 * 
 * <p>The search is performed in parallel across multiple data sources for optimal performance.
 * Results include user information along with relationship status (friend, following, pending requests).
 * Blocked users are automatically excluded from search results.
 * 
 * <p>Operation flow:
 * <ol>
 *   <li>Extract authentication keys in {@link #setUserRequestData()}</li>
 *   <li>Validate session cookie via parent class</li>
 *   <li>Fetch and validate the searching user</li>
 *   <li>Execute parallel searches by email/username and by name fields</li>
 *   <li>Filter results to exclude blocked users and self</li>
 *   <li>Enrich results with relationship status</li>
 *   <li>Return consolidated search results</li>
 * </ol>
 * 
 * <p>Error conditions:
 * <ul>
 *   <li>Invalid or expired session</li>
 *   <li>Searching user not found or inactive</li>
 *   <li>Database errors during search</li>
 * </ul>
 * 
 * @see AbstractUserServicesOPS
 * @see SearchUserServicesREQ
 * @see SearchUserRESP
 */
@Service
public class SearchUsersService
        extends AbstractUserServicesOPS
{
    /** Thread-safe map to store search results keyed by user key */
    private Map<String, SearchResultsHelper.SearchResults> searchResults = new ConcurrentHashMap<>();

    /** Logging utility for exception tracking */
    @Autowired JamiiLoggingUtils jamiiLoggingUtils;
    
    /** Repository for user login operations */
    @Autowired private UserLogin userLogin;
    
    /** Repository for user profile data operations */
    @Autowired private UserData userData;
    
    /** Utility for managing user relationships */
    @Autowired private JamiiRelationshipUtils rutils;
    
    /** Request object containing search parameters */
    protected SearchUserServicesREQ req = null;

    /**
     * Maps the incoming request to a {@link SearchUserServicesREQ} and extracts the
     * authentication keys required for session validation.
     */
    @Override
    protected void setUserRequestData()
    {
        req = new SearchUserServicesREQ();
        req = (SearchUserServicesREQ) JamiiMapperUtils.mapObject(getRequest(), SearchUserServicesREQ.class);
        setDeviceKey(req.getDeviceKey());
        setUserKey(req.getUserKey());
        setSessionKey(req.getSessionKey());
    }

    /**
     * Processes the user search request.
     * 
     * <p>This method performs the following operations:
     * <ul>
     *   <li>Validates the searching user exists and is active</li>
     *   <li>Initializes search results and data structures</li>
     *   <li>Executes parallel searches by email/username and by name fields</li>
     *   <li>Waits for both search operations to complete</li>
     *   <li>Handles any exceptions during the search process</li>
     * </ul>
     * 
     * <p>The search is performed asynchronously using {@link CompletableFuture}
     * to enable parallel execution of multiple search strategies.
     * 
     * @throws Exception if an error occurs during processing or user is not found
     */
    @Override
    public void processRequest()
            throws Exception
    {
        try {
            if (!getIsSuccessful()) {
                return;
            }

            // Initialize search state
            this.searchResults = new ConcurrentHashMap<>();
            this.userData.dataList = new ArrayList<>();

            // Request parameters are already mapped in setUserRequestData()

            // Reuse validated user from cookie validation to avoid redundant database call
            this.userLogin.data = this.cookie.getValidatedUser();
            if (this.userLogin.data == null || this.userLogin.data.getId() == null) {
                this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
                this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
                setIsSuccessful(false);
                return;
            }

            // Run searches in parallel for optimal performance
            CompletableFuture<Void> emailUsernameSearch = searchUsingEmailAndUsername(req);
            CompletableFuture<Void> namesSearch = searchUsingNames(req);

            // Wait for both search operations to complete
            CompletableFuture.allOf(emailUsernameSearch, namesSearch).join();
        }
        catch (Exception e) {
            jamiiLoggingUtils.ExceptionLogger(this.getClass().getName(), e, this.userLogin.data );
            this.jamiiErrorsMessagesRESP.setGenericErrorMessage();
            this.JamiiError = jamiiErrorsMessagesRESP.getJSONRESP();
            setIsSuccessful(false);
        }
    }

    /**
     * Generates the HTTP response for the search operation.
     * 
     * <p>If the search was successful, returns a response containing all matching users
     * with their relationship status. If validation failed, returns an error response.
     * 
     * <p>Each result includes:
     * <ul>
     *   <li>Username and user key</li>
     *   <li>First and last name</li>
     *   <li>Friend status</li>
     *   <li>Following status</li>
     *   <li>Pending friend/follow request status</li>
     * </ul>
     * 
     * @return ResponseEntity containing the search results or error message
     */
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

    /**
     * Searches for users by email address and username.
     * 
     * <p>This async method performs parallel searches:
     * <ul>
     *   <li>Search by username matching the search string</li>
     *   <li>Search by email address matching the search string</li>
     * </ul>
     * 
     * <p>Results are filtered to:
     * <ul>
     *   <li>Exclude the searching user themselves</li>
     *   <li>Exclude blocked users (both directions)</li>
     *   <li>Include relationship status (friend, following, pending requests)</li>
     * </ul>
     * 
     * @param req the search request containing the search string
     * @return CompletableFuture that completes when the search finishes
     */
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<Void> searchUsingEmailAndUsername(SearchUserServicesREQ req)
    {
        // Execute both searches and combine results
        List<UserLoginTBL> allUsers = List.of(
                        userLogin.searchUserUsername(req.getSearchstring()),
                        userLogin.searchUserEmailAddress(req.getSearchstring())
                ).stream().flatMap(List::stream).toList();

        for( UserLoginTBL user : allUsers ){

            // Skip the searching user
            if( Objects.equals(this.userLogin.data.getId(), user.getId())){
                continue;
            }

            // Initialize relationship context
            rutils.setSender(this.userLogin.data);
            rutils.setReceiver(user);
            rutils.initRelationShip();

            // Skip blocked users
            if (rutils.checkIfUserIsBlocked() || rutils.checkIfUserHasBlockedReceiver()) {
                continue;
            }

            // Build search result with user info and relationship status
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

            // Add to results (only if not already present)
            this.searchResults.putIfAbsent(user.getUserKey(), obj);
        };
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Searches for users by name fields (first, middle, last name).
     * 
     * <p>This async method performs parallel searches:
     * <ul>
     *   <li>Search by first name matching the search string</li>
     *   <li>Search by middle name matching the search string</li>
     *   <li>Search by last name matching the search string</li>
     * </ul>
     * 
     * <p>Results are filtered to:
     * <ul>
     *   <li>Exclude the searching user themselves</li>
     *   <li>Exclude blocked users (both directions)</li>
     *   <li>Include relationship status (friend, following, pending requests)</li>
     * </ul>
     * 
     * @param req the search request containing the search string
     * @return CompletableFuture that completes when the search finishes
     */
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<Void> searchUsingNames(SearchUserServicesREQ req)
    {
        // Execute all three name searches and combine results
        List<UserDataTBL> allUsers =
                List.of(
                        userData.searchUserFirstname(req.getSearchstring()),
                        userData.searchUserMiddlename(req.getSearchstring()),
                        userData.searchUserLastname(req.getSearchstring())
                ).stream().flatMap(List::stream).toList();

        for( UserDataTBL userdata : allUsers ){

            // Get the associated user login record
            Optional<UserLoginTBL> user = Optional.ofNullable( userdata.getUserloginid() );
            if (user.isEmpty() || Objects.equals(this.userLogin.data.getId(), user.get().getId())) {
                continue;
            }

            // Initialize relationship context
            rutils.setSender(this.userLogin.data);
            rutils.setReceiver(user.get());
            rutils.initRelationShip();

            // Skip blocked users
            if (rutils.checkIfUserIsBlocked() || rutils.checkIfUserHasBlockedReceiver()) {
                continue;
            }

            // Build search result with user info and relationship status
            SearchResultsHelper.SearchResults obj = new SearchResultsHelper.SearchResults();
            obj.setUSERNAME(user.get().getUsername());
            obj.setUSER_KEY(user.get().getUserKey());
            obj.setFIRSTNAME(userdata.getFirstname());
            obj.setLASTNAME(userdata.getLastname());
            obj.setFriend(rutils.checkIfUsersAreFriends());
            obj.setFollowing(rutils.checkIfUserIsFollowing());
            obj.setHasPendingFriendRequest(rutils.checkIfUserHasPendingFriendRequest());
            obj.setHasPendingFollowingRequest(rutils.checkIfUserHasPendingFollowRequest());

            // Add to results (only if not already present)
            this.searchResults.putIfAbsent(user.get().getUserKey(), obj);
        };
        return CompletableFuture.completedFuture(null);
    }
}
