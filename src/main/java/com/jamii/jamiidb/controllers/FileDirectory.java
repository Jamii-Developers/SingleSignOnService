package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.FileDirectoryTBL;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.FileDirectoryREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class FileDirectory
{

    //Creating a table object to reference when creating data for that table
    public FileDirectoryTBL data = new FileDirectoryTBL();
    public ArrayList<FileDirectoryTBL> dataList = new ArrayList<>();

    @Autowired private FileDirectoryREPO fileDirectoryREPO;

    public FileDirectoryTBL createFileDirectory(UserLoginTBL userLoginTBL, FileTableOwnerTBL fileTableOwnerTBL, String uidirectory)
    {

        data.setUserloginid(userLoginTBL);
        data.setFiletableownerid(fileTableOwnerTBL);
        data.setUidirectory(uidirectory);
        data.setLastupdated(LocalDateTime.now());
        save();
        return data;
    }

    public Optional<FileDirectoryTBL> fetch(UserLoginTBL userLoginTBL, FileTableOwnerTBL fileTableOwnerTBL)
    {
        return this.fileDirectoryREPO.findByUserloginidAndFiletableownerid(userLoginTBL, fileTableOwnerTBL).stream().findFirst();
    }

    /**
     * This has method has been deprecated use the method save( )
     *
     * @param fileDirectoryTBL
     */
    @Deprecated
    public void update(FileDirectoryTBL fileDirectoryTBL)
    {
        this.fileDirectoryREPO.save(fileDirectoryTBL);
    }

    /**
     * This has method has been deprecated use the method save( )
     *
     * @param fileDirectoryTBL
     */
    @Deprecated
    public void add(FileDirectoryTBL fileDirectoryTBL)
    {
        this.fileDirectoryREPO.save(fileDirectoryTBL);
    }

    public void save()
    {
        data = this.fileDirectoryREPO.save(data);
    }

    public void saveAll()
    {
        Iterable<FileDirectoryTBL> datalist = this.fileDirectoryREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
