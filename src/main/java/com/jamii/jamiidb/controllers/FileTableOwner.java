package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.FileTableOwnerREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class FileTableOwner {

    @Autowired
    private FileTableOwnerREPO fileTableOwnerREPO;

    //Creating a table object to reference when creating data for that table
    public FileTableOwnerTBL data = new FileTableOwnerTBL( );
    public ArrayList< FileTableOwnerTBL > dataList = new ArrayList<>( );

    /**
     * Setting active statuses
     */
    public final static Integer ACTIVE_STATUS_STORE             = 1;
    public final static Integer ACTIVE_STATUS_IN_TRASH          = 2;
    public final static Integer ACTIVE_STATUS_DELETED           = 3;

    public FileTableOwnerTBL add(FileTableOwnerTBL fileTableOwnerTBL) {
        return this.fileTableOwnerREPO.save(fileTableOwnerTBL);
    }

    public Optional<FileTableOwnerTBL> fetch(UserLoginTBL user, String filename) {
        return this.fileTableOwnerREPO.findByUserloginidAndSystemfilename(user, filename).stream().findFirst();
    }

    /**
     * This has been deprecated use the fuction save( )
     * @param fileInformation
     */
    @Deprecated
    public void update(FileTableOwnerTBL fileInformation) {
        this.fileTableOwnerREPO.save(fileInformation);
    }

    public void save( ){
        data = this.fileTableOwnerREPO.save( data );
    }

    public void saveAll( ){
        Iterable<FileTableOwnerTBL> datalist = this.fileTableOwnerREPO.saveAll( dataList ) ;
        dataList.clear( );
        datalist.forEach( x -> dataList.add( x ) );
    }

}
