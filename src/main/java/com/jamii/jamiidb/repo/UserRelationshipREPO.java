package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRelationshipREPO extends CrudRepository<UserRelationshipTBL, Integer> {

    List<UserRelationshipTBL> findBySenderidAndReceiveridAndTypeAndStatus(UserLoginTBL sender, UserLoginTBL reciever, int type, int status);
}
