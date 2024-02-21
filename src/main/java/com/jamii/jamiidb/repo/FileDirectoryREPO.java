package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.FileDirectoryTBL;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileDirectoryREPO extends CrudRepository<FileDirectoryTBL, Integer> {

    public List< FileDirectoryTBL > findByUserloginidAndFiletableownerid(UserLoginTBL userLoginTBL, FileTableOwnerTBL fileTableOwnerTBL );
}
