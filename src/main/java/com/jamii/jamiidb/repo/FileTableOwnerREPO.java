package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileTableOwnerREPO extends CrudRepository<FileTableOwnerTBL, Integer> {

    public List<FileTableOwnerTBL> findByUserloginidAndSystemfilenameAndStatus( UserLoginTBL userLoginTBL, String systemfilename , Integer Status );
}
