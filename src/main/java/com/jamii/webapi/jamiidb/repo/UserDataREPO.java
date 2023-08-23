package com.jamii.webapi.jamiidb.repo;

import com.jamii.webapi.jamiidb.model.UserDataTBL;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

public interface UserDataREPO extends CrudRepository<UserDataTBL, Integer> {
}
