package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRelationshipREPO extends CrudRepository<UserRelationshipTBL, Integer> {

    List<UserRelationshipTBL> findBySenderidAndReceiveridAndTypeAndStatus(UserLoginTBL sender, UserLoginTBL reciever, int type, int status);
    List<UserRelationshipTBL> findBySenderidAndReceiveridAndType(UserLoginTBL sender, UserLoginTBL reciever, int type);

    List<UserRelationshipTBL> findBySenderidAndReceiverid(UserLoginTBL sender, UserLoginTBL receiver);
    List<UserRelationshipTBL> findByReceiveridAndTypeAndStatus(UserLoginTBL sender, int type, int status);

    @Query( value = "SELECT * FROM jamiidb.user_relationship WHERE (sender_id = ? OR receiver_id = ?) AND status = ? AND type = ?", nativeQuery = true)
    List<UserRelationshipTBL> findBySenderidOrReceiverid( int senderid, int recieverid,int type, int status);


}
