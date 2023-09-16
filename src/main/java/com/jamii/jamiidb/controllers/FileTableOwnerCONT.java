package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.FileTableOwnerREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.jamii.jamiidb.model.FileTableOwnerTBL.ACTIVE_STATUS_STORE;

@Service
public class FileTableOwnerCONT {

    @Autowired
    private FileTableOwnerREPO fileTableOwnerREPO ;

    public FileTableOwnerTBL add( FileTableOwnerTBL fileTableOwnerTBL  ){
        return this.fileTableOwnerREPO.save( fileTableOwnerTBL ) ;
    }

    public Optional<FileTableOwnerTBL>getFileByUserLoginIdAndName( UserLoginTBL user , String filename ){
        return this.fileTableOwnerREPO.findByUserloginidAndSystemfilenameAndStatus( user , filename, ACTIVE_STATUS_STORE ).stream( ).findFirst( );
    }

    public void update( FileTableOwnerTBL fileInformation ) {
        this.fileTableOwnerREPO.save( fileInformation ) ;
    }
}
