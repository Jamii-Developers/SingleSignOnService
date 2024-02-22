package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.UserDataTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDataREPO extends CrudRepository<UserDataTBL, Integer> {

    List< UserDataTBL > findByUserloginidAndCurrent( UserLoginTBL userLoginTBL, boolean current );
    List< UserDataTBL > findByFirstnameStartingWithAndCurrent( String searchString, Boolean currentStatusOn);
    List< UserDataTBL > findByLastnameStartingWithAndCurrent( String searchString, Boolean currentStatusOn);
    List< UserDataTBL > findByMiddlenameStartingWithAndCurrent( String searchString, Boolean currentStatusOn);
}
