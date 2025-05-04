package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRelationshipREPO extends CrudRepository<UserRelationshipTBL, Integer> {

    List<UserRelationshipTBL> findBySenderidAndReceiveridAndTypeAndStatus(UserLoginTBL sender, UserLoginTBL reciever, int type, int status);
    List<UserRelationshipTBL> findBySenderidAndReceiveridAndType(UserLoginTBL sender, UserLoginTBL reciever, int type);

    List<UserRelationshipTBL> findBySenderidAndReceiverid(UserLoginTBL sender, UserLoginTBL receiver);
    List<UserRelationshipTBL> findByReceiveridAndTypeAndStatus(UserLoginTBL sender, int type, int status);

    List<UserRelationshipTBL> findBySenderIdOrReceiverIdAndStatusAndType(
            int senderId,
            int receiverId,
            int status,
            int type
    );


}
