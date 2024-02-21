package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDataREPO extends CrudRepository<UserDataTBL, Integer> {

    List< UserDataTBL > findByUserloginidAndCurrent( UserLoginTBL userLoginTBL, boolean current );

    @Query( value = "SELECT * FROM user_data WHERE ( first_name like '?%' OR middle_name like '?%'   OR last_name like '?%' ) AND current = ?4 ORDER BY ID DESC LIMIT 30", nativeQuery = true)
    List<UserDataTBL> searchUser( String firstname, String middlename, String lastname,  boolean current );
}
