package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.FileTableOwnerTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileTableOwnerREPO
        extends CrudRepository<FileTableOwnerTBL, Integer>
{

    List<FileTableOwnerTBL> findByUserloginidAndSystemfilenameAndStatus(UserLoginTBL userLoginTBL, String systemfilename, Integer Status);

    List<FileTableOwnerTBL> findByUserloginidAndSystemfilename(UserLoginTBL userLoginTBL, String systemfilename);
}
