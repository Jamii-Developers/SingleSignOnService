package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.FileDirectoryTBL;
import com.jamii.databaseconfig.model.FileTableOwnerTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileDirectoryREPO
        extends CrudRepository<FileDirectoryTBL, Integer>
{

    List<FileDirectoryTBL> findByUserloginidAndFiletableownerid(UserLoginTBL userLoginTBL, FileTableOwnerTBL fileTableOwnerTBL);
}
