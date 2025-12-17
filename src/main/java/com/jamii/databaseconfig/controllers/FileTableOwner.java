package com.jamii.databaseconfig.controllers;

import com.jamii.databaseconfig.model.FileTableOwnerTBL;
import com.jamii.databaseconfig.model.UserLoginTBL;
import com.jamii.databaseconfig.repo.FileTableOwnerREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Component
public class FileTableOwner
{

    /**
     * Setting active statuses
     */
    public final static Integer ACTIVE_STATUS_STORE = 1;
    public final static Integer ACTIVE_STATUS_IN_TRASH = 2;
    public final static Integer ACTIVE_STATUS_DELETED = 3;

    //Creating a table object to reference when creating data for that table
    public FileTableOwnerTBL data = new FileTableOwnerTBL();
    public ArrayList<FileTableOwnerTBL> dataList = new ArrayList<>();
    @Autowired private FileTableOwnerREPO fileTableOwnerREPO;

    public FileTableOwnerTBL add(FileTableOwnerTBL fileTableOwnerTBL)
    {
        return this.fileTableOwnerREPO.save(fileTableOwnerTBL);
    }

    public Optional<FileTableOwnerTBL> fetch(UserLoginTBL user, String filename)
    {
        return this.fileTableOwnerREPO.findByUserloginidAndSystemfilename(user, filename).stream().findFirst();
    }

    /**
     * This has been deprecated use the function save( )
     *
     * @param fileInformation
     */
    @Deprecated
    public void update(FileTableOwnerTBL fileInformation)
    {
        this.fileTableOwnerREPO.save(fileInformation);
    }

    public void save()
    {
        data = this.fileTableOwnerREPO.save(data);
    }

    public void saveAll()
    {
        Iterable<FileTableOwnerTBL> datalist = this.fileTableOwnerREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }

    public boolean checkStatus(ArrayList<Integer> checkAvailability)
    {
        return checkAvailability.stream().anyMatch(x -> Objects.equals(data.getStatus(), x));
    }
}
