package com.jamii.webapi.jamiidb.repo;

import com.jamii.webapi.jamiidb.model.UserDataTBL;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDataREPO extends CrudRepository<UserDataTBL, Integer> {

    @Query( value = "SELECT * FROM user_data WHERE USER_LOGIN_ID = ?1 and CURRENT = ?2", nativeQuery = true)
    List< UserDataTBL > findbyuserloginidandactive( int userloginID, Boolean active );
}
