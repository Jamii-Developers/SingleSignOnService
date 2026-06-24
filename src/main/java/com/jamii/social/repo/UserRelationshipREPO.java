package com.jamii.social.repo;

import com.jamii.users.model.UserLoginTBL;
import com.jamii.social.model.UserRelationshipTBL;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for {@link UserRelationshipTBL} entities.
 * 
 * <p>This repository provides CRUD operations and custom queries for user relationship records.</p>
 */
public interface UserRelationshipREPO
        extends CrudRepository<UserRelationshipTBL, Integer>
{

    /**
     * Finds relationship records by sender, receiver, type, and status.
     * @param sender the user who initiated the relationship
     * @param reciever the user who received the relationship
     * @param type the relationship type to match
     * @param status the status to match
     * @return list of matching relationship records
     */
    List<UserRelationshipTBL> findBySenderidAndReceiveridAndTypeAndStatus(UserLoginTBL sender, UserLoginTBL reciever, int type, int status);

    /**
     * Finds relationship records by sender, receiver, and type.
     * @param sender the user who initiated the relationship
     * @param reciever the user who received the relationship
     * @param type the relationship type to match
     * @return list of matching relationship records
     */
    List<UserRelationshipTBL> findBySenderidAndReceiveridAndType(UserLoginTBL sender, UserLoginTBL reciever, int type);

    /**
     * Finds relationship records by sender and receiver.
     * @param sender the user who initiated the relationship
     * @param receiver the user who received the relationship
     * @return list of matching relationship records
     */
    List<UserRelationshipTBL> findBySenderidAndReceiverid(UserLoginTBL sender, UserLoginTBL receiver);

    /**
     * Finds relationship records by receiver, type, and status.
     * @param sender the user to search for (as receiver)
     * @param type the relationship type to match
     * @param status the status to match
     * @return list of matching relationship records
     */
    List<UserRelationshipTBL> findByReceiveridAndTypeAndStatus(UserLoginTBL sender, int type, int status);

    /**
     * Finds relationship records where user is either sender or receiver, with matching status and type.
     * @param senderId the user ID to match as sender
     * @param receiverId the user ID to match as receiver
     * @param status the status to match
     * @param type the relationship type to match
     * @return list of matching relationship records
     */
    @Query("SELECT u FROM UserRelationshipTBL u WHERE (u.senderid = :senderId OR u.receiverid = :receiverId) AND u.status = :status AND u.type = :type")
    List<UserRelationshipTBL> findBySenderOrReceiverAndStatusAndType(@Param("senderId") UserLoginTBL senderId, @Param("receiverId") UserLoginTBL receiverId, @Param("status") int status, @Param("type") int type);
}
