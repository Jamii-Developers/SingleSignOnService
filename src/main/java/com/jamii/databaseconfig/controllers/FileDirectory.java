package com.jamii.databaseconfig.controllers;

import com.jamii.databaseconfig.model.FileDirectoryTBL;
import com.jamii.databaseconfig.model.FileTableOwnerTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.databaseconfig.repo.FileDirectoryREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Service component for managing file directory records in the database.
 * 
 * <p>This class handles the storage and retrieval of file directory information,
 * associating directories with users and file table owners.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Create file directory records</li>
 *     <li>Fetch directory information by user and file table owner</li>
 *     <li>Save directory records individually or in batch</li>
 * </ul>
 */
@Component
public class FileDirectory
{

    /**
     * Single file directory record instance for creating new entries.
     */
    public FileDirectoryTBL data = new FileDirectoryTBL();
    
    /**
     * List of file directory records for batch operations.
     */
    public ArrayList<FileDirectoryTBL> dataList = new ArrayList<>();

    @Autowired private FileDirectoryREPO fileDirectoryREPO;

    /**
     * Creates a new file directory record for a user and file table owner.
     * 
     * @param userLoginTBL the user to associate the directory with
     * @param fileTableOwnerTBL the file table owner to associate the directory with
     * @param uidirectory the unique identifier for the directory
     * @return the saved file directory record with generated ID
     */
    public FileDirectoryTBL createFileDirectory(UserLoginTBL userLoginTBL, FileTableOwnerTBL fileTableOwnerTBL, String uidirectory)
    {

        data.setUserloginid(userLoginTBL);
        data.setFiletableownerid(fileTableOwnerTBL);
        data.setUidirectory(uidirectory);
        data.setLastupdated(LocalDateTime.now());
        save();
        return data;
    }

    /**
     * Fetches file directory information for a specific user and file table owner.
     * 
     * @param userLoginTBL the user to fetch directory information for
     * @param fileTableOwnerTBL the file table owner to fetch directory information for
     * @return an Optional containing the file directory information if found, empty otherwise
     */
    public Optional<FileDirectoryTBL> fetch(UserLoginTBL userLoginTBL, FileTableOwnerTBL fileTableOwnerTBL)
    {
        return this.fileDirectoryREPO.findByUserloginidAndFiletableownerid(userLoginTBL, fileTableOwnerTBL).stream().findFirst();
    }

    /**
     * Updates a file directory record in the database.
     * 
     * @param fileDirectoryTBL the file directory to update
     * @deprecated Use {@link #save()} instead which operates on the {@code data} field
     */
    @Deprecated
    public void update(FileDirectoryTBL fileDirectoryTBL)
    {
        this.fileDirectoryREPO.save(fileDirectoryTBL);
    }

    /**
     * Adds a file directory record to the database.
     * 
     * @param fileDirectoryTBL the file directory to add
     * @deprecated Use {@link #save()} instead which operates on the {@code data} field
     */
    @Deprecated
    public void add(FileDirectoryTBL fileDirectoryTBL)
    {
        this.fileDirectoryREPO.save(fileDirectoryTBL);
    }

    /**
     * Saves the current file directory record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = this.fileDirectoryREPO.save(data);
    }

    /**
     * Saves all file directory records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<FileDirectoryTBL> datalist = this.fileDirectoryREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
