package com.jamii.jDrive.repo;

import com.jamii.jDrive.model.FileTableOwnerTBL;
import com.jamii.users.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link FileTableOwnerTBL} entities.
 * 
 * <p>This repository provides CRUD operations and custom queries for file ownership records.</p>
 */
public interface FileTableOwnerREPO
        extends CrudRepository<FileTableOwnerTBL, Integer>
{

    /**
     * Finds file ownership records by user, system filename, and status.
     * @param userLoginTBL the user to search for
     * @param systemfilename the system filename to match
     * @param Status the status to match
     * @return list of matching file ownership records
     */
    List<FileTableOwnerTBL> findByUserloginidAndSystemfilenameAndStatus(UserLoginTBL userLoginTBL, String systemfilename, Integer Status);

    /**
     * Finds file ownership records by user and system filename.
     * @param userLoginTBL the user to search for
     * @param systemfilename the system filename to match
     * @return list of matching file ownership records
     */
    List<FileTableOwnerTBL> findByUserloginidAndSystemfilename(UserLoginTBL userLoginTBL, String systemfilename);
}
