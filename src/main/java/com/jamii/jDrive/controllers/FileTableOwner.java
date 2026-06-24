package com.jamii.jDrive.controllers;

import com.jamii.jDrive.model.FileTableOwnerTBL;
import com.jamii.users.model.UserLoginTBL;
import com.jamii.jDrive.repo.FileTableOwnerREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Service component for managing file table owner records in the database.
 * 
 * <p>This class handles file ownership tracking, allowing files to be associated
 * with users and managed through different lifecycle states (store, trash, deleted).</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Add file ownership records</li>
 *     <li>Fetch file ownership by user and filename</li>
 *     <li>Check file status</li>
 *     <li>Save file ownership records individually or in batch</li>
 * </ul>
 */
@Component
public class FileTableOwner
{

    /**
     * File status constant for files in storage.
     */
    public final static Integer ACTIVE_STATUS_STORE = 1;
    
    /**
     * File status constant for files in trash.
     */
    public final static Integer ACTIVE_STATUS_IN_TRASH = 2;
    
    /**
     * File status constant for deleted files.
     */
    public final static Integer ACTIVE_STATUS_DELETED = 3;

    /**
     * Single file table owner record instance for creating new entries.
     */
    public FileTableOwnerTBL data;
    
    /**
     * List of file table owner records for batch operations.
     */
    public ArrayList<FileTableOwnerTBL> dataList;
    @Autowired private FileTableOwnerREPO fileTableOwnerREPO;

    /**
     * Adds a file table owner record to the database.
     * 
     * @param fileTableOwnerTBL the file table owner record to add
     * @return the saved file table owner record with generated ID
     */
    public FileTableOwnerTBL add(FileTableOwnerTBL fileTableOwnerTBL)
    {
        return this.fileTableOwnerREPO.save(fileTableOwnerTBL);
    }

    /**
     * Fetches file table owner information for a specific user and filename.
     * 
     * @param user the user to fetch file ownership for
     * @param filename the system filename to fetch
     * @return an Optional containing the file table owner information if found, empty otherwise
     */
    public Optional<FileTableOwnerTBL> fetch(UserLoginTBL user, String filename)
    {
        return this.fileTableOwnerREPO.findByUserloginidAndSystemfilename(user, filename).stream().findFirst();
    }

    /**
     * Updates a file table owner record in the database.
     * 
     * @param fileInformation the file table owner information to update
     * @deprecated Use {@link #save()} instead which operates on the {@code data} field
     */
    @Deprecated
    public void update(FileTableOwnerTBL fileInformation)
    {
        this.fileTableOwnerREPO.save(fileInformation);
    }

    /**
     * Saves the current file table owner record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = this.fileTableOwnerREPO.save(data);
    }

    /**
     * Saves all file table owner records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<FileTableOwnerTBL> datalist = this.fileTableOwnerREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }

    /**
     * Checks if the current file's status matches any of the specified statuses.
     * 
     * @param checkAvailability list of status values to check against
     * @return {@code true} if the file's status matches any in the list, {@code false} otherwise
     */
    public boolean checkStatus(ArrayList<Integer> checkAvailability)
    {
        return checkAvailability.stream().anyMatch(x -> Objects.equals(data.getStatus(), x));
    }
}
