package com.jamii.jDrive.repo;

import com.jamii.jDrive.model.FileDirectoryTBL;
import com.jamii.jDrive.model.FileTableOwnerTBL;
import com.jamii.jUser.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link FileDirectoryTBL} entities.
 * 
 * <p>This repository provides CRUD operations and custom queries for file directory records.</p>
 */
public interface FileDirectoryREPO
        extends CrudRepository<FileDirectoryTBL, Integer>
{

    /**
     * Finds file directory records by user and file owner.
     * @param userLoginTBL the user to search for
     * @param fileTableOwnerTBL the file owner to match
     * @return list of matching file directory records
     */
    List<FileDirectoryTBL> findByUserloginidAndFiletableownerid(UserLoginTBL userLoginTBL, FileTableOwnerTBL fileTableOwnerTBL);
}
