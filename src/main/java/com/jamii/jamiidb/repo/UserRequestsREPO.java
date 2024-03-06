package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRequestsTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRequestsREPO extends CrudRepository<UserRequestsTBL, Integer> {

    List< UserRequestsTBL > findBySenderidAndRecevierid(UserLoginTBL user1, UserLoginTBL user2);
    List<UserRequestsTBL> findBySenderidAndRecevieridAndRequestype(UserLoginTBL user1, UserLoginTBL user2, Integer requestType);
}
