package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRelationshipREPO
        extends CrudRepository<UserRelationshipTBL, Integer>
{

    List<UserRelationshipTBL> findBySenderidAndReceiveridAndTypeAndStatus(UserLoginTBL sender, UserLoginTBL reciever, int type, int status);

    List<UserRelationshipTBL> findBySenderidAndReceiveridAndType(UserLoginTBL sender, UserLoginTBL reciever, int type);

    List<UserRelationshipTBL> findBySenderidAndReceiverid(UserLoginTBL sender, UserLoginTBL receiver);

    List<UserRelationshipTBL> findByReceiveridAndTypeAndStatus(UserLoginTBL sender, int type, int status);

    @Query("SELECT u FROM UserRelationshipTBL u WHERE (u.senderid = :senderId OR u.receiverid = :receiverId) AND u.status = :status AND u.type = :type")
    List<UserRelationshipTBL> findBySenderOrReceiverAndStatusAndType(@Param("senderId") UserLoginTBL senderId, @Param("receiverId") UserLoginTBL receiverId, @Param("status") int status, @Param("type") int type);
}
